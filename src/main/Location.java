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

    // TODO: accessors for times

}
