package main;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    void appendToRoute() {
        Route route = new Route(10);
        Location loc1 = new Location( 10, 5.0, 10.0 );
        Location loc2 = new Location( 20, 7.5, 5.0 );
        route.appendToRoute( loc1 );
        route.appendToRoute( loc2 );

        assertEquals( route.getLocationIndex(10), 0 );
        assertEquals( route.getLocationIndex(20), 1 );
        assertEquals( route.getLocationCount(), 2 );
    }

    @Test
    void addTaxi() {
        Route route = new Route(10);
        Location loc1 = new Location( 10, 5.0, 10.0 );
        Location loc2 = new Location( 20, 7.5, 5.0 );
        route.appendToRoute( loc1 );
        route.appendToRoute( loc2 );

        RoboTaxi taxi1 = new RoboTaxi(1000, 6);
        RoboTaxi taxi2 = new RoboTaxi(1001, 6);
        route.addTaxi( taxi1, 10 );
        route.addTaxi( taxi2, 20 );
        assertEquals( route.getTaxiCount(), 2 );
    }

    @Test
    void removeTaxi() {
        Route route = new Route(10);
        Location loc1 = new Location( 10, 5.0, 10.0 );
        Location loc2 = new Location( 20, 7.5, 5.0 );
        route.appendToRoute( loc1 );
        route.appendToRoute( loc2 );

        RoboTaxi taxi1 = new RoboTaxi(1000, 6);
        RoboTaxi taxi2 = new RoboTaxi(1001, 6);
        route.addTaxi( taxi1, 10 );
        route.addTaxi( taxi2, 20 );
        route.removeTaxi( taxi1 );
        assertEquals( route.getTaxiCount(), 1 );

    }

    @Test
    void findTaxiFor1() {
        Route route = new Route(10);
        Location loc1 = new Location( 10, 5.0, 10.0 );
        Location loc2 = new Location( 20, 7.5, 5.0 );
        route.appendToRoute( loc1 );
        route.appendToRoute( loc2 );

        RoboTaxi taxi1 = new RoboTaxi(1000, 6);
        RoboTaxi taxi2 = new RoboTaxi(1001, 6);
        route.addTaxi( taxi1, 10 );
        route.addTaxi( taxi2, 20 );

        // taxi1 is closest to 10
        RoboTaxi tx = route.findTaxiFor( 10 );
        assertTrue( tx.equals( taxi1 ) );
    }


    @Test
    void findTaxiFor2() {
        Route route = new Route(10);
        Location loc1 = new Location( 10, 5.0, 10.0 );
        Location loc2 = new Location( 20, 7.5, 5.0 );
        route.appendToRoute( loc1 );
        route.appendToRoute( loc2 );

        RoboTaxi taxi1 = new RoboTaxi(1000, 6);
        RoboTaxi taxi2 = new RoboTaxi(1001, 6);
        route.addTaxi( taxi1, 10 );
        route.addTaxi( taxi2, 20 );

        // taxi2 is closest to 20
        RoboTaxi tx = route.findTaxiFor( 20 );
        assertTrue( tx.equals( taxi2 ) );
    }


    @Test
    void findTaxiFor3() {
        Route route = new Route(10);
        Location loc1 = new Location( 10, 5.0, 10.0 );
        Location loc2 = new Location( 20, 7.5, 5.0 );
        Location loc3 = new Location( 30, 10.0, 5.0 );

        route.appendToRoute( loc1 );
        route.appendToRoute( loc2 );
        route.appendToRoute( loc3 );

        RoboTaxi taxi1 = new RoboTaxi(1000, 6);
        RoboTaxi taxi2 = new RoboTaxi(1001, 6);
        route.addTaxi( taxi1, 10 );
        route.addTaxi( taxi2, 20 );

        // no taxi available for 30
        RoboTaxi tx = route.findTaxiFor( 30 );
        assertTrue( tx == null );
    }
    
    @Test
    void findTaxiFor4() {
        Route route = new Route(10);
        Location loc1 = new Location( 10, 5.0, 10.0 );
        Location loc2 = new Location( 20, 7.5, 5.0 );
        Location loc3 = new Location( 30, 10.0, 5.0 );

        route.appendToRoute( loc1 );
        route.appendToRoute( loc2 );
        route.appendToRoute( loc3 );

        RoboTaxi taxi1 = new RoboTaxi(1000, 6);
        RoboTaxi taxi2 = new RoboTaxi(1001, 6);
        route.addTaxi( taxi1, 10 );
        route.addTaxi( taxi2, 20 );

        // no taxi available for a location we don't have
        RoboTaxi tx = route.findTaxiFor( 50 );
        assertTrue( tx == null );
    }
}