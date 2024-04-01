package MonteCarlo.Rozdelenia;

import java.util.Random;

public class Exponencialne {
    private Random random;
    private double lambda;
    private int seed = 1;
    public Exponencialne(double lambda) {
        this.random = new Random();
        this.lambda = lambda;
    }
    public Exponencialne(double lambda, int seed) {
        this.random = new Random(seed);
        this.lambda = lambda;
    }

    public double sample() {
        return  Math.log(1.0-random.nextDouble())*(-lambda);
    }
}
