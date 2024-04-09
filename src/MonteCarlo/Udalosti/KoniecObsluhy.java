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
        osoba.setStav(StavyOsoby.V_RADE_PRED_POKLADNOU);
        int idPokladneNaZaradenie = predajna.getPokladne().getIDPokladne(predajna.getNahodnyJav().getNahodnePostavenieDoPokladne());
        boolean[] obsluzneDanehoTypu;
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            obsluzneDanehoTypu = predajna.getObsluzneMiesta().getOnlineObsluzne();
        } else {
            obsluzneDanehoTypu = predajna.getObsluzneMiesta().getNormalneObsluzne();
        }
        obsluzneDanehoTypu[osoba.getIdObsluzneho()] = !osoba.isNechalTovarNaVydajni();
        if (idPokladneNaZaradenie != -1) {
            //nasla sa volna pokladna tak zarad osobu do tej pokladne
            predajna.naplanujUdalost(new ZačiatokPlatenia(predajna, predajna.getSimCas(), osoba, idPokladneNaZaradenie));
        } else {
            //zarad osobu do najratsieho radu
            int idRaduNaZaradenie = predajna.getPokladne().getIDNajmensiehoRadu(predajna.getNahodnyJav().getNahodnePostavanieDoRadu());
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
        //naplanuj novu obsluhu ak nie je rad prazdny
        if (obsluzneDanehoTypu[osoba.getIdObsluzneho()] && !queue.isEmpty()) {
            Osoba novaOsoba = queue.poll();
            int id = predajna.getObsluzneMiesta().getIDVolnaPokladna(novaOsoba);
            if (id != -1) {
                //pokladna je volna
                predajna.naplanujUdalost(new ZačiatokObsluhy(predajna, predajna.getSimCas(), novaOsoba, id));
            }

        }
        predajna.setStavyOsob(osoba.toArray());
    }
}
