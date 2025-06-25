package Controller;

import Libs.Rngs;
import Model.*;
import Utils.Distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private final MsqT msqT = new MsqT();

    private final EventListManager eventListManager;
    private Distribution distr;

    private final List<MsqEvent> systemList = new ArrayList<>(NODES); //lista globale pari al numero di nodi nel sistema
    private final List<MsqSum> sumList = new ArrayList<>(NODES + 1); //una in più per le statistiche globali

    public static final List<Center> centerList = new ArrayList<>(); //contiene le istanze di tutti i nodi simulati

    public SystemController(Rngs rngs) {
        centerList.clear();

        eventListManager = EventListManager.getInstance();
        distr = Distribution.getInstance(rngs);

        eventListManager.resetState();

        SimpleCenter smallCenter = new SimpleCenter(SMALL_SERVER, VehicleType.SMALL);
        SimpleCenter mediumCenter = new SimpleCenter(MEDIUM_SERVER, VehicleType.MEDIUM);
        SimpleCenter largeCenter = new SimpleCenter(LARGE_SERVER, VehicleType.LARGE);
        RideSharingCenter rideSharingCenter = new RideSharingCenter(SMALL_SERVER_RIDESHARING+MEDIUM_SERVER+LARGE_SERVER, VehicleType.RIDESHARING);

        centerList.addAll(Arrays.asList(smallCenter, mediumCenter, largeCenter, rideSharingCenter));

        for (int i = 0; i < 4; i++) {
            systemList.add(i, new MsqEvent(0, ARRIVAL, MsqEvent.VehicleType.fromInt(i), true));
        }

        //per ogni centro prendiamo l'evento con il time out inferiore

        // Initialize small in system list
        List<MsqEvent> eventInServiceListSmall = eventListManager.getEventInServiceListSmall();
        int nextEventSmall = MsqEvent.getNextEvent(eventInServiceListSmall);
        systemList.set(0, new MsqEvent(eventInServiceListSmall.get(nextEventSmall).getTime(),ARRIVAL, MsqEvent.VehicleType.SMALL,true));

        // Initialize medium in system list
        List<MsqEvent> eventInServiceListMedium = eventListManager.getEventInServiceListMedium();
        int nextEventMedium = MsqEvent.getNextEvent(eventInServiceListMedium);
        systemList.set(1, new MsqEvent(eventInServiceListSmall.get(nextEventSmall).getTime(),ARRIVAL, MsqEvent.VehicleType.MEDIUM,true));

        // Initialize large in system list
        List<MsqEvent> eventInServiceListLarge = eventListManager.getEventInServiceListLarge();
        int nextEventLarge = MsqEvent.getNextEvent(eventInServiceListLarge);
        systemList.set(2, new MsqEvent(eventInServiceListSmall.get(nextEventSmall).getTime(),ARRIVAL, MsqEvent.VehicleType.LARGE,true));

        // Initialize rideSharing in system list
        List<MsqEvent> eventInServiceListRideSharing = eventListManager.getEventInServiceListRideSharing();
        int nextEventRideSharing = MsqEvent.getNextEvent(eventInServiceListRideSharing);
        systemList.set(3, new MsqEvent(eventInServiceListSmall.get(nextEventSmall).getTime(),ARRIVAL, MsqEvent.VehicleType.RIDESHARING,true));

        eventListManager.setSystemEventsList(systemList);
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

        while (msqT.getCurrent() < STOP_FIN) {
            if ((e = getNextEvent(eventList)) == -1) break;

            msqT.setNext(eventList.get(e).getTime());
            this.area = this.area + (msqT.getNext() - msqT.getCurrent()) * number;
            msqT.setCurrent(msqT.getNext());

            if (e < 4) {
                centerList.get(e).setSeed(seed);
                centerList.get(e).finiteSimulation(e);
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