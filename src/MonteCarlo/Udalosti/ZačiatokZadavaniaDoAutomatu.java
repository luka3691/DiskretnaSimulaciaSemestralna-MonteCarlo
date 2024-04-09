package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Predajna;
import MonteCarlo.UdalostnaSimulacia;

public class ZačiatokZadavaniaDoAutomatu extends Udalost {
    private Osoba osoba;
    public ZačiatokZadavaniaDoAutomatu(UdalostnaSimulacia jadro, double casUdalosti, Osoba osoba) {

        super(jadro, casUdalosti);
        this.osoba = osoba;
    }

    @Override
    public void execute() {
        Predajna predajna = (Predajna)jadro;
        predajna.setAutomatIsEmpty(false);
        predajna.getPriemerCakanieVRadePredAutomatom().pridajZaznam(predajna.getSimCas() - osoba.getCasPrichodu());
        predajna.getPriemerDlzkaRadu().pridajZaznam(predajna.getOsobyQueue().size(), predajna.getSimCas());
        osoba.setStav(StavyOsoby.ZADAVANIE_DO_AUTOMATU);
        predajna.setOsobaUAutomatu(osoba);
        //naplanuj koniec zadavania do automatu
        double trvanieUdalosti = predajna.getNahodnyJav().getCasZadavaniaDoAutomatu();
        predajna.naplanujUdalost(new KoniecZdavaniaDoAutomatu(predajna, predajna.getSimCas()+trvanieUdalosti, osoba));
        predajna.getPriemerVytazenieAutomatu().pridajZaznam(trvanieUdalosti);
        predajna.setStavyOsob(osoba.toArray());
    }
}
