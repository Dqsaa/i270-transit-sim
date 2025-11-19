package main;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    @Test
    void addToHead() {
        List<RoboTaxi> list = new List<RoboTaxi>();
        RoboTaxi taxi = new RoboTaxi( 1, 6 );
        list.addToHead( taxi );
        assertTrue( list.getLength() == 1 );
    }

    @Test
    void remove() {
        List<RoboTaxi> list = new List<RoboTaxi>();
        RoboTaxi taxi1 = new RoboTaxi( 1, 6 );
        list.addToHead( taxi1 );

        RoboTaxi taxi2 = new RoboTaxi( 2, 6 );
        list.addToHead( taxi2 );
        assertTrue( list.getLength() == 2 );

        list.remove( taxi1 );
        assertTrue( list.getLength() == 1 );

        list.initIteration();
        RoboTaxi tx = list.next();
        assertTrue( tx.equals( taxi2 ) );
    }

    @Test
    void remove2() {
        List<RoboTaxi> list = new List<RoboTaxi>();
        RoboTaxi taxi1 = new RoboTaxi( 1, 6 );
        list.addToHead( taxi1 );
        list.remove( taxi1 );
        assertTrue( list.getLength() == 0 );

        RoboTaxi taxi2 = new RoboTaxi( 2, 6 );
        list.addToHead( taxi2 );
        assertTrue( list.getLength() == 1 );

        list.initIteration();
        RoboTaxi tx = list.next();
        assertTrue( tx.equals( taxi2 ) );
    }

}