package MonteCarlo.Rozdelenia;

import java.util.Random;

public class Poissonovo {
    private double lambda;
    public Poissonovo(double lambda, int seed) {
        this.lambda = lambda;
    }

    public double sample() {
        Random r = new Random();
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
