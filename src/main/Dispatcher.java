package main;

public class Dispatcher {
    Stack<RoboTaxi> taxiFleet = new Stack<RoboTaxi>();
    //  data structure for taxis actively in service - held by route? all together? - linked list?
    ListRecord onRoute1;
    ListRecord onRoute2;
    ListRecord onRoute3;
    ListRecord onRoute4;
    ListRecord onRoute5;
    ListRecord onRoute6;
    ListRecord onRoute7;

    int taxiCount;
    int taxiSeats;

    public Dispatcher( int numTaxi, int numTaxiSeats ) {
        taxiCount = numTaxi;
        taxiSeats = numTaxiSeats;
        for( int i = 0; i < numTaxi; i++ ) {
            taxiFleet.push( new RoboTaxi( ( numTaxi - i ),  numTaxiSeats ) );
        }
        //create given number of RoboTaxi objects
    }

    public int checkRoute ( int locID ) {
        if ( locID < 100 ) {
            return '1';
        } else if ( locID < 200 ) {
            return '2';
        } else if( locID < 300 ) {
            return '3';
        } else if( locID < 400 ) {
            return '4';
        } else if( locID < 500 ) {
            return '5';
        } else if( locID < 600 ) {
            return '6';
        }
        else return '0';
    }

    public ListRecord getRoute( int locID ) {
        if ( locID < 100 ) {
            return onRoute1;
        } else if ( locID < 200 ) {
            return onRoute2;
        } else if( locID < 300 ) {
            return onRoute3;
        } else if( locID < 400 ) {
            return onRoute4;
        } else if( locID < 500 ) {
            return onRoute5;
        } else if( locID < 600 ) {
            return onRoute6;
        } else if( locID < 700 ) {
            return onRoute7;
        } else return null;
    }

//    public boolean routeEmpty( int routeNum ) {
//        if( routeNum == 1 ) {
//            return onRoute1 == null;
//        } else if ( routeNum == 2 ) {
//            return onRoute2 == null;
//        } else if ( routeNum == 3 ) {
//            return onRoute3 == null;
//        } else if ( routeNum == 4 ) {
//            return onRoute4 == null;
//        } else if ( routeNum == 5 ) {
//            return onRoute5 == null;
//        } else if ( routeNum == 6 ) {
//            return onRoute6 == null;
//        } else if ( routeNum == 7 ) {
//            return onRoute7 == null;
//        } else {
//            return true;
//        }
//    }

    public RoboTaxi findRideShare( ListRecord taxisOnRoute, int locID ) {
        ListRecord index = taxisOnRoute;
        while ( index != null && index.data != null ) {
            if ( index.data.getLocation() > locID && index.data.getOpenSeats() > 0 ) {
                return index.data;
            }
            index = index.next;
        }
        return null;
    }

    public void callTaxi ( int locID, Commuter commuter ) {
        int route = checkRoute( locID );

        if( route == null ) {
            System.out.println("Invalid locID, no route found");
        } else {
            ListRecord taxisOnRoute = getRoute( locID );
            if( ! routeEmpty( route ) ) {
                RoboTaxi rideShare = findRideShare( taxisOnRoute , locID );
                if( rideShare != null ) {
                    rideShare.pickupCommuter( locID, commuter);
                } else {
                    if( taxiFleet.hasNext() ) {
                        RoboTaxi newDispatch
                    }
                }
            }
        }
        // x check robotaxi fleet for robotaxi on same route;
        // x if robotaxi location is > locID && not full
        // x assign commuter to taxi
        // else if taxi stack not empty, pop taxi from stack and startRoute( route, locID )
        // assign commuter to taxi
        // else (temp)
    }

    public void returnToOrigin( RoboTaxi taxi ) {
        int numPassengers = taxiSeats - taxi.getOpenSeats();
        taxiFleet.push( taxi );
    }

    public class ListRecord {
        public ListRecord next;
        public RoboTaxi data;

        public ListRecord( RoboTaxi taxi ) {
            data = taxi;
            next = null;
        }
    }
}
