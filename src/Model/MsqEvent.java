package Model;

public class MsqEvent {

    public MsqEvent(double time, int i, int i1) {
    }

    public int getX() {
    }

    public double getT() {
    }

    public enum EventType {
        ARRIVAL,    // Request arrives
        MATCH,      // Request matched with a vehicle
        CANCEL,     // Request cancelled before matching
        COMPLETE    // Ride completed
    }

    public enum VehicleType {
        SMALL(0),
        MEDIUM(1),
        LARGE(2);

        private final int type;

        VehicleType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    private final double time;            // Event time
    private final EventType type;         // Type of event
    private final VehicleType vehicleType;

    public MsqEvent(double time, EventType type, VehicleType vehicleType) {
        this.time = time;
        this.type = type;
        this.vehicleType = vehicleType;
    }

    public double getTime() {
        return time;
    }

    public EventType getType() {
        return type;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Override
    public String toString() {
        return String.format("[time=%.3f, type=%s, vehicleType=%s]", time, type, vehicleType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MsqEvent)) return false;
        MsqEvent other = (MsqEvent) obj;
        return Double.compare(time, other.time) == 0 &&
                type == other.type &&
                vehicleType == other.vehicleType;
    }

    @Override
    public int hashCode() {
        int result = 17;
        long temp = Double.doubleToLongBits(time);
        result = 31 * result + (int)(temp ^ (temp >>> 32));
        result = 31 * result + type.hashCode();
        result = 31 * result + vehicleType.hashCode();
        return result;
    }
}
