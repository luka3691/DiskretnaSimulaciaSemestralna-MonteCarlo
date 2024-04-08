package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Osoby.TypZakaznika;
import MonteCarlo.Predajna;
import MonteCarlo.UdalostnaSimulacia;

import java.util.Queue;

public class KoniecObsluhy extends Udalost{
    Osoba osoba;
    public KoniecObsluhy(UdalostnaSimulacia jadro,double casUdalosti, Osoba osoba) {
        super(jadro, casUdalosti);
        this.osoba = osoba;

    }

    @Override
    public void execute() {
        Predajna predajna = (Predajna)jadro;
        //tu treba zaznamenat cas v obchode do statistiky
        osoba.setStav(StavyOsoby.V_RADE_PRED_POKLADNOU);
        //stanok.getPriemerCasVObchode().pridajZaznam(stanok.getSimCas() - osoba.getCasPrichodu());
        int idPokladneNaZaradenie = predajna.getPokladne().getIDPokladne(predajna.getNahodnyJav().getNahodnePostavenieDoPokladne());
        boolean[] obsluzneDanehoTypu;
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            obsluzneDanehoTypu = predajna.getObsluzneMiesta().getOnlineObsluzne();
        } else {
            obsluzneDanehoTypu = predajna.getObsluzneMiesta().getNormalneObsluzne();
        }
        obsluzneDanehoTypu[osoba.getIdObsluzneho()] = !osoba.isNechalTovarNaVydajni();
        if (idPokladneNaZaradenie != -1) {
            //pokladna je volna
            //stanok.getPokladne().getPokladne()[idPokladneNaZaradenie] = false;
            predajna.naplanujUdalost(new ZačiatokPlatenia(predajna, predajna.getSimCas(), osoba, idPokladneNaZaradenie));
        } else {
            //zaradenie do najkratsieho radu
            int idRaduNaZaradenie = predajna.getPokladne().getIDNajmensiehoRadu(predajna.getNahodnyJav().getNahodnePostavanieDoRadu());
            //stanok.getPriemerDlzkaRadovPriPokladniach().get(idRaduNaZaradenie).pridajZaznam(stanok.getPokladne().getRady()[idRaduNaZaradenie].size(), stanok.getSimCas());
            predajna.getPokladne().getRady()[idRaduNaZaradenie].add(osoba);
            predajna.getPriemerDlzkaRadovPriPokladniach().get(idRaduNaZaradenie).pridajZaznam(predajna.getPokladne().getRady()[idRaduNaZaradenie].size(), predajna.getSimCas());
            osoba.setIdPokladne(idRaduNaZaradenie);
        }
        Queue<Osoba> queue;
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            queue = predajna.getObsluzneMiesta().getOnlineQueue();
        } else {
            queue = predajna.getObsluzneMiesta().getOsobyQueue();
        }
        if (obsluzneDanehoTypu[osoba.getIdObsluzneho()] && !queue.isEmpty()) {
            Osoba novaOsoba = queue.poll();
            //tu treba zaznamenat dlzku radu (pretoze sa meni velkost radu)
            //stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
            int id = predajna.getObsluzneMiesta().getIDVolnaPokladna(novaOsoba);
            if (id != -1) {
                predajna.naplanujUdalost(new ZačiatokObsluhy(predajna, predajna.getSimCas(), novaOsoba, id));
            }

        }
        predajna.setStavyOsob(osoba.toArray());
        //stanok.setAutomatIsEmpty(true);
    }
}
