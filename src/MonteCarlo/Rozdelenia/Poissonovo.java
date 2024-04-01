package MonteCarlo.Rozdelenia;

import java.util.Random;

public class Poissonovo {
    private double lambda;
    private Random r;
    private int seed = 1;
    public Poissonovo(double lambda) {
        r = new Random(seed);
        this.lambda = lambda;
    }
    public Poissonovo(double lambda, int seed) {
        r = new Random(seed);
        this.lambda = lambda;
    }

    public double sample() {

        double L = Math.exp(-lambda);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }
}
