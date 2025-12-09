package main;

public class Location {
    private int id;
    private double timeFromStation, timeToNext;
    Queue<Commuter> commuterQueue;

    public Location( int idvalue, double tm_fr, double tm_to ) {
        id = idvalue;
        timeFromStation = tm_fr;
        timeToNext = tm_to;
        commuterQueue = new Queue<>();
    }
    public int getID() { return id; }

    public double getTimeFromStation() { return timeFromStation; }

    public double getTimeToNext() { return timeToNext; }

    public void arrive( Commuter c ) {
        commuterQueue.enqueue( c );
        System.out.println("====  commuter  added  to queue at " + id);
    }

    public Commuter pickup() {
        return commuterQueue.dequeue();
    }

    public boolean hasPassenger() {
        return !commuterQueue.isEmpty();
    }
}
