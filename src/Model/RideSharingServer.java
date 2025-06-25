package Model;

public class RideSharingServer extends Server {
    private int remainingCapacity;

    public RideSharingServer(Type type){
        super(type);
        this.remainingCapacity = type.getCapacity();
    }

    public int getRemainingCapacity(){
        return remainingCapacity;
    }

    // Aggiunge capacità, lancia eccezione se si supera la capacità massima
    public void addCapacity(int amount) throws IllegalArgumentException {
        if (remainingCapacity + amount > super.getCapacity()) {
            throw new IllegalArgumentException("Non puoi aggiungere oltre la capacità massima di " + super.getCapacity());
        }
        remainingCapacity += amount;
    }

    // Sottrae capacità, lancia eccezione se si scende sotto zero
    public void subtractCapacity(int amount) throws IllegalArgumentException {
        if (remainingCapacity - amount < 0) {
            throw new IllegalArgumentException("Non puoi sottrarre oltre la capacità disponibile (0)");
        }
        remainingCapacity -= amount;
    }
}
