package main;

public class Dispatcher {
    Stack<RoboTaxi> taxiFleet = new Stack<RoboTaxi>();
    Route[] routes;
    int taxiCount;
    final int taxiSeats;

    public Dispatcher( int numTaxi, int numTaxiSeats, Route[] rts) {
        taxiCount = numTaxi;
        taxiSeats = numTaxiSeats;
        routes = rts;

        for( int i = 0; i < numTaxi; i++ ) {
            taxiFleet.push( new RoboTaxi( ( numTaxi - i ),  numTaxiSeats ) );
        }
    }

    public Route getRoute( int locID ) {
        if ( locID < 100 ) {
            return routes[0];
        } else if ( locID < 200 ) {
            return routes[1];
        } else if( locID < 300 ) {
            return routes[2];
        } else if( locID < 400 ) {
            return routes[3];
        } else if( locID < 500 ) {
            return routes[4];
        } else if( locID < 600 ) {
            return routes[5];
        } else if( locID < 700 ) {
            return routes[6];
        } else return null;
    }

    public void callTaxi ( int locID, Commuter commuter ) {
        Route currentRoute = getRoute(locID);

        if ( currentRoute == null ) {
            System.out.println(" Invalid locID ");
        } else {
            RoboTaxi rideShare = Route.findTaxiFor(locID);
            if (rideShare != null) {
                rideShare.pickupCommuter(locID, commuter);
            } else {
                if ( taxiFleet.hasNext() ) {
                    rideShare = taxiFleet.pop();
                    currentRoute.addTaxi( rideShare, 0 );
                    rideShare.pickupCommuter(locID, commuter);
                }
            }
        }
    }

    public void returnToFleet( RoboTaxi taxi ) {
        taxiFleet.push( taxi );
    }

}
