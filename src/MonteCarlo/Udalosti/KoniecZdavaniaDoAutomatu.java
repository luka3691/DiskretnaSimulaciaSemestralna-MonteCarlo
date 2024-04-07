package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Stanok;
import MonteCarlo.UdalostnaSimulacia;

public class KoniecZdavaniaDoAutomatu extends Udalost{
    Osoba osoba;
    public KoniecZdavaniaDoAutomatu(UdalostnaSimulacia jadro, double casUdalosti, Osoba osoba) {
        super(jadro, casUdalosti);
        this.osoba = osoba;

    }

    @Override
    public void execute() {
        Stanok stanok = (Stanok)jadro;

        //tu treba zaznamenat cas v obchode do statistiky
        //osoba.setStav(StavyOsoby.ODCHADZA);
        //stanok.getPriemerCasVObchode().pridajZaznam(stanok.getSimCas() - osoba.getCasPrichodu());

        //


        //tu treba zaznamenat dlzku radu (pretoze sa meni velkost radu)
        //stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
        osoba.setStav(StavyOsoby.KONIEC_ZADAVANIA_DO_AUTOMATU);
        int id = stanok.getObsluzneMiesta().getIDVolnaPokladna(osoba);
        if (id != -1) {
            stanok.naplanujUdalost(new ZačiatokObsluhy(stanok, stanok.getSimCas(), osoba, id));
            stanok.setAutomatIsEmpty(true);
        } else {
            stanok.getObsluzneMiesta().zaradDoRadu(osoba);
            if (stanok.getObsluzneMiesta().zmestiSa(stanok.getAutomatIsEmpty())) {
                stanok.setAutomatIsEmpty(true);
            }
            osoba.setStav(StavyOsoby.V_RADE_PRED_OSBLUHOU);
        };
        //
        stanok.setOsobaUAutomatu(null);
        if (!stanok.getOsobyQueue().isEmpty() && stanok.getObsluzneMiesta().zmestiSa(stanok.getAutomatIsEmpty())) {
            Osoba novaOsoba = stanok.getOsobyQueue().poll();
            //tu treba zaznamenat dlzku radu (pretoze sa meni velkost radu)
            //stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
            stanok.naplanujUdalost(new ZačiatokZadavaniaDoAutomatu(stanok, stanok.getSimCas(), novaOsoba));
            stanok.setAutomatIsEmpty(false);
        }
        stanok.setStavyOsob(osoba.toArray());
    }
}
