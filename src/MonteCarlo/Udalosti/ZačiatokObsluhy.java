package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Osoby.TypZakaznika;
import MonteCarlo.Predajna;
import MonteCarlo.UdalostnaSimulacia;

public class ZačiatokObsluhy extends Udalost {
    private Osoba osoba;
    private int IDPokladne;
    public ZačiatokObsluhy(UdalostnaSimulacia jadro, double casUdalosti, Osoba osoba, int id) {

        super(jadro, casUdalosti);
        this.osoba = osoba;
        this.osoba.setIdPokladne(id);
        this.IDPokladne = id;

    }

    @Override
    public void execute() {

        Predajna predajna = (Predajna)jadro;
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            predajna.getObsluzneMiesta().getOnlineObsluzne()[IDPokladne] = false;
        } else {
            predajna.getObsluzneMiesta().getNormalneObsluzne()[IDPokladne] = false;
        }
        osoba.setIdObsluzneho(IDPokladne);
        osoba.setStav(StavyOsoby.JE_OBSLUHOVANY);
        this.osoba.setNadrozmernaObjednavka(predajna.getNahodnyJav().getNechaTovarNaObsluznom());
        //naplanuj koniec obsluhy
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            double trvanieUdalosti = predajna.getNahodnyJav().getCasNaPripravenieOnline();
            predajna.naplanujUdalost(new KoniecObsluhy(predajna, predajna.getSimCas() +trvanieUdalosti, osoba));
            predajna.getPriemerVytazenostObsluznychOnline().get(IDPokladne).pridajZaznam(trvanieUdalosti);
        } else {
            double trvanieUdalosti = predajna.getNahodnyJav().getCasNaNadiktovanieObjednavky() + predajna.getNahodnyJav().getTravnieObjednavky();
            predajna.naplanujUdalost(new KoniecObsluhy(predajna, predajna.getSimCas() + trvanieUdalosti, osoba));
            predajna.getPriemerVytazenostObsluznychOstatne().get(IDPokladne).pridajZaznam(trvanieUdalosti);
        }
        //ak rad pred automatom  nie je prazdny a zmesti sa do radu pred obsluzne miesta a automat je aktualne prazdny, tak naplanuj zadavanie do automatu
        if (!predajna.getOsobyQueue().isEmpty() && predajna.getObsluzneMiesta().zmestiSa(predajna.getAutomatIsEmpty()) && predajna.getAutomatIsEmpty()) {
            Osoba novaOsoba = predajna.getOsobyQueue().poll();
            predajna.naplanujUdalost(new ZačiatokZadavaniaDoAutomatu(predajna, predajna.getSimCas(), novaOsoba));
        }
        predajna.setStavyOsob(osoba.toArray());
    }
}
