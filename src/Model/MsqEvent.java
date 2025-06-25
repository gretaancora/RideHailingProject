package Model;

import java.util.List;

public class MsqEvent {

    public static int findOne(List<MsqEvent> event) {
        int s;
        int i = 2;

        /* find the index of the first available */
        while (i < event.size() && event.get(i).x != false)
            i++;                        /* (idle) server */

        if (i >= event.size()) return -1;

        s = i;
        while (i < event.size() - 1) {         /* now, check the others to find which   */
            i++;                        /* has been idle longest                 */

            if ((event.get(i).x == false) && (event.get(i).time < event.get(s).time))
                s = i;
        }

        return (s);
    }

    public static int findActiveServers(List<MsqEvent> event) {
        int count = 0;

        int s = 2;
        while (s < event.size()) {
            if (event.get(s).getX() == true) count++;

            s++;
        }

        return count;
    }

    public void setT(double v) {
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
        LARGE(2),
        RIDESHARING(3);

        private final int type;

        VehicleType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }


        public static VehicleType fromInt(int value) {
            for (VehicleType type : VehicleType.values()) {
                if (type.getType() == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid VehicleType value: " + value);
        }
    }

    private final double time;            // Event time
    private final EventType type;         // Type of event
    private final VehicleType vehicleType;
    private boolean x;

    public MsqEvent(double time, EventType type, VehicleType vehicleType, boolean x) {
        this.time = time;
        this.type = type;
        this.vehicleType = vehicleType;
        this.x = x;
    }

    public boolean getX() {
        return x;
    }

    public void setX(boolean x) {
        this.x = x;
    }

    public double getTime() {
        return time;
    }

    public double setTime(double time) {
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
        result = 31 * result + Double.hashCode(time);
        result = 31 * result + type.hashCode();
        result = 31 * result + vehicleType.hashCode();
        return result;
    }

    public static int getNextEvent(List<MsqEvent> eventList){
        int e;
        int i = 0;

        while (i < eventList.size() && !eventList.get(i).x)       /* find the index of the first 'active' */
            i++;                                                /* element in the event list            */

        if (i >= eventList.size()) return -1;

        e = i;
        while (i < eventList.size() - 1) {         /* now, check the others to find which  */
            i++;                             /* event type is most imminent          */

            if ((eventList.get(i).x) && (eventList.get(i).time < eventList.get(e).time))
                e = i;
        }
        return (e);
    }
}
