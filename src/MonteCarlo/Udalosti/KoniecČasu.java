package MonteCarlo.Udalosti;

import MonteCarlo.Predajna;
import MonteCarlo.UdalostnaSimulacia;

public class KoniecČasu extends Udalost{
    public KoniecČasu(UdalostnaSimulacia jadro, double casUdalosti) {
        super(jadro, casUdalosti);
    }

    @Override
    public void execute() {
        Predajna predajna = (Predajna)jadro;
        predajna.getOsobyQueue().clear();
    }
}
