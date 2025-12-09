package main;

import java.util.Queue;

public class Dispatcher {
    Stack<RoboTaxi> taxiFleet = new Stack<RoboTaxi>();
    Route[] routes;
    int taxiCount;
    final int taxiSeats;
    int totalCalls =0;

    public Dispatcher(int numTaxi, int numTaxiSeats, Route[] rts) {
        taxiCount = numTaxi;
        taxiSeats = numTaxiSeats;
        routes = rts;

        for (int i = 0; i < numTaxi; i++) {
            taxiFleet.push(new RoboTaxi((numTaxi - i), numTaxiSeats));
        }
    }

    public void update(double currentTime, Queue<Commuter> stationQueue) {
        for (int i = 0; i < routes.length; i++) {
            routes[i].update(currentTime, this, stationQueue);
        }

        // check for taxis at station
        // empty passengers and return to stack
        if(currentTime > 160) {
            System.out.println(totalCalls + " calls");
        }
    }

    public Route getRoute(int locID) {
        if (locID < 100) {
            return null;
        } else if (locID < 200) {
            return routes[0];
        } else if (locID < 300) {
            return routes[1];
        } else if (locID < 400) {
            return routes[2];
        } else if (locID < 500) {
            return routes[3];
        } else if (locID < 600) {
            return routes[4];
        } else if (locID < 700) {
            return routes[5];
        } else if (locID < 800) {
            return routes[6];
        } else if (locID < 900) {
            return routes[7];
        } else
            return null;
    }

    public void callTaxi(double currentTime, int locID, Commuter commuter) {
        Route currentRoute = getRoute(locID);
        totalCalls++;
        if (currentRoute == null) {
            System.out.println(" Invalid locID ");
        } else {
            RoboTaxi rideShare = currentRoute.findTaxiFor(locID);
            if (rideShare != null) {
                rideShare.pickupCommuter(locID, commuter);
                System.out.println(" > commuter assigned to vehicle ID:" + rideShare.getVin());
            } else if (taxiFleet.hasNext()) {

                rideShare = taxiFleet.pop();
                currentRoute.addTaxi(currentTime, rideShare, locID);
                rideShare.pickupCommuter(locID, commuter);
                System.out
                        .println(" > new vehicle dispatched with ID:" + rideShare.getVin() + "\n \t Traveling to: "
                                + rideShare.getNextLocation() + " arriving at " + rideShare.getNextLocationTime());
            } else {
                currentRoute.locations[currentRoute.getLocationIndex(locID)].arrive(commuter);
            }
        }
    }

    public void returnToFleet(RoboTaxi taxi) {
        taxiFleet.push(taxi);
    }

}