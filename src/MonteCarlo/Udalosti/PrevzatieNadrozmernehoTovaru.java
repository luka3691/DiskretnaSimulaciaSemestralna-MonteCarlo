package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Osoby.TypZakaznika;
import MonteCarlo.Predajna;
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
        Predajna predajna = (Predajna)jadro;
        //tu treba zaznamenat cas v obchode do statistiky
        Queue<Osoba> queue;
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            predajna.getObsluzneMiesta().getOnlineObsluzne()[osoba.getIdObsluzneho()] = true;
            queue = predajna.getObsluzneMiesta().getOnlineQueue();
        } else {
            predajna.getObsluzneMiesta().getNormalneObsluzne()[osoba.getIdObsluzneho()] = true;
            queue = predajna.getObsluzneMiesta().getOsobyQueue();
        }

        predajna.getPriemerCasVObchode().pridajZaznam(predajna.getSimCas() - osoba.getCasPrichodu());

        if (!queue.isEmpty()) {
            Osoba novaOsoba = queue.poll();
            //tu treba zaznamenat dlzku radu (pretoze sa meni velkost radu)
            //stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
            int id = predajna.getObsluzneMiesta().getIDVolnaPokladna(novaOsoba);
            if (id != -1) {
                predajna.naplanujUdalost(new ZačiatokObsluhy(predajna, predajna.getSimCas(), novaOsoba, id));
            }
        }


        osoba.setStav(StavyOsoby.ODCHADZA);
        predajna.setStavyOsob(osoba.toArray());
        int pocetObsluzenych = predajna.getPocetObsluzenychZakaznikov() + 1;
        predajna.setPocetObsluzenychZakaznikov(pocetObsluzenych);
        //stanok.getPriemerCasVObchode().pridajZaznam(stanok.getSimCas() - osoba.getCasPrichodu());

    }
}
