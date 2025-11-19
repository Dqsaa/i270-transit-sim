package main;

public class Dispatcher {
    Stack<RoboTaxi> taxiFleet = new Stack<RoboTaxi>();
    //  data structure for taxis actively in service - held by route? all together? - linked list?
    Route[] routes = new Route[7];


//    ListRecord onRoute1;
//    ListRecord onRoute2;
//    ListRecord onRoute3;
//    ListRecord onRoute4;
//    ListRecord onRoute5;
//    ListRecord onRoute6;
//    ListRecord onRoute7;

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

//    public ListRecord getRoute( int locID ) {
//        if ( locID < 100 ) {
//            return route[0];
//        } else if ( locID < 200 ) {
//            return onRoute2;
//        } else if( locID < 300 ) {
//            return onRoute3;
//        } else if( locID < 400 ) {
//            return onRoute4;
//        } else if( locID < 500 ) {
//            return onRoute5;
//        } else if( locID < 600 ) {
//            return onRoute6;
//        } else if( locID < 700 ) {
//            return onRoute7;
//        } else return null;
//    }

    public Route getRoute( int locID ) {
        if ( locID < 100 ) {
            return route[0];
        } else if ( locID < 200 ) {
            return route[1];
        } else if( locID < 300 ) {
            return route[2];
        } else if( locID < 400 ) {
            return route[3];
        } else if( locID < 500 ) {
            return route[4];
        } else if( locID < 600 ) {
            return route[5];
        } else if( locID < 700 ) {
            return route[6];
        } else return null;
    }

//    public RoboTaxi findRideShare( ListRecord taxisOnRoute, int locID ) {
//        ListRecord index = taxisOnRoute;
//        while ( index != null && index.data != null ) {
//            if ( index.data.getLocation() > locID && index.data.getOpenSeats() > 0 ) {
//                return index.data;
//            }
//            index = index.next;
//        }
//        return null;
//    }

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

        // x assign commuter to taxi
        // else if taxi stack not empty, pop taxi from stack and startRoute( route, locID )
        // assign commuter to taxi
        // else (temp)
    }
    public void callTaxi ( int locID, Commuter commuter ) {
        int route = checkRoute( locID );

        if (route == 0) {
            System.out.println(" Invalid locID ");
        } else {
            Route currentRoute = getRoute(locID);
            RoboTaxi rideShare = Route.findTaxiFor(locID);
            if (rideShare != null) {
                rideShare.pickupCommuter(locID, commuter);
            } else {
                if ( taxiFleet.hasNext() ) {
                    currentRoute.addTaxi( rideShare );
                    rideShare.pickupCommuter( locID, commuter );
                    //RoboTaxi - if nextLocation == null, nextLocation = locID, double arrivedAt, double nextArrivalTime
                }
            }
        }
    }

    public void returnToOrigin( Route rt, RoboTaxi taxi ) {
        int numPassengers = taxiSeats - taxi.getOpenSeats();
        rt.removeTaxi( taxi );
        //move passengers to train queue from taxi array
        taxiFleet.push( taxi );
    }

//    public class ListRecord {
//        public ListRecord next;
//        public RoboTaxi data;
//
//        public ListRecord( RoboTaxi taxi ) {
//            data = taxi;
//            next = null;
//        }
//    }
}
