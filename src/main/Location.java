package main;

public class Location {
    private int id;
    private double timeFromStation, timeToNext;

    public Location( int idvalue, double tm_fr, double tm_to ) {
        id = idvalue;
        timeFromStation = tm_fr;
        timeToNext = tm_to;
    }
    public int getID() { return id; }

    public double getTimeFromStation() { return timeFromStation; }

    public double getTimeToNext() { return timeToNext; }
}
