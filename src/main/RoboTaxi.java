package main;

public class RoboTaxi {
    int vin;
    int state; // 0 idle, 1 dispatching, 2 in transit
    int route;
    int capacity;
    int openSeats;
    int currentLocation;
    int nextLocation;


    public RoboTaxi(int id, int numTaxiSeats) {
        vin = id;
        capacity = numTaxiSeats;
        openSeats = capacity;
        currentLocation = 0; // STATION
        state = 0; // idle
    }

    public void startRoute ( int rt, int locID ) {
        route = rt;
        nextLocation = locID;
    }

    public int getRoute() {
        return route;
    }

    public int getLocation() {
        return currentLocation;
    }

    public int getOpenSeats() {
        return openSeats;
    }

    public void pickupCommuter ( int locID, Commuter commuter) {

    }
}