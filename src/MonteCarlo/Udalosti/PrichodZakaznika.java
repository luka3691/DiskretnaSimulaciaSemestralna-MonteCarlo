package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.SimJadro;
import MonteCarlo.Stanok;
import MonteCarlo.UdalostnaSimulacia;

public class PrichodZakaznika extends Udalost {
    public PrichodZakaznika(UdalostnaSimulacia jadro, double casUdalosti ) {
        super(jadro, casUdalosti);
    }


    @Override
    public void execute() {
        Stanok stanok = (Stanok)jadro;
        Osoba osoba = new Osoba(StavyOsoby.PRICHOD, casUdalosti, stanok.getNewPersonIndex());
        int volnaPokladna = stanok.getVolnaPokladna();
        if (volnaPokladna == 1) {
            stanok.naplanujUdalost(new ZačiatokObsluhy(stanok, stanok.getSimCas(), osoba));
        } else {
            stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
            osoba.setStav(StavyOsoby.V_RADE);
            stanok.getOsobyQueue().add(osoba);
            stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
        }

        double dalsiPrichod = stanok.getSimCas() + stanok.getPrichodLudi().sample();
        if (dalsiPrichod < stanok.getKoniecCasu()) {
            stanok.naplanujUdalost(new PrichodZakaznika(jadro, dalsiPrichod));
        }
    }


}
