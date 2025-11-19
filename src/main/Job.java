package main;

public class Job {
    public double enterTime;
    public double startTime;
    public double serviceTime;
    public double endTime;
    public double finishedTime;
    Commuter customer;

    public Job ( double currentTime, Commuter c ) {
        enterTime = currentTime;
        customer = c;
    }
}
