package Model;

public class RideSharingServer extends Server{
    private int remainingCapacity;

    public RideSharingServer(int capacity){
        super(capacity);
        remainingCapacity = capacity;
    }

    public int getRemainingCapacity(){
        return remainingCapacity;
    }
}
