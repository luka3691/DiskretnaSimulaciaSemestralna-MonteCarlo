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

        //tu treba zaznamenat cas v obchode do statistiky
        //osoba.setStav(StavyOsoby.ODCHADZA);
        //stanok.getPriemerCasVObchode().pridajZaznam(stanok.getSimCas() - osoba.getCasPrichodu());

        //


        //tu treba zaznamenat dlzku radu (pretoze sa meni velkost radu)
        //stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
        osoba.setStav(StavyOsoby.KONIEC_ZADAVANIA_DO_AUTOMATU);
        int id = predajna.getObsluzneMiesta().getIDVolnaPokladna(osoba);
        if (id != -1) {
            predajna.naplanujUdalost(new ZačiatokObsluhy(predajna, predajna.getSimCas(), osoba, id));
            predajna.setAutomatIsEmpty(true);
        } else {
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
        //
        predajna.setOsobaUAutomatu(null);
        if (!predajna.getOsobyQueue().isEmpty() && predajna.getObsluzneMiesta().zmestiSa(predajna.getAutomatIsEmpty())) {
            Osoba novaOsoba = predajna.getOsobyQueue().poll();
            //tu treba zaznamenat dlzku radu (pretoze sa meni velkost radu)
            //stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
            predajna.naplanujUdalost(new ZačiatokZadavaniaDoAutomatu(predajna, predajna.getSimCas(), novaOsoba));
            predajna.setAutomatIsEmpty(false);
        }
        predajna.setStavyOsob(osoba.toArray());
    }
}
