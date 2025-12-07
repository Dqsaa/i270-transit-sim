package main;

public class RoboTaxi {
    int vin;
    int state; // 0 idle, 1 dispatching, 2 in transit
    int capacity;
    int openSeats;
    int currentLocation;
    int nextLocation;
    double currentLocationTime;
    double nextLocationTime;

    Commuter[] passengers;

    public RoboTaxi(int id, int numTaxiSeats) {
        vin = id;
        capacity = numTaxiSeats;
        openSeats = capacity;
        currentLocation = 0; // STATION
        state = 0; // idle
        passengers = new Commuter[capacity];
    }

    public void arriveLocation(int locID, double time) {
        currentLocation = nextLocation;
        currentLocationTime = time;

        setNextLocation(locID);
    }

    public int getVin() {
        return vin;
    }

    public int getLocation() {
        return currentLocation;
    }

    public int getNextLocation() {
        return nextLocation;
    }

    public int getOpenSeats() {
        return openSeats;
    }

    public double getCurrentLocationTime() {
        return currentLocationTime;
    }

    public double getNextLocationTime() {
        return nextLocationTime;
    }

    public void setNextLocationTime(double nextTime) {

        nextLocationTime = nextTime;
    }

    public void setCurrentLocation(int locID) {
        currentLocation = locID;
    }

    public void setNextLocation(int locID) {
        nextLocation = locID;
    }

    public void pickupCommuter(int locID, Commuter commuter) {
        if (openSeats > 0) {
            passengers[capacity - openSeats] = commuter;
            openSeats--;
        } else {
            System.out.println("ERROR: Passenger assigned to full vehicle");
        }
    }

    public void resetAtStation() {
        currentLocation = 0;
        nextLocation = 0;
        state = 0;
        openSeats = capacity;
        currentLocationTime = 0.0;
        nextLocationTime = 0.0;
        for (int i = 0; i < capacity; i++) {
            passengers[i] = null;
        }
    }

}