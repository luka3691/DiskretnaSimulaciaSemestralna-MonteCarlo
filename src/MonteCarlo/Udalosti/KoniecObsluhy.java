package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Osoby.TypZakaznika;
import MonteCarlo.Stanok;
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
        Stanok stanok = (Stanok)jadro;
        //tu treba zaznamenat cas v obchode do statistiky
        osoba.setStav(StavyOsoby.V_RADE_PRED_POKLADNOU);
        //stanok.getPriemerCasVObchode().pridajZaznam(stanok.getSimCas() - osoba.getCasPrichodu());
        int idPokladneNaZaradenie = stanok.getPokladne().getIDPokladne(stanok.getNahodnyJav().getNahodnePostavenieDoPokladne());
        boolean[] obsluzneDanehoTypu;
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            obsluzneDanehoTypu = stanok.getObsluzneMiesta().getOnlineObsluzne();
        } else {
            obsluzneDanehoTypu = stanok.getObsluzneMiesta().getNormalneObsluzne();
        }
        obsluzneDanehoTypu[osoba.getIdObsluzneho()] = !osoba.isNechalTovarNaVydajni();
        if (idPokladneNaZaradenie != -1) {
            //pokladna je volna
            //stanok.getPokladne().getPokladne()[idPokladneNaZaradenie] = false;
            stanok.naplanujUdalost(new ZačiatokPlatenia(stanok, stanok.getSimCas(), osoba, idPokladneNaZaradenie));
        } else {
            //zaradenie do najkratsieho radu
            int idRaduNaZaradenie = stanok.getPokladne().getIDNajmensiehoRadu(stanok.getNahodnyJav().getNahodnePostavanieDoRadu());
            stanok.getPokladne().getRady()[idRaduNaZaradenie].add(osoba);
        }
        Queue<Osoba> queue;
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            queue = stanok.getObsluzneMiesta().getOnlineQueue();
        } else {
            queue = stanok.getObsluzneMiesta().getOsobyQueue();
        }
        if (obsluzneDanehoTypu[osoba.getIdObsluzneho()] && !queue.isEmpty()) {
            Osoba novaOsoba = queue.poll();
            //tu treba zaznamenat dlzku radu (pretoze sa meni velkost radu)
            //stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
            int id = stanok.getObsluzneMiesta().getIDVolnaPokladna(novaOsoba);
            if (id != -1) {
                stanok.naplanujUdalost(new ZačiatokObsluhy(stanok, stanok.getSimCas(), novaOsoba, id));
            }

        }
        //stanok.setAutomatIsEmpty(true);
    }
}
