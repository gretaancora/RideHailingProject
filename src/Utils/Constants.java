package Utils;

public class Constants {
    /* Number of servers in each center in ride-sharing */
    public static final int SMALL_SERVER = 60;
    public static final int MEDIUM_SERVER = 15;
    public static final int LARGE_SERVER = 15;

    /* Number of servers in ride-sharing */
    public static final int SMALL_SERVER_RIDESHARING = 0;
    public static final int MEDIUM_SERVER_RIDESHARING = 0;
    public static final int LARGE_SERVER_RIDESHARING= 0;

    /* Total number of servers in simple case */
    public static final int SMALL_SERVER_TOT = SMALL_SERVER + SMALL_SERVER_RIDESHARING;
    public static final int MEDIUM_SERVER_TOT = MEDIUM_SERVER + MEDIUM_SERVER_RIDESHARING;
    public static final int LARGE_SERVER_TOT = LARGE_SERVER + LARGE_SERVER_RIDESHARING;


    /* Value of Start and Stop time */
    public static final double START = 0.0;
    public static final double STOP_INF = Double.MAX_VALUE; /* Infinite simulation */
    public static final double STOP_FIN = 10; /* Finite simulation -> check every 1 day */

    /* Probabilities */
    public static final double P_LOSS = 0.1;
    public static final double P_SMALL = 0.2;
    public static final double P_MEDIUM = 0.2;
    public static final double P_LARGE = 0.2;
    public static final double P_RIDE_SHARING = 0.2;


    /* TRADITIONAL_SERVICE */

    public static final double TRAD_STD_DEV = 0.4;
    public static final double TRAD_MIN = 0.1;
    public static final double TRAD_MEAN = 0.1;
    public static final double TRAD_MAX = 0.1;
    public static final double DELAY = 0.1;



    /* Arrival rate in rental station (users/sec) */
    public static final double LAMBDA = 12 / 60.0 / 60.0;

    /* Service rate in rental station (jobs/sec) */
    public static final double MU_RENTAL = LAMBDA;

    /* Service rate (jobs/sec), 37,5 parked cars in one hour */
    public static final double MU_PARKING = 37.5 / 60.0 / 60.0;

    /* Charging rate (jobs/sec), one battery is fully charged in 45 minutes */
    public static final double MU_CHARGING = 1.33 / 60.0 / 60.0;

    /* Service rate (job/sec), is considered to rent car and drive it for 30 min */
    public static final double MU_STRADA = 2 / 60.0 / 60.0;

    /* Exogenous rate */
    public static final double LAMBDA_EXOGENOUS = 3 / 60.0 / 60.0;

    /* rental station (time to process renting service) */
    public static final double RENTAL_SERVICE = 1.0 / MU_RENTAL;

    /* parking station (time to park a car) */
    public static final double PARKING_SERVICE = 1.0 / MU_PARKING;   // mu = 0.625

    /* charging station (time to recharge car in charging station) */
    public static final double CHARGING_SERVICE = 1.0 / MU_CHARGING; // Charging time is 45 minutes on average.

    /* Service rate of route */
    public static final double ROUTE_SERVICE = 1.0 / MU_STRADA;    // Rental time is 30 min

    /* Nodes in system */
    public static final int NODES = 4;

    /* Seed to use for the simulation */
    public static final long SEED = 123456789L;

    /* Parked car in Noleggio */
    public static final int INIT_PARK_CARS = 15;
//    public static final int INIT_PARK_CARS = 0;   // Used to simulate distribution in Python on Strada

    /* Cost constants */
    public static final int CAR_COST = 5; /* Yen/hour for each car */
    public static final int PARKING_COST = 8; /* Yen/hour for each parking */
    public static final int LOSS_COST = 30; /* Yen for each loss */

    /* Profit constants */ /* TODO Variable or constant profit */
    public static final int RENTAL_TIME_PROFIT = 118; /* (100 + 0.3 * 60) Yen/hour when a car is on the road */
    public static final double RENTAL_KM_PROFIT = 0.99; /* Yen/km when a car is on the road: Note we assume to know the mean speed */
    public static final int MEAN_SPEED = 50; /* km/h */
    public static final int RENTAL_PROFIT = 118; /* Yen/hour */
    public static final int RECHARGE_COST = 20; /* Yen for each recharge */

    /* Batch simulation */
    public static final int K = 128;
    public static final int B = 1512 ;
    public static final double ALPHA = 0.05; // Level of confidence = 0.95

    public static final int REPLICATION = 64;

    /* Center constants for file generation */
    public static final String PARCHEGGIO = "Parcheggio";
    public static final String STRADA = "Strada";
    public static final String NOLEGGIO = "Noleggio";
    public static final String RICARICA = "Ricarica";

    /* Simulation constants */
    public static final double INFINITE_INCREMENT = 0.01;
}
