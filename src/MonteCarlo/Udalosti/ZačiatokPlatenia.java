package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Predajna;
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

        Predajna predajna = (Predajna)jadro;
        predajna.getPokladne().getPokladne()[osoba.getIdPokladne()] = false;
        osoba.setStav(StavyOsoby.JE_OBSLUHOVANY_V_POKLADNI);
        double trvanieUdalosti = predajna.getNahodnyJav().getTravniePlatby();
        //naplanuj koniec platenia
        predajna.naplanujUdalost(new KoniecPlatenia(jadro, predajna.getSimCas() + trvanieUdalosti, osoba));
        predajna.getPriemerVytazenostPokladni().get(osoba.getIdPokladne()).pridajZaznam(trvanieUdalosti);
        predajna.setStavyOsob(osoba.toArray());
    }
}
