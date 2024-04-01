package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.SimJadro;
import MonteCarlo.Stanok;
import MonteCarlo.UdalostnaSimulacia;

public class KoniecObsluhy extends Udalost{
    Osoba osoba;
    public KoniecObsluhy(UdalostnaSimulacia jadro,double casUdalosti, Osoba osoba) {
        super(jadro, casUdalosti);
        this.osoba = osoba;

    }

    @Override
    public void execute() {
        Stanok stanok = (Stanok)jadro;
        //tu treba zaznamenat cas v obchode do statistiky
        osoba.setStav(StavyOsoby.ODCHADZA);
        stanok.getPriemerCasVObchode().pridajZaznam(stanok.getSimCas() - osoba.getCasPrichodu());

        //
        if (!stanok.getOsobyQueue().isEmpty()) {
            Osoba novaOsoba = stanok.getOsobyQueue().poll();
            //tu treba zaznamenat dlzku radu (pretoze sa meni velkost radu)
            stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
            stanok.naplanujUdalost(new ZačiatokObsluhy(stanok, stanok.getSimCas(), novaOsoba));

        }
        stanok.setPokladnaIsEmpty(true);
    }
}
