package Model;

//stato e comportamento

import Controller.EventListManager;
import Utils.Distribution;
import Model.MsqEvent.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleCenter implements Center {

    // λ_ext (quello del servizio tradizionale), λ_int(quello che mi viene dal ride sharing), s_1, s_2, ..., s_n (numero di server)

    //stessa struttura ma per tre centri diversi

    private final List<MsqEvent> serverList;
    private final EventListManager eventListManager;
    private int s;
    private Distribution distr;
    private int number; //number oj jobs in the system
    private MsqT msqT;
    private double area;
    private double service;
    private int index; //number oj job processing

    public SimpleCenter(int i, VehicleType vehicleType) {

        eventListManager = EventListManager.getInstance();
        distr = Distribution.getInstance();
        msqT = MsqT.getInstance();
        serverList = new ArrayList<>(i + 1);

        /* Initial servers setup */
        for (s = 0; s < 1 + i; s++) {
            serverList.add(s, new MsqEvent(0, EventType.ARRIVAL, vehicleType, true));
        }

        // First arrival event (car to charge)
        double arrival = distr.getArrival(vehicleType);

        // Add this new event and setting time to arrival time
        serverList.set(0, new MsqEvent(arrival, EventType.ARRIVAL, vehicleType, true));

        // per ogni tipologia
        switch (vehicleType) {
            case SMALL -> eventListManager.setEventInServiceListSmall(serverList);
            case MEDIUM -> eventListManager.setEventInServiceListMedium(serverList);
            case LARGE -> eventListManager.setEventInServiceListLarge(serverList);
            default -> throw new IllegalArgumentException("Unsupported vehicle type: " + vehicleType);
        }
    }

    @Override
    public void finiteSimulation(int i) throws Exception {

        /* ricordarsi di aggiungere arrivi interni da ride sharing*/

        List<MsqEvent> eventList = null;
        int e;

        switch (i) {
            case 0 -> eventList = eventListManager.getEventInServiceListSmall();
            case 1 -> eventList = eventListManager.getEventInServiceListMedium();
            case 2 -> eventList = eventListManager.getEventInServiceListLarge();
        }

        // Exit condition : There are no external arrivals, and I haven't processing job.
        if (eventList.getFirst().getX() == false && eventList.get(1).getX() == false && this.number == 0) return;

        //acquisizione dell'evento successivo da gestire
        if ((e = MsqEvent.getNextEvent(eventList)) == -1) return;
        msqT.setNext(eventList.get(e).getTime());
        area += (msqT.getNext() - msqT.getCurrent()) * number;
        msqT.setCurrent(msqT.getNext());

        if (e < 1) {
            //arrivo tradizionale
            this.number++;

            eventList.getFirst().setTime(msqT.getCurrent() + distr.getArrival(VehicleType.fromInt(i)));     /* Get new arrival from exogenous (external) arrival */

            /* if (e == 1) eventList.get(1).setX(0);  da gestire */

            s = MsqEvent.findOne(eventList);    /* Search for an idle Server */
            if (s != -1) {                      /* Found an idle server*/
                service = distr.getServiceTraditional();

                /* Set server as active */
                eventList.get(s).setT(msqT.getCurrent() + service);  /* Let's calculate the end of service time */
                eventList.get(s).setX(true);

            }
        } else {    /* Processing a job */
            this.index++;
            this.number--;
            eventList.get(e).setX(false);       /* Current server is no more usable (e = 2 car is ready to be rented) */
        }

        /* Get next event*/
        int nextEvent = MsqEvent.getNextEvent(eventList);
        if (nextEvent == -1)
            eventListManager.getSystemEventsList().get(1).setX(false);

        eventListManager.getSystemEventsList().get(1).setT(eventList.get(nextEvent).getTime());
    }


    @Override
    public void infiniteSimulation() {

    }

    @Override
    public void calculateBatchStatistics() {

    }

    @Override
    public int getNumJob() {
        return 0;
    }

    @Override
    public int getJobInBatch() {
        return 0;
    }

    @Override
    public void setSeed(long seed) {

    }

    @Override
    public void printIteration(boolean isFinite, long seed, int event, int runNumber, double time) {

    }

    @Override
    public void printResult(int runNumber, long seed) {

    }

    @Override
    public void printFinalStatsStazionario() {

    }

    @Override
    public void printFinalStatsTransitorio() {

    }

    public void setMsqT(MsqT msqT) {
        this.msqT = msqT;
    }

    public List<MsqEvent> getServerList() {
        return serverList;
    }
}
