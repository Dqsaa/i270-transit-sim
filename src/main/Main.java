package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {

        public static void main(String[] args) {
                try {
                        // Load locations + routes from files
                        RouteBuilder rb = new RouteBuilder();

                        System.out.println("Looking for: " + new java.io.File("locations.csv").getAbsolutePath());

                        if (!rb.readLocations("locations.csv") || !rb.readRoutes("routes.csv")) {
                                System.out.println("ERROR: Could not read locations.csv or routes.csv");
                                return;
                        }

                        Route[] routes = rb.build();
                        System.out.println("Loaded routes: " + routes.length);

                        // Collect all unique location IDs from the routes
                        List<Integer> allLocationIds = new ArrayList<>();

                        for (Route r : routes) {
                                for (int i = 0; i < r.getLocationCount(); i++) {
                                        int id = r.locations[i].getID();

                                        if (id != 0 && !allLocationIds.contains(id)) {
                                                allLocationIds.add(id);
                                        }
                                }
                        }

                        System.out.println("Arrival streams created for locations: " + allLocationIds);

                        // Create ArrivalProcess for each location
                        Random rng = new Random(12345);

                        // Population density / arrival rates for each location
                        Map<Integer, Double> lambdaByLocation = new HashMap<>();

                        // Route 103-series
                        lambdaByLocation.put(103, 0.030); // Walkersville - large suburb (~6k pop)
                        lambdaByLocation.put(102, 0.018); // Worman's Mill - dense apartments
                        lambdaByLocation.put(101, 0.010); // TJ area - smaller housing base

                        // Route 204-series
                        lambdaByLocation.put(204, 0.012); // New Market - small town
                        lambdaByLocation.put(203, 0.025); // Oakdale - big suburban growth
                        lambdaByLocation.put(202, 0.028); // Spring Ridge - very dense suburb
                        lambdaByLocation.put(201, 0.004); // Airport area - few households

                        // Route 304-series
                        lambdaByLocation.put(304, 0.035); // Urbana - huge commuter suburb
                        lambdaByLocation.put(303, 0.010); // FSK Mall workforce
                        lambdaByLocation.put(302, 0.008); // Walmart area - retail workers
                        lambdaByLocation.put(301, 0.006); // Costco workforce

                        // Route 403-series
                        lambdaByLocation.put(403, 0.050); // Ballenger Creek - massive ~25k CDP
                        lambdaByLocation.put(402, 0.015); // Westview offices + retail
                        lambdaByLocation.put(401, 0.012); // New Design corridor

                        // Route 503-series
                        lambdaByLocation.put(503, 0.014); // Middletown
                        lambdaByLocation.put(502, 0.006); // Braddock Heights - sparse
                        lambdaByLocation.put(501, 0.030); // Golden Mile - dense apartments

                        // Route 604-series
                        lambdaByLocation.put(604, 0.020); // Whittier - large housing development
                        lambdaByLocation.put(603, 0.010); // Old Farm
                        lambdaByLocation.put(602, 0.008); // Hood College students
                        lambdaByLocation.put(601, 0.012); // Baker Park neighborhood

                        // Route 704-series
                        lambdaByLocation.put(704, 0.015); // FCC students
                        lambdaByLocation.put(703, 0.040); // Fort Detrick - huge workforce
                        lambdaByLocation.put(702, 0.015); // TJ Dr corridor
                        lambdaByLocation.put(701, 0.020); // FMH hospital workers

                        // Route 804-series
                        lambdaByLocation.put(804, 0.010); // Jefferson small town
                        lambdaByLocation.put(803, 0.020); // 340 Park & Ride morning spike
                        lambdaByLocation.put(802, 0.008); // Jefferson Place apartments
                        lambdaByLocation.put(801, 0.010); // Carrollton Park apartments

                        // Build arrival processes
                        Map<Integer, ArrivalProcess> streams = new HashMap<>();

                        for (int locId : allLocationIds) {
                                double lambda = lambdaByLocation.getOrDefault(locId, 0.01);

                                ArrivalProcess ap = new ArrivalProcess(
                                                0.0, // start time
                                                lambda, // location-specific arrival rate
                                                rng,
                                                locId, // starting location
                                                0 // destination = station (ID 0)
                                );

                                streams.put(locId, ap);
                        }

                        // create two trains

                        double trainTravelTime = 20.0; // minutes Frederick --> DC

                        // Train 1 schedule
                        List<Double> departureTimes1 = new ArrayList<>();
                        departureTimes1.add(20.0);
                        departureTimes1.add(40.0);
                        departureTimes1.add(60.0);
                        departureTimes1.add(80.0);
                        departureTimes1.add(100.0);
                        departureTimes1.add(120.0);
                        departureTimes1.add(140.0);
                        departureTimes1.add(160.0);
                        departureTimes1.add(180.0);

                        Train train1 = new Train("T1", departureTimes1, trainTravelTime);

                        // Train 2 schedule (offset from Train 1 by ~20 minutes)
                        List<Double> departureTimes2 = new ArrayList<>();
                        departureTimes2.add(30.0);
                        departureTimes2.add(50.0);
                        departureTimes2.add(70.0);
                        departureTimes2.add(90.0);
                        departureTimes2.add(110.0);
                        departureTimes2.add(130.0);
                        departureTimes2.add(150.0);
                        departureTimes2.add(170.0);

                        Train train2 = new Train("T2", departureTimes2, trainTravelTime);

                        // Put both trains into a list
                        List<Train> trains = new ArrayList<>();
                        trains.add(train1);
                        trains.add(train2);
                        // build the sim with multiple trains

                        double simTime = 180.0;

                        Simulation sim = new Simulation(
                                        simTime,
                                        streams,
                                        trains, // <-- list of trains now
                                        routes);

                        // Run Simulation
                        Metrics m = sim.run();

                        System.out.println("\n=== Simulation Results ===");
                        System.out.printf("Average Wait: %.2f\n", m.getAvgWait());
                        System.out.printf("Average Passage: %.2f\n", m.getAvgPassage());
                        System.out.printf("Throughput: %.4f commuters/min\n", m.getThroughput());
                        System.out.println("Completed Trips: " + m.getCompletedCount());

                } catch (Exception ex) {
                        System.out.println("Simulation failed: " + ex.getMessage());
                        ex.printStackTrace();
                }
        }
}
