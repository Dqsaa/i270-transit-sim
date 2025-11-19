package main;
import java.util.LinkedList;

public class Route {
    Location[] locations; // in order from station, 0 is closest. Some array positions may be null.
    int locationCount;
    List<RoboTaxi> taxis;  // no order assumed

    public int getLocationCount() {
        return locationCount;
    }

    public int getTaxiCount() {
        return taxis.getLength();
    }

    public Route( int maxLocations ) {
        locationCount = 0;
        locations = new Location[ maxLocations ];
        taxis = new List<RoboTaxi>();
    };

    public boolean appendToRoute( Location loc ) {
        if (locationCount == locations.length) { return false; }

        locations[locationCount++] = loc;
        return true;
    }

    public void addTaxi( RoboTaxi tx, int locationID ) {
        tx.setCurrentLocation( locationID );
        tx.setNextLocation( getNextLocationID( locationID ) );
        taxis.addToHead( tx );
    }

    public RoboTaxi removeTaxi( RoboTaxi tx ) {
        taxis.remove( tx );
        tx.setCurrentLocation(0);
        tx.setNextLocation(0);
        return tx;
    }

    public void update( double currentTime ) {

        taxis.initIteration();
        RoboTaxi  taxi;
        while ( (taxi = taxis.next()) != null ) {
            if (currentTime > taxi.getNextLocationTime()) {
                //  update the location and next location -- next segment
                int currentLocId = taxi.getLocation();
                taxi.setCurrentLocation( taxi.getNextLocation() );
                taxi.setNextLocation( getNextLocationID(currentLocId) );
                // TODO: update the time
            }
        }
    }

    // Returns first taxi that has space that has this location ID in its remaining list
    // of pickup locations, on the way back to station.   Taxis pick up only on return.
    public RoboTaxi findTaxiFor( int locationID ) {
        if (locationCount == 0) return null;

        RoboTaxi currentClosest = null;
        double   currentShortestTime = 0.0; // distance FROM taxi to pickup point. We want the closest that has any open spots.

        taxis.initIteration();
        RoboTaxi currentTaxi = null;
        while( (currentTaxi = taxis.next()) != null ) {
            if (currentTaxi.getOpenSeats() > 0) {
                // TODO: probably should use next location, when available
                double tmBetween = timeBetween(currentTaxi.getLocation(), locationID);
                if (tmBetween >= 0.0) {
                    if ((currentClosest == null) ||
                            (currentClosest != null && tmBetween < currentShortestTime)) {
                        currentClosest = currentTaxi;
                        currentShortestTime = tmBetween;
                    }
                }
            }
        }
        return currentClosest;
    }

    // if dst lies between src and station, returns total time from location src to location dst.
    // Otherwise, returns -1.  If src and dst are the same, return 0.
    private double timeBetween( int srcId, int dstId ) {
        int srcIndex = getLocationIndex( srcId );
        int dstIndex = getLocationIndex( dstId );
        // as locations are stored consecutively starting from station,
        // the time between exists only if srcIndex >= dstIndex
        if (srcIndex == -1 || dstIndex == -1 || srcIndex < dstIndex) { return -1; }

        double time = 0.0;
        for (int index = srcIndex; index >= dstIndex; index--) {
            time += locations[index].getTimeToNext();
        }

        return time;
    }

    // Returns index of this location in our array, -1 if not found.
    public int getLocationIndex( int locId ) {
        for (int index = 0; index < locationCount; index++) {
            if (locations[index].getID() == locId) {
                return index;
            }
        }
        return -1;
    }

    public int getNextLocationID( int locId ) {
        int index = getLocationIndex( locId );
        if (index == 0) {
            return 0;  // station is next
        } else {
            return locations[index-1].getID();
        }
    }
}
