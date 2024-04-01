package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.SimJadro;
import MonteCarlo.Stanok;
import MonteCarlo.UdalostnaSimulacia;

public class ZačiatokObsluhy extends Udalost {
    private Osoba osoba;
    public ZačiatokObsluhy(UdalostnaSimulacia jadro, double casUdalosti, Osoba osoba) {

        super(jadro, casUdalosti);
        this.osoba = osoba;
    }

    @Override
    public void execute() {
        Stanok stanok = (Stanok)jadro;
        stanok.setPokladnaIsEmpty(false);
        stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
        osoba.setStav(StavyOsoby.JE_OBSLUHOVANY);
        stanok.naplanujUdalost(new KoniecObsluhy(stanok, stanok.getSimCas()+stanok.getObsluha().sample(), osoba));

    }
}
