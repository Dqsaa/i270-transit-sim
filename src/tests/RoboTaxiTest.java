package tests;
import main.*;

public class RoboTaxiTest {
    @Test
    public void pickupCommuter() {
        Commuter com1 = new Commuter();
        Commuter com2 = new Commuter();
        Commuter com3 = new Commuter();
        Commuter com4 = new Commuter();
        Commuter com5 = new Commuter();
        Commuter com6 = new Commuter();
        Commuter com7 = new Commuter();

        RoboTaxi taxi = new RoboTaxi( 1, 6 );

        taxi.pickupCommuter( 104, com1 );
        taxi.pickupCommuter( 103, com2 );
        taxi.pickupCommuter( 102, com3 );
        taxi.pickupCommuter( 101, com4 );
        taxi.pickupCommuter( 101, com5 );
        taxi.pickupCommuter( 101, com6 );
        taxi.pickupCommuter( 101, com7 );
    }

}
