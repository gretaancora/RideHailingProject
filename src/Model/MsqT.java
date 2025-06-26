package Model;

public class MsqT {
    private double current;     // Tempo attuale
    private double next;        // Tempo del prossimo evento
    private double batchTimer;  // Tempo per batching

    // Istanza statica (unica) della classe
    private static MsqT instance;

    // Costruttore privato per evitare istanziazioni esterne
    private MsqT() {
        this.current = 0;
        this.next = 0;
    }

    // ✅ Metodo per ottenere l'unica istanza
    public static MsqT getInstance() {
        if (instance == null) {
            instance = new MsqT();
        }
        return instance;
    }

    // Getter e Setter
    public double getCurrent() {
        return current;
    }

    public double getNext() {
        return next;
    }

    public double getBatchTimer() {
        return batchTimer;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public void setNext(double next) {
        this.next = next;
    }

    public void setBatchTimer(double batchTimer) {
        this.batchTimer = batchTimer;
    }
}
