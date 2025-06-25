package Controller;

import Model.MsqEvent;

import java.util.ArrayList;
import java.util.List;

import static Utils.Constants.*;

public class EventListManager {

    private static EventListManager instance = null;

    /* Each list handle one server queue */
    private List<MsqEvent> eventInServiceListSmall;
    private List<MsqEvent> eventInServiceListMedium;
    private List<MsqEvent> eventInServiceListLarge;
    private List<MsqEvent> eventInServiceListRideSharing;

    private List<MsqEvent> eventInQueueSmall;
    private List<MsqEvent> eventInQueueMedium;
    private List<MsqEvent> eventInQueueLarge;
    private List<MsqEvent> eventInQueueRideSharing;

    private List<MsqEvent> systemEventsList;

    private int carsSmall;
    private int carsMedium;
    private int carsLarge;
    private List<Integer> carsRideSharing = new ArrayList<Integer>();

    private EventListManager() {
        this.eventInServiceListSmall = new ArrayList<>(SMALL_SERVER);
        this.eventInServiceListMedium = new ArrayList<>(MEDIUM_SERVER);
        this.eventInServiceListLarge = new ArrayList<>(LARGE_SERVER);
        this.eventInServiceListRideSharing = new ArrayList<>(SMALL_SERVER_RIDESHARING + MEDIUM_SERVER_RIDESHARING + LARGE_SERVER_RIDESHARING);

        this.eventInQueueSmall = new ArrayList<>();
        this.eventInQueueMedium = new ArrayList<>();
        this.eventInQueueLarge = new ArrayList<>();
        this.eventInQueueRideSharing = new ArrayList<>();

        this.systemEventsList = new ArrayList<>(NODES);
    }

    public static synchronized EventListManager getInstance() {
        /* If instance doesn't exist create a new one */
        if (instance == null) {
            instance = new EventListManager();
        }
        return instance;
    }

    public List<MsqEvent> getEventInServiceListSmall() {
        return eventInServiceListSmall;
    }

    public List<MsqEvent> getEventInServiceListMedium() {
        return eventInServiceListMedium;
    }

    public List<MsqEvent> getEventInServiceListLarge() {
        return eventInServiceListLarge;
    }

    public List<MsqEvent> getEventInServiceListRideSharing() {
        return eventInServiceListRideSharing;
    }

    public void setEventInServiceListRideSharing(List<MsqEvent> eventInServiceListRideSharing) {
        this.eventInServiceListRideSharing = eventInServiceListRideSharing;
    }

    public List<MsqEvent> getEventInQueueSmall() {
        return eventInQueueSmall;
    }

    public void setEventInQueueSmall(List<MsqEvent> eventInQueueSmall) {
        this.eventInQueueSmall = eventInQueueSmall;
    }

    public List<MsqEvent> getEventInQueueMedium() {
        return eventInQueueMedium;
    }

    public void setEventInQueueMedium(List<MsqEvent> eventInQueueMedium) {
        this.eventInQueueMedium = eventInQueueMedium;
    }

    public List<MsqEvent> getEventInQueueLarge() {
        return eventInQueueLarge;
    }

    public void setEventInQueueLarge(List<MsqEvent> eventInQueueLarge) {
        this.eventInQueueLarge = eventInQueueLarge;
    }

    public List<MsqEvent> getEventInQueueRideSharing() {
        return eventInQueueRideSharing;
    }

    public void setEventInQueueRideSharing(List<MsqEvent> eventInQueueRideSharing) {
        this.eventInQueueRideSharing = eventInQueueRideSharing;
    }

    public int getCarsSmall() {
        return carsSmall;
    }

    public void setCarsSmall(int carsSmall) {
        this.carsSmall = carsSmall;
    }

    public int getCarsMedium() {
        return carsMedium;
    }

    public void setCarsMedium(int carsMedium) {
        this.carsMedium = carsMedium;
    }

    public int getCarsLarge() {
        return carsLarge;
    }

    public void setCarsLarge(int carsLarge) {
        this.carsLarge = carsLarge;
    }

    public List<Integer> getCarsRideSharing() {
        return carsRideSharing;
    }

    public void setCarsRideSharing(List<Integer> carsRideSharing) {
        this.carsRideSharing = carsRideSharing;
    }

    public void setEventInServiceListSmall(List<MsqEvent> eventInServiceListSmall) {
        this.eventInServiceListSmall = eventInServiceListSmall;
    }

    public void setEventInServiceListMedium(List<MsqEvent> eventInServiceListMedium) {
        this.eventInServiceListMedium = eventInServiceListMedium;
    }

    public void setEventInServiceListLarge(List<MsqEvent> eventInServiceListLarge) {
        this.eventInServiceListLarge = eventInServiceListLarge;
    }

    public void resetState() {

            this.eventInServiceListSmall.clear();
            this.eventInServiceListMedium.clear();
            this.eventInServiceListLarge.clear();
            this.eventInServiceListRideSharing.clear();

            this.eventInQueueSmall.clear();
            this.eventInQueueMedium.clear();
            this.eventInQueueLarge.clear();
            this.eventInQueueRideSharing.clear();

            this.systemEventsList.clear();

            this.carsSmall = SMALL_SERVER;
            this.carsMedium = MEDIUM_SERVER;
            this.carsLarge = LARGE_SERVER;
            this.carsRideSharing.clear();
            this.carsRideSharing.add(SMALL_SERVER_RIDESHARING);
            this.carsRideSharing.add(MEDIUM_SERVER_RIDESHARING);
            this.carsRideSharing.add(LARGE_SERVER_RIDESHARING);
    }

    public void setSystemEventsList(List<MsqEvent> systemList) {
        this.systemEventsList = systemList;
    }

    public List<MsqEvent> getSystemEventsList() {
        return systemEventsList;
    }
}
