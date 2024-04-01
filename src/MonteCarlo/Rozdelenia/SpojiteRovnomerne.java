package MonteCarlo.Rozdelenia;

import java.util.Random;

public class SpojiteRovnomerne implements IRozdelenie{
    private Random random;
    private final double min;
    private final double max;

    public SpojiteRovnomerne(double min, double max, Random random) {
        this.min = min;
        this.max = max;
        this.random = new Random(random.nextInt());
    }

    public double sample() {
        return this.random.nextDouble() * ( this.max - this.min ) + this.min;
    }
}