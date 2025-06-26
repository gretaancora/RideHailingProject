package Model;

import Controller.EventListManager;
import Utils.Distribution;
import Model.MsqEvent.*;

import java.util.ArrayList;
import java.util.List;

public class RideSharingCenter implements Center{

    private final List<MsqEvent> serverList;
    private final EventListManager eventListManager;
    private final MsqT msqT;
    private int s;
    private Distribution distr;

    public RideSharingCenter(int i, VehicleType vehicleType) {

        eventListManager = EventListManager.getInstance();
        distr = Distribution.getInstance();
        msqT = MsqT.getInstance();
        serverList = new ArrayList<>(i+1);

        /* Initial servers setup */
        for (s = 0; s < 1 + i; s++) {
            serverList.add(s, new MsqEvent(0, MsqEvent.EventType.ARRIVAL, vehicleType, true));
        }

        // First arrival event (car to charge)
        double arrival = distr.getArrival(vehicleType);

        // Add this new event and setting time to arrival time
        serverList.set(0, new MsqEvent(arrival, MsqEvent.EventType.ARRIVAL, vehicleType, true));
        eventListManager.setEventInServiceListRideSharing(serverList);
    }

    @Override
    public void finiteSimulation(int e) throws Exception {

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

    @Override
    public void setMsqT(MsqT msqT) {

    }
}
