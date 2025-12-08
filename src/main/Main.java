package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {

        public static void main(String[] args) {
                try {
                        // Load locations + routes from files
                        RouteBuilder rb = new RouteBuilder();

                        System.out.println("Looking for: " + new java.io.File("locations.csv").getAbsolutePath());

                        if (!rb.readLocations("locations.csv") || !rb.readRoutes("routes.csv")) {
                                System.out.println("ERROR: Could not read locations.csv or routes.csv");
                                return;
                        }

                        Route[] routes = rb.build();
                        System.out.println("Loaded routes: " + routes.length);

                        // Collect all unique location IDs from the routes
                        List<Integer> allLocationIds = new ArrayList<>();

                        for (Route r : routes) {
                                for (int i = 0; i < r.getLocationCount(); i++) {
                                        int id = r.locations[i].getID();

                                        if (id != 0 && !allLocationIds.contains(id)) {
                                                allLocationIds.add(id);
                                        }
                                }
                        }

                        System.out.println("Arrival streams created for locations: " + allLocationIds);

                        // Create ArrivalProcess for each location
                        Random rng = new Random(12345);

                        // Population density / arrival rates for each location
                        Map<Integer, Double> lambdaByLocation = new HashMap<>();

                        double arrivalRate = 0.02; // rates unified for all locations for now, will refine later

                        // Route 103-series
                        lambdaByLocation.put(103, arrivalRate);
                        lambdaByLocation.put(102, arrivalRate);
                        lambdaByLocation.put(101, arrivalRate);

                        // Route 204-series
                        lambdaByLocation.put(204, arrivalRate);
                        lambdaByLocation.put(203, arrivalRate);
                        lambdaByLocation.put(202, arrivalRate);
                        lambdaByLocation.put(201, arrivalRate);

                        // Route 304-series
                        lambdaByLocation.put(304, arrivalRate);
                        lambdaByLocation.put(303, arrivalRate);
                        lambdaByLocation.put(302, arrivalRate);
                        lambdaByLocation.put(301, arrivalRate);

                        // Route 403-series
                        lambdaByLocation.put(403, arrivalRate);
                        lambdaByLocation.put(402, arrivalRate);
                        lambdaByLocation.put(401, arrivalRate);

                        // Route 503-series
                        lambdaByLocation.put(503, arrivalRate);
                        lambdaByLocation.put(502, arrivalRate);
                        lambdaByLocation.put(501, arrivalRate);

                        // Route 604-series
                        lambdaByLocation.put(604, arrivalRate);
                        lambdaByLocation.put(603, arrivalRate);
                        lambdaByLocation.put(602, arrivalRate);
                        lambdaByLocation.put(601, arrivalRate);

                        // Route 704-series
                        lambdaByLocation.put(704, arrivalRate);
                        lambdaByLocation.put(703, arrivalRate);
                        lambdaByLocation.put(702, arrivalRate);
                        lambdaByLocation.put(701, arrivalRate);

                        // Build arrival processes
                        Map<Integer, ArrivalProcess> streams = new HashMap<>();

                        for (int locId : allLocationIds) {
                                double lambda = lambdaByLocation.getOrDefault(locId, 0.01);

                                ArrivalProcess ap = new ArrivalProcess(
                                                0.0, // start time
                                                lambda, // location-specific arrival rate
                                                rng,
                                                locId, // starting location
                                                0 // destination = station (ID 0)
                                );

                                streams.put(locId, ap);
                        }

                        // create two trains

                        double trainTravelTime = 40.0; // minutes Frederick --> DC

                        // Train 1 schedule
                        List<Double> departureTimes1 = new ArrayList<>();
                        departureTimes1.add(20.0);
                        departureTimes1.add(60.0);
                        departureTimes1.add(100.0);
                        departureTimes1.add(140.0);
                        departureTimes1.add(180.0);

                        Train train1 = new Train("T1", departureTimes1, trainTravelTime);

                        // Train 2 schedule (offset from Train 1 by ~20 minutes)
                        List<Double> departureTimes2 = new ArrayList<>();
                        departureTimes2.add(40.0);
                        departureTimes2.add(80.0);
                        departureTimes2.add(120.0);
                        departureTimes2.add(160.0);

                        Train train2 = new Train("T2", departureTimes2, trainTravelTime);

                        // Put both trains into a list
                        List<Train> trains = new ArrayList<>();
                        trains.add(train1);
                        trains.add(train2);
                        // build the sim with multiple trains

                        double simTime = 200.0;

                        Simulation sim = new Simulation(
                                        simTime,
                                        streams,
                                        trains, // <-- list of trains now
                                        routes);

                        // Run Simulation
                        Metrics m = sim.run();

                        System.out.println("\n=== Simulation Results ===");
                        System.out.printf("Average Wait: %.2f\n", m.getAvgWait());
                        System.out.printf("Average Passage: %.2f\n", m.getAvgPassage());
                        System.out.printf("Throughput: %.4f commuters/min\n", m.getThroughput());
                        System.out.println("Completed Trips: " + m.getCompletedCount());

                } catch (Exception ex) {
                        System.out.println("Simulation failed: " + ex.getMessage());
                        ex.printStackTrace();
                }
        }
}
