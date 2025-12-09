package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Simulation {
    int numTaxi = 200;
    int numSeats = 8;

    // simple wrapper for each train's state (schedule + onboard passengers)
    private static class TrainState {
        final Train train;
        final String label;
        final List<Double> departureTimes;
        int nextDepartureIndex = 0;
        double nextDepartureTime;
        double nextArrivalTime = Double.POSITIVE_INFINITY;
        final List<Commuter> onboard = new ArrayList<>();


        TrainState(Train train, String label) {
            this.train = train;
            this.label = label;
            this.departureTimes = train.getDepartureTimes();
            if (!departureTimes.isEmpty()) {
                this.nextDepartureTime = departureTimes.get(0);
            } else {
                this.nextDepartureTime = Double.POSITIVE_INFINITY;
            }
        }
    }

    // clock
    private double currentTime = 0.0;
    private final double stopTime;

    // one ArrivalProcess per location (locID -> process)
    private final Map<Integer, ArrivalProcess> arrivalsByLocation;

    // all train states (we're now supporting multiple trains)
    private final List<TrainState> trains = new ArrayList<>();

    // routes / road network
    private final Route[] routes;

    // dispatcher holds idle taxis in a stack
    private final Dispatcher dispatcher;

    // people at the station waiting for the train
    private final java.util.Queue<Commuter> stationQueue = new java.util.LinkedList<>();

    // data for metrics
    private final List<Double> waitTimes = new ArrayList<>();
    private final List<Double> passageTimes = new ArrayList<>();
    private int completedCount = 0;

    public Simulation(double simTime,
            Map<Integer, ArrivalProcess> arrivalsByLocation,
            List<Train> trainList,
            Route[] routes) {

        this.stopTime = simTime;
        this.arrivalsByLocation = arrivalsByLocation;
        this.routes = routes;

        // basic taxi fleet (change numbers if you want)
        this.dispatcher = new Dispatcher(numTaxi, numSeats, routes);

        // wrap each Train into a TrainState with a simple label like "Train1", "Train2"
        for (int i = 0; i < trainList.size(); i++) {
            Train t = trainList.get(i);
            String label = "Train" + (i + 1);
            trains.add(new TrainState(t, label));
        }
    }

    // ///////////// MAIN LOOP ///////////////////////

    public Metrics run() {

        System.out.println("=== Traffic Simulation (Train + RoboTaxis + Routes) ===");

        final double EPS = 1e-9;

        while (currentTime < stopTime) {

            dispatcher.update(currentTime, stationQueue);

            // 1) next arrival (across all locations)
            double nextArrivalTime = Double.POSITIVE_INFINITY;
            int nextArrivalLoc = -1;
            ArrivalProcess nextArrivalProcess = null;

            for (Map.Entry<Integer, ArrivalProcess> entry : arrivalsByLocation.entrySet()) {
                int locId = entry.getKey();
                ArrivalProcess ap = entry.getValue();
                double t = ap.getNextArrivalTime();
                if (t < nextArrivalTime) {
                    nextArrivalTime = t;
                    nextArrivalLoc = locId;
                    nextArrivalProcess = ap;
                }
            }

            // 2) next departure / arrival across all trains
            double earliestDeparture = Double.POSITIVE_INFINITY;
            double earliestTrainArrival = Double.POSITIVE_INFINITY;

            for (TrainState ts : trains) {
                if (ts.nextDepartureTime < earliestDeparture) {
                    earliestDeparture = ts.nextDepartureTime;
                }
                if (ts.nextArrivalTime < earliestTrainArrival) {
                    earliestTrainArrival = ts.nextArrivalTime;
                }
            }

            double nextEventTime = minPositive(
                    nextArrivalTime,
                    earliestDeparture,
                    earliestTrainArrival);

            if (nextEventTime == Double.POSITIVE_INFINITY) {
                break; // nothing else to do
            }

            //currentTime = nextEventTime;
            currentTime += 0.01;

            // 1) location arrival
            if (currentTime > nextArrivalTime && nextArrivalProcess != null) {
                handleArrival(nextArrivalLoc, nextArrivalProcess);
            }

            // 2) trains ARRIVING in DC at this time
            for (TrainState ts : trains) {
                if (Math.abs(currentTime - ts.nextArrivalTime) < EPS) {
                    handleTrainArrival(ts);
                }
            }

            // 3) trains DEPARTING Frederick at this time
            for (TrainState ts : trains) {
                if (Math.abs(currentTime - ts.nextDepartureTime) < EPS) {
                    handleTrainDeparture(ts);
                }
            }
        }

        double avgWait = average(waitTimes);
        double avgPassage = average(passageTimes);
        double throughput = (currentTime > 0.0)
                ? (completedCount / currentTime)
                : 0.0;

        double[] utilizations = new double[0]; // still not tracking this

        return new Metrics(
                avgWait,
                avgPassage,
                throughput,
                completedCount,
                waitTimes,
                passageTimes,
                utilizations);
    }

    ///////////// EVENTS ///////////////////////

    // commuter shows up at a location (e.g. 10, 200, 300)
    private void handleArrival(int locId, ArrivalProcess ap) {
        Commuter c = ap.nextCommuter(currentTime);

        //System.out.printf("[%.2f] Commuter arrived at location %d%n", currentTime, locId);
        //System.out.println("        Taxi requested from location " + locId);

        // decide which taxi/route serves this commuter
        dispatcher.callTaxi(currentTime, locId, c);

        // taxi to this location
        // double taxiTravel = getTaxiTravelTime(locId);
        // c.addWait(taxiTravel);

        // System.out.printf(" Taxi travel = %.2f minutes%n", taxiTravel);

        // after taxi, they end up at the station and wait for the train
        // stationQueue.add(c);
        // System.out.println(" After taxi, commuter now at station. Queue size " +
        // stationQueue.size());
    }

    // train boards from the station queue and departs
    private void handleTrainDeparture(TrainState ts) {
        Train train = ts.train;

        System.out.printf("[%.2f] %s departing Frederick, queue size %d%n",
                currentTime, ts.label, stationQueue.size());

        List<Commuter> boarded = train.board(stationQueue, currentTime);

        ts.onboard.clear();
        ts.onboard.addAll(boarded);
        System.out.println(ts.onboard.size() + " 0000");
        // record wait times (arrival --> train boarding)
        for (Commuter c : boarded) {
            double wait = currentTime - c.getArrivalTime();
            c.addWait(wait);
            //System.out.printf("        Commuter boarded %s, waited %.2f minutes%n", ts.label, wait);
        }

        // schedule this trip's DC arrival
        if (!boarded.isEmpty()) {
            ts.nextArrivalTime = currentTime + train.getTransitTime();
            System.out.println("        " + ts.label + " will arrive in DC at t=" + ts.nextArrivalTime);
        } else {
            System.out.println("        " + ts.label + " departed empty; no arrival event scheduled.");
            ts.nextArrivalTime = Double.POSITIVE_INFINITY;
        }

        // move to next departure in this train's schedule
        ts.nextDepartureIndex++;
        if (ts.nextDepartureIndex < ts.departureTimes.size()) {
            ts.nextDepartureTime = ts.departureTimes.get(ts.nextDepartureIndex);
        } else {
            ts.nextDepartureTime = Double.POSITIVE_INFINITY;
        }
    }

    // train reaches DC; trips finish
    private void handleTrainArrival(TrainState ts) {
        Train train = ts.train;
        double travelTime = train.getTransitTime();

        System.out.printf("[%.2f] %s arrived in DC with %d commuters%n",
                currentTime, ts.label, ts.onboard.size());

        for (Commuter c : ts.onboard) {
            c.addTravel(travelTime);

            double wait = c.getTotalWait(); // already set at departure
            double totalTrip = c.getElapsed(); // wait + taxi + train

            waitTimes.add(wait);
            passageTimes.add(totalTrip);
            completedCount++;

            //System.out.printf("        Commuter finished trip on %s: wait=%.2f min, total=%.2f min%n",
            //        ts.label, wait, totalTrip);
        }

        train.disembark(ts.onboard, currentTime);

        ts.onboard.clear();
        ts.nextArrivalTime = Double.POSITIVE_INFINITY;
    }

    //////////////// taxis / routes ////////////////////

    //////////// HELPERS ////////////////////////

    private static double minPositive(double... values) {
        double min = Double.POSITIVE_INFINITY;
        for (double v : values) {
            if (v >= 0.0 && v < min) {
                min = v;
            }
        }
        return min;
    }

    private static double average(List<Double> values) {
        if (values == null || values.isEmpty())
            return 0.0;
        double sum = 0.0;
        for (double v : values) {
            sum += v;
        }
        return sum / values.size();
    }
}