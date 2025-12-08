package main;

import java.io.*;
import java.util.Scanner;

// The Route Builder reads a set of data files and builds an array of Routes.
// Usage:
//    RouteBuilder rb = new RouteBuilder();
//    rb.readLocations( location_filename );
//    rb.readRoutes( route_filename );
//    try {
//      Routes[] routes = rb.build();
//    catch( RouteBuilder.LocationIdException ex ) {
//      // an illegal location id existed in a route
//    }
public class RouteBuilder {
    private List<Location> locations;
    private List<RouteDefinition> routeDefinitions;

    public RouteBuilder() {
        locations = new List<Location>();
        routeDefinitions = new List<RouteDefinition>();
    }

    public int getLocationCount() {
        return locations.getLength();
    }

    public int getRouteCount() {
        return routeDefinitions.getLength();
    }

    public boolean readLocations(String filename) throws LocationIdException {
        File file = new File(filename);
        System.out.println("Looking for: " + file.getAbsolutePath());
        try {
            Scanner sc = new Scanner(file);
            int lineCount = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                    String[] tokens = line.split(",");
                    // location has id, time from station, time to next
                    if (tokens.length == 3) {
                        try {
                            int locId = Integer.parseInt(tokens[0]);
                            double tfs = Double.parseDouble(tokens[1]);
                            double tts = Double.parseDouble(tokens[2]);
                            Location loc = new Location(locId, tfs, tts);
                            locations.addToHead(loc);
                            lineCount++;
                        } catch (NumberFormatException ex) {
                            throw new LocationIdException(lineCount);
                        }
                    } else {
                        throw new LocationIdException(lineCount);
                    }
                }
            }
            return true;
        } catch (java.io.FileNotFoundException ex) {
            return false;
        }
    }

    private class RouteDefinition {
        public List<Integer> routeIds;

        public RouteDefinition() {
            routeIds = new List<Integer>();
        }
    }

    public boolean readRoutes(String filename) throws LocationIdException {
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);
            int routeCount = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                    String[] tokens = line.split(",");
                    RouteDefinition rd = new RouteDefinition();

                    try {
                        for (int i = 0; i < tokens.length; i++) {
                            rd.routeIds.append(Integer.valueOf(tokens[i]));
                        }
                        routeDefinitions.append(rd);
                        routeCount++;

                    } catch (NumberFormatException ex) {
                        return false;
                    }
                }
            }
        } catch (java.io.FileNotFoundException ex) {
            return false;
        }
        return true;
    }

    public class LocationIdException extends Exception {

        private int route, locationId;

        public LocationIdException(int rtIndex, int locId) {
            route = rtIndex;
            locationId = locId;
        }

        public LocationIdException(int line) {
            route = -1;
            locationId = line;
        }

        public String toString() {
            if (route >= 0) {
                return String.format("Illegal location ID: route %d locationID %d", route, locationId);
            } else {
                return String.format("Illegal location data: line %d", locationId);
            }
        }
    }

    public Route[] build() throws LocationIdException {
        Route[] routes = new Route[routeDefinitions.getLength()];
        int routeIndex = 0;

        // for each route definition, gather the associated locations in order
        routeDefinitions.initIteration();
        RouteDefinition rd;
        while ((rd = routeDefinitions.next()) != null) {
            Route route = new Route(rd.routeIds.getLength());

            rd.routeIds.initIteration();
            Integer locId;

            while ((locId = rd.routeIds.next()) != null) {
                int id = locId.intValue();
                if (id == 0) {
                    continue;
                }

                Location loc = findLocation(locId.intValue());
                if (loc == null) {
                    throw new LocationIdException(routeIndex, locId);
                }
                route.appendToRoute(loc);
            }
            routes[routeIndex++] = route;
        }
        return routes;
    }

    private Location findLocation(int id) {
        locations.initIteration();
        Location loc;
        while ((loc = locations.next()) != null) {
            if (loc.getID() == id) {
                return loc;
            }
        }
        return null;
    }
}
