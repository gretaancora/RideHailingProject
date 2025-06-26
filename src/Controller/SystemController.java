package Controller;

import Libs.Rngs;
import Model.*;
import Utils.Distribution;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static Model.MsqEvent.*;
import static Model.MsqEvent.EventType.*;
import static Utils.Constants.*;

public class SystemController {
    long number = 0;                     /* number of jobs in the node         */
    int e;                               /* next event index                   */
    int s;                               /* server index                       */
    long index = 0;                      /* used to count processed jobs       */
    double area = 0.0;           /* time integrated number in the node */

    double service;
    private String systemType;

    private final MsqT msqT = MsqT.getInstance();

    private final EventListManager eventListManager;
    private final Distribution distr;

    private final List<MsqEvent> systemList = new ArrayList<>(NODES); //lista globale pari al numero di nodi nel sistema
    private final List<MsqSum> sumList = new ArrayList<>(NODES + 1); //una in più per le statistiche globali

    public static final List<Center> centerList = new ArrayList<>(); //contiene le istanze di tutti i nodi simulati

    public SystemController(Rngs rngs) {
        // Refresh
        centerList.clear();
        eventListManager = EventListManager.getInstance();
        distr = Distribution.getInstance(rngs);
        eventListManager.resetState();

        // Read system type from config.properties

        Properties config = new Properties();
        systemType = "simple"; // default

        try (InputStream input = new FileInputStream("src/resources/config.properties")) {
            config.load(input);
            systemType = config.getProperty("SYSTEM_TYPE");
        } catch (IOException e) {
            System.err.println("Unable to read config.properties. Using default 'simple' system.");
        }

        // Initialize centers based on systemType
        switch (systemType) {
            case "simple":
                addSimpleCenters();
                break;
            case "ridesharing":
                addSimpleCenters();
                addRideSharingCenter();
                break;
            default:
                throw new IllegalArgumentException("Unsupported system type: " + systemType);
        }

        // Set msqT for each center
        for (Center center : centerList) {
            center.setMsqT(this.msqT);
        }
        // Initialize systemList with corresponding events
        initializeSystemEvents();
        eventListManager.setSystemEventsList(systemList);
    }


    //solo tradizionale
    private void addSimpleCenters() {
        centerList.add(new SimpleCenter(SMALL_SERVER, VehicleType.SMALL));
        centerList.add(new SimpleCenter(MEDIUM_SERVER, VehicleType.MEDIUM));
        centerList.add(new SimpleCenter(LARGE_SERVER, VehicleType.LARGE));
    }

    //solo ridesharing
    private void addRideSharingCenter() {
        centerList.add(new RideSharingCenter(SMALL_SERVER_RIDESHARING + MEDIUM_SERVER + LARGE_SERVER, VehicleType.RIDESHARING));
    }

    private void initializeSystemEvents() {
        systemList.clear();

        if (systemType.equals("simple")) {
            // Solo 3 tipi: SMALL, MEDIUM, LARGE
            for (int i = 0; i < 3; i++) {
                systemList.add(new MsqEvent(0, ARRIVAL, MsqEvent.VehicleType.fromInt(i), true));
            }

            initializeEventIfPresent(MsqEvent.VehicleType.SMALL, eventListManager.getEventInServiceListSmall(), 0);
            initializeEventIfPresent(MsqEvent.VehicleType.MEDIUM, eventListManager.getEventInServiceListMedium(), 1);
            initializeEventIfPresent(MsqEvent.VehicleType.LARGE, eventListManager.getEventInServiceListLarge(), 2);

        } else if (systemType.equals("ridesharing")) {
            // 4 tipi, incluso RIDESHARING
            for (int i = 0; i < 4; i++) {
                systemList.add(new MsqEvent(0, ARRIVAL, MsqEvent.VehicleType.fromInt(i), true));
            }

            initializeEventIfPresent(MsqEvent.VehicleType.SMALL, eventListManager.getEventInServiceListSmall(), 0);
            initializeEventIfPresent(MsqEvent.VehicleType.MEDIUM, eventListManager.getEventInServiceListMedium(), 1);
            initializeEventIfPresent(MsqEvent.VehicleType.LARGE, eventListManager.getEventInServiceListLarge(), 2);
            initializeEventIfPresent(MsqEvent.VehicleType.RIDESHARING, eventListManager.getEventInServiceListRideSharing(), 3);

        } else {
            throw new IllegalArgumentException("Unsupported system type: " + systemType);
        }
    }

    private void initializeEventIfPresent(MsqEvent.VehicleType type, List<MsqEvent> eventList, int index) {
        if (!eventList.isEmpty()) {
            int nextEventIdx = MsqEvent.getNextEvent(eventList);
            MsqEvent nextEvent = eventList.get(nextEventIdx);
            systemList.set(index, new MsqEvent(nextEvent.getTime(), ARRIVAL, type, true));
        }
    }

    public void simulation(int simulationType, long seed, int runNumber) throws Exception {
        System.out.println("Starting simulation - seed: " + seed);

        switch (simulationType) {
            case 0:
                simpleSimulation(seed, runNumber);
                break;
            case 1:
                infiniteSimulation(seed);
                break;
            default:
                throw new IllegalArgumentException("Invalid simulation choice");
        }
    }

    /* Finite horizon simulation */
    public void simpleSimulation(long seed, int runNumber) throws Exception {
        int e;
        List<MsqEvent> eventList = eventListManager.getSystemEventsList();
        //la lista di eventi globali

        while (msqT.getCurrent() < STOP_FIN) {
            if ((e = getNextEvent(eventList)) == -1) break;

            System.out.println("Before update: msqT=" + msqT + " eventList=" + eventList);
            System.out.println("Next event index: " + e + " Time: " + eventList.get(e).getTime());

            msqT.setNext(eventList.get(e).getTime());
            this.area = this.area + (msqT.getNext() - msqT.getCurrent()) * number;
            msqT.setCurrent(msqT.getNext());

            if (e < 4) {
                centerList.get(e).setSeed(seed);
                centerList.get(e).finiteSimulation(e);
                //chiamo quella di ogni centro

                System.out.println("After finiteSimulation: eventList=" + eventListManager.getSystemEventsList());

                centerList.get(e).printIteration(true, seed, e, runNumber, msqT.getCurrent());

                eventList = eventListManager.getSystemEventsList();
            } else throw new Exception("Invalid event");
        }

        System.out.println("\n\n");

        for (int i = 0; i < NODES; i++) centerList.get(i).printResult(runNumber, seed);
    }


    /* Infinite horizon simulation */
    public void infiniteSimulation(long seed) throws Exception {
        int e;

        msqT.setCurrent(START);
        msqT.setNext(START);

        List<MsqEvent> eventList = eventListManager.getSystemEventsList();

        while (pendingEvents()) {
            if ((e = getNextEvent(eventList)) == -1) break;

            msqT.setNext(eventList.get(e).getTime());
            this.area = this.area + (msqT.getNext() - msqT.getCurrent()) * number;
            msqT.setCurrent(msqT.getNext());

            if (e < 4) {
                centerList.get(e).setSeed(seed);

                centerList.get(e).infiniteSimulation();
                eventList = eventListManager.getSystemEventsList();
            } else throw new Exception("Invalid event");
        }

        for (int i = 0; i < NODES; i++) centerList.get(i).printFinalStatsStazionario();
    }

    /* Fetch index of most imminent event among all servers */
    private int getNextEvent(List<MsqEvent> eventList) {
        double threshold = Double.MAX_VALUE;
        int e = -1;
        int i = 0;

        for (MsqEvent event : eventList) {
            if (event.getTime() < threshold && event.getX()) {
                threshold = event.getTime();
                e = i;
            }
            i++;
        }
        return e;
    }

    /* Check if there is a centre that has not processed B*K events */
    private boolean pendingEvents() {
        for (Center center : centerList) {
            if (center.getJobInBatch() <= B * K) {
                return true;
            }
        }
        return false;
    }
}