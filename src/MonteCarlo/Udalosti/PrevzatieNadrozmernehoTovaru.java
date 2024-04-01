package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Osoby.TypZakaznika;
import MonteCarlo.Stanok;
import MonteCarlo.UdalostnaSimulacia;

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
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            stanok.getObsluzneMiesta().getOnlineObsluzne()[osoba.getIdObsluzneho()] = true;
        } else {
            stanok.getObsluzneMiesta().getNormalneObsluzne()[osoba.getIdObsluzneho()] = true;
        }

        osoba.setStav(StavyOsoby.ODCHADZA);
        //stanok.getPriemerCasVObchode().pridajZaznam(stanok.getSimCas() - osoba.getCasPrichodu());

    }
}
