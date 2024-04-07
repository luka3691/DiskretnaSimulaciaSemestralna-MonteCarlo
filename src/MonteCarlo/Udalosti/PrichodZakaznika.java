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
        Osoba osoba = new Osoba(StavyOsoby.PRICHOD, stanok.getSimCas(), stanok.getNewPersonIndex(), stanok.getNahodnyJav().getTypZakaznika());

        stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
        osoba.setStav(StavyOsoby.V_RADE_PRED_AUTOMATOM);
        stanok.getOsobyQueue().add(osoba);
        stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());

        if (stanok.getAutomatIsEmpty() && stanok.getObsluzneMiesta().zmestiSa(stanok.getAutomatIsEmpty())) {
            stanok.naplanujUdalost(new ZačiatokZadavaniaDoAutomatu(stanok, stanok.getSimCas(), stanok.getOsobyQueue().poll()));
            stanok.setAutomatIsEmpty(false);
        }

        double dalsiPrichod = stanok.getSimCas() + stanok.getNahodnyJav().getPrichodLudi();
        if (dalsiPrichod < stanok.getKoniecCasu()) {
            stanok.naplanujUdalost(new PrichodZakaznika(jadro, dalsiPrichod));
        } else {
            stanok.getOsobyQueue().clear();
        }
    }


}
