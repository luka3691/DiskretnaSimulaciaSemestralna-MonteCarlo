package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Predajna;
import MonteCarlo.UdalostnaSimulacia;

public class PrichodZakaznika extends Udalost {
    public PrichodZakaznika(UdalostnaSimulacia jadro, double casUdalosti ) {
        super(jadro, casUdalosti);
    }


    @Override
    public void execute() {
        Predajna predajna = (Predajna)jadro;
        Osoba osoba = new Osoba(StavyOsoby.PRICHOD, predajna.getSimCas(), predajna.getNewPersonIndex(), predajna.getNahodnyJav().getTypZakaznika());

        osoba.setStav(StavyOsoby.V_RADE_PRED_AUTOMATOM);
        predajna.getPriemerDlzkaRadu().pridajZaznam(predajna.getOsobyQueue().size(), predajna.getSimCas());

        if (predajna.getAutomatIsEmpty() && predajna.getObsluzneMiesta().zmestiSa(predajna.getAutomatIsEmpty())) {
            predajna.naplanujUdalost(new ZačiatokZadavaniaDoAutomatu(predajna, predajna.getSimCas(), osoba));
            predajna.setAutomatIsEmpty(false);
        } else {
            predajna.getOsobyQueue().add(osoba);
            predajna.getPriemerDlzkaRadu().pridajZaznam(predajna.getOsobyQueue().size(), predajna.getSimCas());
        }

        double dalsiPrichod = predajna.getSimCas() + predajna.getNahodnyJav().getPrichodLudi();
        if (dalsiPrichod < predajna.getKoniecCasu()) {
            predajna.naplanujUdalost(new PrichodZakaznika(jadro, dalsiPrichod));
        }
        predajna.setStavyOsob(osoba.toArray());
    }


}
