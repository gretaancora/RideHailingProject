package Utils;

import Libs.Rngs;

public class Distribution {
    private static Distribution instance = null;

    private final Rngs rngs;

    private Distribution(Rngs rngsToSet) {
        this.rngs = rngsToSet;
    }

    public static Distribution getInstance(Rngs rngsToSet) {
        if (instance == null) {
            instance = new Distribution(rngsToSet);
        }
        return instance;
    }

    public static Distribution getInstance() {
        return instance;
    }

    /** Distribuzione esponenziale per gli arrivi */
    public double exponential(double lambda) {
        return -lambda * Math.log(1.0 - rngs.random());
    }

    /** Generatore di gaussiana troncata */
    public double truncatedNormal(double mean, double stdDev, double min, double max) {
        double value;
        do {
            double u1 = rngs.random();
            double u2 = rngs.random();
            double z = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
            value = mean + stdDev * z;
        } while (value < min || value > max);
        return value;
    }

    /** Servizio tradizionale */
    public double getServiceTraditional(double mean, double stdDev, double min, double max) {
        rngs.selectStream(1);
        return truncatedNormal(mean, stdDev, min, max);
    }

    /** Servizio con ride sharing (tempo più lungo) */
    public double getServiceRideSharing(double mean, double stdDev, double min, double max, double delay) {
        rngs.selectStream(2);
        return truncatedNormal(mean, stdDev, min, max) + delay;
    }

    public Rngs getRngs() {
        return rngs;
    }

    public double generateArrivalTime(int index) {
        exponential(LAMBDA * "P".concat(Integer.toString(index)));
    }
}
