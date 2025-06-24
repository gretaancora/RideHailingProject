package Model;

public abstract class Server {
    private final int capacity;
    private boolean availability;

    public Server(int capacity){
        this.capacity = capacity;
        this.availability = true;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean getAvailability() {
        return availability;
    }
}
