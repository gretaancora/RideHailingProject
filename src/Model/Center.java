package Model;

public interface Center {
    /* Finite horizon simulation */
    void finiteSimulation() throws Exception;

    /* Infinite horizon simulation */
    void infiniteSimulation();

    void calculateBatchStatistics();

    int getNumJob();

    int getJobInBatch();

    void setSeed(long seed);

    void printIteration(boolean isFinite, long seed, int event, int runNumber, double time);

    void printResult(int runNumber, long seed);

    void printFinalStatsStazionario();

    void printFinalStatsTransitorio();
}