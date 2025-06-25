package Controller;

import Libs.Rngs;
import Model.MsqEvent;
import Model.MsqT;
import Model.SimpleCenter;
import Utils.Distribution;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static Utils.Constants.*;

public class SystemController {

    private SimpleCenter[] centerTrad = null;
    private SimpleCenter rideSharingCenter = null;
    private final Distribution distr;
    private final MsqT msqT = new MsqT();

    private double area = 0.0;
    private int number = 0;

    private final List<MsqEvent> eventList = new ArrayList<>();

    private String systemType;

    public SystemController(Rngs rngs) throws IOException {
        distr = Distribution.getInstance(rngs);

        loadConfig();

        if ("simple".equalsIgnoreCase(systemType)) {
            initSimpleCenters();
        } else if ("sharing".equalsIgnoreCase(systemType)) {
            initSimpleCenters();
            initRideSharingCenter();
        } else {
            throw new IllegalArgumentException("Unsupported SYSTEM_TYPE: " + systemType);
        }

        initEventList();
    }

    private void loadConfig() throws IOException {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            prop.load(fis);
            systemType = prop.getProperty("SYSTEM_TYPE", "simple").trim();
            System.out.println("Loaded SYSTEM_TYPE = " + systemType);
        }
    }

    private void initSimpleCenters() {
        centerTrad = new SimpleCenter[3];
        for (int i = 0; i < 3; i++) {
            centerTrad[i] = new SimpleCenter();
        }
    }

    private void initRideSharingCenter() {
        rideSharingCenter = new SimpleCenter();
    }

    private void initEventList() {
        eventList.clear();

        //3 centri tradizionali
        for (int i = 0; i < 3;) {
            eventList.add(new MsqEvent(distr.generateArrivalTime(++i), 0, i));
        }

        //centro ride sharing
        if ("sharing".equalsIgnoreCase(systemType)) {
            eventList.add(new MsqEvent(distr.generateArrivalTime(4), 0, 3));
        }
    }


    public void runFiniteSimulation(long seed, int runNumber) throws Exception {
        int e;

        while (msqT.getCurrent() < STOP_FIN) {
            if ((e = getNextEvent(eventList)) == -1) break;

            MsqEvent evt = eventList.get(e);

            msqT.setNext(evt.getT());
            area += (msqT.getNext() - msqT.getCurrent()) * number;
            msqT.setCurrent(msqT.getNext());

            if (evt.getX() < 3) {
                centerTrad[evt.getX()].setSeed(seed);
                centerTrad[evt.getX()].finiteSimulation();
                centerTrad[evt.getX()].printIteration(true, seed, evt.getX(), runNumber, msqT.getCurrent());

            } else if (evt.getX() == 3 && "sharing".equalsIgnoreCase(systemType)) {
                rideSharingCenter.setSeed(seed);
                rideSharingCenter.finiteSimulation();
                rideSharingCenter.printIteration(true, seed, 3, runNumber, msqT.getCurrent());

            } else {
                throw new Exception("Invalid event index: " + evt.getX());
            }

            // Rigenero evento con nuovo tempo di arrivo
            eventList.set(e, new MsqEvent(msqT.getCurrent() + distr.generateArrivalTime(evt.getX()), 0, evt.getX()));
        }

        System.out.println("\n\n");

        for (int i = 0; i < 3; i++) {
            centerTrad[i].printResult(runNumber, seed);
        }
        if ("sharing".equalsIgnoreCase(systemType)) {
            rideSharingCenter.printResult(runNumber, seed);
        }
    }

    public void runInfiniteSimulation(long seed) throws Exception {
        int e;

        msqT.setCurrent(START);
        msqT.setNext(START);

        while (pendingEvents()) {
            if ((e = getNextEvent(eventList)) == -1) break;

            MsqEvent evt = eventList.get(e);

            msqT.setNext(evt.getT());
            area += (msqT.getNext() - msqT.getCurrent()) * number;
            msqT.setCurrent(msqT.getNext());

            if (evt.getX() < 3) {
                centerTrad[evt.getX()].setSeed(seed);
                centerTrad[evt.getX()].infiniteSimulation();
            } else if (evt.getX() == 3 && "sharing".equalsIgnoreCase(systemType)) {
                rideSharingCenter.setSeed(seed);
                rideSharingCenter.infiniteSimulation();
            } else {
                throw new Exception("Invalid event index: " + evt.getX());
            }

            // Rigenero evento con nuovo tempo di arrivo
            eventList.set(e, new MsqEvent(msqT.getCurrent() + distr.generateArrivalTime(evt.getX()), 0, evt.getX()));
        }

        for (int i = 0; i < 3; i++) {
            centerTrad[i].printFinalStatsStazionario();
        }
        if ("sharing".equalsIgnoreCase(systemType)) {
            rideSharingCenter.printFinalStatsStazionario();
        }
    }

    private boolean pendingEvents() {
        // Adatta la logica di terminazione se serve
        return true;
    }

    private int getNextEvent(List<MsqEvent> events) {
        double min = Double.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < events.size(); i++) {
            double t = events.get(i).getT();
            if (t > 0 && t < min) {
                min = t;
                index = i;
            }
        }
        return index;
    }
}
