package Model;

public class MsqAggrData {

    private long arrivals; //numero di arrivi
    private long cancelledRequests; //numero richieste cancellate prima del match
    private long matchedRequests; // richieste accettate
    private double sumWaitingTime; // waiting time totale
    private double sumServiceTime; //service time totale
    private long totalCompleted; // richieste totali completate

    public MsqAggrData(){
        this.arrivals = 0;
        this.cancelledRequests = 0;
        this.matchedRequests = 0;
        this.sumWaitingTime = 0.0;
        this.sumServiceTime = 0.0;
        this.totalCompleted = 0;
    }
    public void recordArrival() {
        arrivals++;
    }

    public void recordCancellation() {
        cancelledRequests++;
    }

    public void recordMatch(double waitingTime) {
        matchedRequests++;
        sumWaitingTime += waitingTime;
    }

    public void recordCompletion(double serviceTime) {
        totalCompleted++;
        sumServiceTime += serviceTime;
    }

    public long getArrivals() {
        return arrivals;
    }

    public long getCancelledRequests() {
        return cancelledRequests;
    }

    public long getMatchedRequests() {
        return matchedRequests;
    }

    public long getTotalCompleted() {
        return totalCompleted;
    }

    public double getSumServiceTime() {
        return sumServiceTime;
    }

    public double getSumWaitingTime() {
        return sumWaitingTime;
    }

}
