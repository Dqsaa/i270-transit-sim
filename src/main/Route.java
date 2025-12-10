package main;

import java.util.Queue;

import java.util.LinkedList;

public class Route {
    Location[] locations; // in order from station, 0 is closest. Some array positions may be null.
    int locationCount;
    List<RoboTaxi> taxis; // no order assumed

    public int getLocationCount() {
        return locationCount;
    }

    public int getTaxiCount() {
        return taxis.getLength();
    }

    public Route(int maxLocations) {
        locationCount = 0;
        locations = new Location[maxLocations];
        taxis = new List<RoboTaxi>();
    };

    public boolean appendToRoute(Location loc) {
        if (locationCount == locations.length) {
            return false;
        }

        locations[locationCount++] = loc;
        return true;
    }

    public void addTaxi(double currentTime, RoboTaxi tx, int locationID) {
        tx.setCurrentLocation(1000);
        tx.setNextLocation(locationID);
        tx.setNextLocationTime(currentTime + locations[getLocationIndex(locationID)].getTimeFromStation());
        taxis.addToHead(tx);
    }

    public RoboTaxi removeTaxi(RoboTaxi tx) {
        taxis.remove(tx);
        tx.setCurrentLocation(0);
        tx.setNextLocation(0);
        return tx;
    }

    public void update(double currentTime, Dispatcher dispatcher, Queue<Commuter> stationQueue) {
        taxis.initIteration();
        RoboTaxi taxi;
        while ((taxi = taxis.next()) != null) {
            if (currentTime > taxi.getNextLocationTime()) {
                if (taxi.getNextLocation() == 0) {
                    System.out.println("[" + currentTime + "] Taxi ID#" + taxi.getVin()
                            + " arriving at STATION from " + taxi.getLocation());

                    dispatcher.totalTaxiTrips++;

                    // mark taxi at station
                    taxi.setCurrentLocation(0);
                    taxi.setNextLocation(0);
                    taxi.setNextLocationTime(Double.POSITIVE_INFINITY);

                    int dropped = 0;

                    // unload all passengers into the station queue
                    for (int i = 0; i < taxi.passengers.length; i++) {
                        Commuter p = taxi.passengers[i];
                        if (p != null) {
                            stationQueue.add(p);
                            taxi.passengers[i] = null;
                            dropped++;
                        }
                    }

                    System.out.println("    < dropped off " + dropped
                            + " commuters, station queue size = " + stationQueue.size() + " >");

                    taxi.openSeats = taxi.capacity; // reset seats

                    dispatcher.recordTrip(dropped);

                    // remove this taxi from the route and return it to the fleet
                    removeTaxi(taxi);
                    dispatcher.returnToFleet(taxi);
                } else {
                    // update the location and next location -- next segment
                    System.out.println("[" + currentTime + "] Taxi ID#" + taxi.getVin() + " arriving at "
                            + taxi.getNextLocation() + " from " + taxi.getLocation());
                    taxi.arriveLocation(currentTime);

                    int currentLocId = taxi.getLocation();
                    int nextLocID = getNextLocationID(taxi.getLocation());
                    taxi.setNextLocation(nextLocID);
                    taxi.setNextLocationTime(currentTime + locations[getLocationIndex(currentLocId)].getTimeToNext());

                    if (taxi.getOpenSeats() > 0) {
                        while (locations[getLocationIndex(currentLocId)].hasPassenger() && taxi.getOpenSeats() > 0) {
                            taxi.pickupCommuter(currentLocId, locations[getLocationIndex(currentLocId)].pickup());
                            // System.out.println("+++++++++ picked up from queue @" + currentLocId);
                        }
                    }
                }
            }
        }
    }

    // Returns first taxi that has space that has this location ID in its remaining
    // list
    // of pickup locations, on the way back to station. Taxis pick up only on
    // return.
    public RoboTaxi findTaxiFor(int locationID) {
        if (locationCount == 0)
            return null;

        RoboTaxi currentClosest = null;
        double currentShortestTime = 0.0; // distance FROM taxi to pickup point. We want the closest that has any open
                                          // spots.

        taxis.initIteration();
        RoboTaxi currentTaxi = null;
        while ((currentTaxi = taxis.next()) != null) {
            if (currentTaxi.getOpenSeats() > 0) {
                // TODO: probably should use next location, when available
                double tmBetween = timeBetween(currentTaxi.getLocation(), locationID);
                if (tmBetween >= 0.0) {
                    if ((currentClosest == null) ||
                            (currentClosest != null && tmBetween < currentShortestTime)) {
                        currentClosest = currentTaxi;
                        currentShortestTime = tmBetween;
                    }
                }
            }
        }
        return currentClosest;
    }

    // if dst lies between src and station, returns total time from location src to
    // location dst.
    // Otherwise, returns -1. If src and dst are the same, return 0.
    private double timeBetween(int srcId, int dstId) {
        int srcIndex = getLocationIndex(srcId);
        int dstIndex = getLocationIndex(dstId);
        // as locations are stored consecutively starting from station,
        // the time between exists only if srcIndex >= dstIndex
        if (srcIndex == -1 || dstIndex == -1 || srcIndex < dstIndex) {
            return -1;
        }

        double time = 0.0;
        for (int index = srcIndex; index >= dstIndex; index--) {
            time += locations[index].getTimeToNext();
        }

        return time;
    }

    // Returns index of this location in our array, -1 if not found.
    public int getLocationIndex(int locId) {
        for (int index = 0; index < locationCount; index++) {
            // System.out.println("Comparing: " + locId + " to " + locations[index].getID()
            // );
            if (locations[index].getID() == locId) {
                return index;
            }
        }
        return -1;
    }

    public int getNextLocationID(int locId) {
        int index = getLocationIndex(locId);
        if (index == 0) {
            return 0; // station is next
        } else {
            return locations[index - 1].getID();
        }
    }
}
