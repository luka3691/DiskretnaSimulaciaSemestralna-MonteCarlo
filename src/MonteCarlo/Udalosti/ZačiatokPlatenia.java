package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Osoby.TypZakaznika;
import MonteCarlo.Stanok;
import MonteCarlo.UdalostnaSimulacia;

public class ZačiatokPlatenia extends Udalost {
    private Osoba osoba;
    private int IDPokladne;
    public ZačiatokPlatenia(UdalostnaSimulacia jadro, double casUdalosti, Osoba osoba, int id) {

        super(jadro, casUdalosti);
        this.osoba = osoba;
        this.osoba.setIdPokladne(id);
        this.IDPokladne = id;

    }

    @Override
    public void execute() {

        Stanok stanok = (Stanok)jadro;
        stanok.getPokladne().getPokladne()[osoba.getIdPokladne()] = false;
        osoba.setStav(StavyOsoby.JE_OBSLUHOVANY_V_POKLADNI);
        double trvanieUdalosti = stanok.getNahodnyJav().getTravniePlatby();
        stanok.naplanujUdalost(new KoniecPlatenia(jadro, stanok.getSimCas() + trvanieUdalosti, osoba));
        stanok.getPriemerVytazenostPokladni().get(osoba.getIdPokladne()).pridajZaznam(trvanieUdalosti);
        stanok.setStavyOsob(osoba.toArray());
    }
}
