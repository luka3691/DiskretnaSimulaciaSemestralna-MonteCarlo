package MonteCarlo.Rozdelenia;

import java.util.Random;

public class Exponencialne {
    private Random random;
    private double lambda;
    public Exponencialne(Random random, double lambda) {
        this.random = new Random();
        this.lambda = lambda;
    }

    public double sample() {
        return  Math.log(1.0-random.nextDouble())*(-lambda);
    }
}
