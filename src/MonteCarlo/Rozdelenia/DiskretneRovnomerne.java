package MonteCarlo.Rozdelenia;

import java.util.Random;

public class DiskretneRovnomerne implements IRozdelenie{
    private Random random;
    private final int min;
    private final int max;

    public DiskretneRovnomerne(int min, int max, Random random) {
        this.min = min;
        this.max = max;
        this.random = new Random();
    }

    public double sample() {
        return this.random.nextInt(this.max - this.min + 1) + this.min ;
    }
}
