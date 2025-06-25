package Controller;

import Model.MsqAggrData;
import Model.MsqEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventListManager {

    private static EventListManager instance = null;

    private final MsqAggrData[] statisticsPerCategory = new MsqAggrData[3];
    private final List<MsqEvent> serverList = new ArrayList<>(); // eventi da processare, ordinati per tempo

    private EventListManager() {
        for (int i = 0; i < 3; i++) {
            statisticsPerCategory[i] = new MsqAggrData();
        }
    }

    // Singleton getInstance
    public static synchronized EventListManager getInstance() {
        if (instance == null) {
            instance = new EventListManager();
        }
        return instance;
    }

    // Aggiunge evento mantenendo la lista ordinata per tempo t
    public void addEvent(MsqEvent e) {
        int index = Collections.binarySearch(serverList, e, Comparator.comparingDouble(MsqEvent.getTime()));
        if (index < 0) index = -(index + 1);
        serverList.add(index, e);
    }

    // Processa il prossimo evento e aggiorna statistiche e stato
    public MsqEvent processNextEvent() {
        if (serverList.isEmpty()) return null;

        MsqEvent event = serverList.remove(0);

        MsqEvent.VehicleType cat = event.getVehicleType();  // assuming getCategory() in MsqEvent returns 1-based category

        switch (event.getType()) {
            case ARRIVAL:
                statisticsPerCategory[cat.getType()].recordArrival();
                handleArrival(event);
                break;
            case MATCH:
                // Es. waitingTime potrebbe venire calcolato altrove e passato qui o da evento
                double waitingTime = 0; // placeholder
                statisticsPerCategory[cat.getType()].recordMatch(waitingTime);
                handleMatch(event, waitingTime);
                break;
            case CANCEL:
                statisticsPerCategory[cat.getType()].recordCancellation();
                handleCancel(event);
                break;
            case COMPLETE:
                double serviceTime = 0; // placeholder
                statisticsPerCategory[cat.getType()].recordCompletion(serviceTime);
                handleCompletion(event, serviceTime);
                break;
        }

        return event;
    }

    // Metodi per logica specifica (puoi implementare in modo più dettagliato)
    private void handleArrival(MsqEvent e) {
        // inserisci in coda, genera nuovo evento match se possibile, ecc.
    }

    private void handleMatch(MsqEvent e, double waitingTime) {
        // logica di matching: aggiornamenti coda, assegnazione veicoli, ecc.
    }

    private void handleCancel(MsqEvent e) {
        // logica di cancellazione richiesta
    }

    private void handleCompletion(MsqEvent e, double serviceTime) {
        // logica completamento servizio
    }

    // Getter per statistiche
    public MsqAggrData[] getStatistics() {
        return statisticsPerCategory;
    }

    // Getter per la lista eventi (utile per debug o ispezione)
    public List<MsqEvent> getServerList() {
        return serverList;
    }
}
