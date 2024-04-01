package MonteCarlo.Udalosti;
import java.util.Comparator;

public class UdalostComparator implements Comparator<Udalost> {
    @Override
    public int compare(Udalost u1, Udalost u2) {
        // Compare based on casUdalosti
        if (u1.getCasUdalosti() < u2.getCasUdalosti())
            return -1;
        else if (u1.getCasUdalosti() > u2.getCasUdalosti())
            return 1;
        else
            return 0;
    }
}

