package main;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouteBuilderTest {

    @Test
    void readLocations() {
        try {
            RouteBuilder rb = new RouteBuilder();
            rb.readLocations( "test/locations.test.txt");
            assertTrue( rb.getLocationCount() == 6 );

        } catch( RouteBuilder.LocationIdException ex ) {
            assertTrue( false );
        }
    }

    @Test
    void readRoutes() {
        try {
            RouteBuilder rb = new RouteBuilder();
            rb.readLocations( "test/locations.test.txt");
            rb.readRoutes( "test/routes.test.txt");
            assertTrue( rb.getRouteCount() == 2 );

        } catch( RouteBuilder.LocationIdException ex ) {
            assertTrue( false );
        }
    }

    @Test
    void build() {
        try {
            RouteBuilder rb = new RouteBuilder();
            rb.readLocations( "test/locations.test.txt");
            rb.readRoutes( "test/routes.test.txt");
            Route[] routes = rb.build();

            assertTrue( routes.length == 2 );
            assertTrue( routes[0].getLocationCount() == 3 );
            assertTrue( routes[1].getLocationCount() == 3 );
            assertTrue( routes[0].getLocationIndex(10) == 0 );
            assertTrue( routes[1].getLocationIndex(100) == 0 );

        } catch( RouteBuilder.LocationIdException ex ) {
            assertTrue( false );
        }
    }
}