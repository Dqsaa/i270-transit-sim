package main;

import java.util.Random;

public class ArrivalProcess {
    private final ExponentialDistribution interArrivalDist;
    private double nextArrivalTime;
    private final Random rng;

    // These define where this stream of commuters is coming from / going to
    private final int startNode;
    private final int destinationNode;

    public ArrivalProcess(double startTime,
            double lambda,
            Random rng,
            int startNode,
            int destinationNode) {

        this.interArrivalDist = new ExponentialDistribution(lambda);
        this.rng = rng;
        this.startNode = startNode;
        this.destinationNode = destinationNode;
        this.nextArrivalTime = startTime + interArrivalDist.sample();
    }

    public double getNextArrivalTime() {
        return nextArrivalTime;
    }

    public Commuter nextCommuter(double currentTime) {
        // Pick a random group size in [minGroupSize, maxGroupSize]

        Commuter c = new Commuter(nextArrivalTime, startNode, destinationNode);

        nextArrivalTime += interArrivalDist.sample();
        return c;
    }
}
