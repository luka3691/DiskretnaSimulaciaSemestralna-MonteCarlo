package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Stanok;
import MonteCarlo.UdalostnaSimulacia;

public class PrichodZakaznika extends Udalost {
    public PrichodZakaznika(UdalostnaSimulacia jadro, double casUdalosti ) {
        super(jadro, casUdalosti);
    }


    @Override
    public void execute() {
        Stanok stanok = (Stanok)jadro;
        Osoba osoba = new Osoba(StavyOsoby.PRICHOD, casUdalosti, stanok.getNewPersonIndex(), stanok.getNahodnyJav().getTypZakaznika());

        if (stanok.getAutomatIsEmpty() && stanok.getObsluzneMiesta().zmestiSa()) {
            stanok.naplanujUdalost(new ZačiatokZadavaniaDoAutomatu(stanok, stanok.getSimCas(), osoba));
        } else {
            stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
            osoba.setStav(StavyOsoby.V_RADE_PRED_AUTOMATOM);
            stanok.getOsobyQueue().add(osoba);
            stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
        }

        double dalsiPrichod = stanok.getSimCas() + stanok.getNahodnyJav().getPrichodLudi();
        //toto tu treba skontrolovat
        if (dalsiPrichod < stanok.getKoniecCasu()) {
            stanok.naplanujUdalost(new PrichodZakaznika(jadro, dalsiPrichod));
        }
    }


}
