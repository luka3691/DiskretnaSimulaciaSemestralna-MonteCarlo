package MonteCarlo.Udalosti;

import MonteCarlo.Osoby.Osoba;
import MonteCarlo.SimJadro;
import MonteCarlo.UdalostnaSimulacia;

public abstract class Udalost{
    protected double casUdalosti;
    protected UdalostnaSimulacia jadro;

    public Udalost(UdalostnaSimulacia jadro, double casUdalosti) {
        this.jadro = jadro;
        this.casUdalosti = casUdalosti;

    }
    public abstract void execute();

    public void generujCasObsluhy() {

    }

    public double getCasUdalosti() {
        return casUdalosti;
    }

    public void setCasUdalosti(double casUdalosti) {
        this.casUdalosti = casUdalosti;
    }
}
