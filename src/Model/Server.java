package Model;

public abstract class Server {

    private boolean availability;
    private final Type type;

    public enum Type {
        SMALL(3),
        MEDIUM(4),
        LARGE(8);

        private final int capacity;

        Type(int capacity) {
            this.capacity = capacity;
        }

        public int getCapacity() {
            return capacity;
        }
    }

    public Server(Type type){
        this.type = type;
        this.availability = true;
    }

    public int getCapacity() {
        return type.getCapacity();
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public Type getType() {
        return type;
    }
}
