package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Osoby.TypZakaznika;
import MonteCarlo.Predajna;
import MonteCarlo.UdalostnaSimulacia;

public class KoniecZdavaniaDoAutomatu extends Udalost{
    Osoba osoba;
    public KoniecZdavaniaDoAutomatu(UdalostnaSimulacia jadro, double casUdalosti, Osoba osoba) {
        super(jadro, casUdalosti);
        this.osoba = osoba;

    }

    @Override
    public void execute() {
        Predajna predajna = (Predajna)jadro;
        osoba.setStav(StavyOsoby.KONIEC_ZADAVANIA_DO_AUTOMATU);
        int id = predajna.getObsluzneMiesta().getIDVolnaPokladna(osoba);
        if (id != -1) {
            //nasla sa volna pokladna, naplanuj zaciatok obsluhy
            predajna.naplanujUdalost(new ZačiatokObsluhy(predajna, predajna.getSimCas(), osoba, id));
            predajna.setAutomatIsEmpty(true);
        } else {
            //zarad osobu do radu pred obsluznymi
            predajna.getObsluzneMiesta().zaradDoRadu(osoba);
            if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
                predajna.getPriemerDlzkaRaduPredObsluzOnline().pridajZaznam(predajna.getObsluzneMiesta().getOnlineQueue().size(), predajna.getSimCas());
            } else {
                predajna.getPriemerDlzkaRaduPredObsluzNormal().pridajZaznam(predajna.getObsluzneMiesta().getOsobyQueue().size(), predajna.getSimCas());
            }
            if (predajna.getObsluzneMiesta().zmestiSa(predajna.getAutomatIsEmpty())) {
                predajna.setAutomatIsEmpty(true);
            }
            osoba.setStav(StavyOsoby.V_RADE_PRED_OSBLUHOU);
        };
        //ak je niekto v rade pred automatom a zmesti sa do radu pred obsluzne tak naplanuj dalsie zadavanie do automatu
        predajna.setOsobaUAutomatu(null);
        if (!predajna.getOsobyQueue().isEmpty() && predajna.getObsluzneMiesta().zmestiSa(predajna.getAutomatIsEmpty())) {
            Osoba novaOsoba = predajna.getOsobyQueue().poll();
            predajna.naplanujUdalost(new ZačiatokZadavaniaDoAutomatu(predajna, predajna.getSimCas(), novaOsoba));
            predajna.setAutomatIsEmpty(false);
        }
        predajna.setStavyOsob(osoba.toArray());
    }
}
