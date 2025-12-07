package main;

public class Commuter {

    private final double arrivalTime; // time this commuter group enters the system
    private double totalWait; // total minutes spent waiting
    private double totalTravel; // total minutes spent riding (taxi + train)

    private final int startLocation; // origin node
    private final int destination;// destination node

    public Commuter(double arrivalTime, int startLocation, int destination) {
        this.arrivalTime = arrivalTime;
        this.startLocation = startLocation;
        this.destination = destination;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public int getStartLocation() {
        return startLocation;
    }

    public int getDestination() {
        return destination;
    }

    public double getTotalWait() {
        return totalWait;
    }

    public double getTotalTravel() {
        return totalTravel;
    }

    public void addWait(double dt) {
        totalWait += dt;
    }

    public void addTravel(double dt) {
        totalTravel += dt;
    }

    public double getElapsed() {
        return totalWait + totalTravel;
    }

    // Stubs for methods required by Train class
    public void markJourneyStart(double x) {
    }

    public void markJourneyEnd(double x) {
    }

    public void setState(State st) {
    }

    // Enum required by Train class
    public static enum State {
        ON_TRAIN, COMPLETED
    };
}