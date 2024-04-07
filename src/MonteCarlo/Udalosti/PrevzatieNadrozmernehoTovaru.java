package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Osoby.TypZakaznika;
import MonteCarlo.Stanok;
import MonteCarlo.UdalostnaSimulacia;

import java.util.Queue;

public class PrevzatieNadrozmernehoTovaru extends Udalost{
    Osoba osoba;
    public PrevzatieNadrozmernehoTovaru(UdalostnaSimulacia jadro, double casUdalosti, Osoba osoba) {
        super(jadro, casUdalosti);
        this.osoba = osoba;

    }

    @Override
    public void execute() {
        Stanok stanok = (Stanok)jadro;
        //tu treba zaznamenat cas v obchode do statistiky
        Queue<Osoba> queue;
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            stanok.getObsluzneMiesta().getOnlineObsluzne()[osoba.getIdObsluzneho()] = true;
            queue = stanok.getObsluzneMiesta().getOnlineQueue();
        } else {
            stanok.getObsluzneMiesta().getNormalneObsluzne()[osoba.getIdObsluzneho()] = true;
            queue = stanok.getObsluzneMiesta().getOsobyQueue();
        }

        stanok.getPriemerCasVObchode().pridajZaznam(stanok.getSimCas() - osoba.getCasPrichodu());

        if (!queue.isEmpty()) {
            Osoba novaOsoba = queue.poll();
            //tu treba zaznamenat dlzku radu (pretoze sa meni velkost radu)
            //stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
            int id = stanok.getObsluzneMiesta().getIDVolnaPokladna(novaOsoba);
            if (id != -1) {
                stanok.naplanujUdalost(new ZačiatokObsluhy(stanok, stanok.getSimCas(), novaOsoba, id));
            }
        }


        osoba.setStav(StavyOsoby.ODCHADZA);
        stanok.setStavyOsob(osoba.toArray());
        //stanok.getPriemerCasVObchode().pridajZaznam(stanok.getSimCas() - osoba.getCasPrichodu());

    }
}
