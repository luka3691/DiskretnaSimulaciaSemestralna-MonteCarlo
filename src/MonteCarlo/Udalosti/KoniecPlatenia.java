package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Osoby.TypZakaznika;
import MonteCarlo.Stanok;
import MonteCarlo.UdalostnaSimulacia;

public class KoniecPlatenia extends Udalost{
    Osoba osoba;
    public KoniecPlatenia(UdalostnaSimulacia jadro, double casUdalosti, Osoba osoba) {
        super(jadro, casUdalosti);
        this.osoba = osoba;

    }

    @Override
    public void execute() {
        Stanok stanok = (Stanok)jadro;
        //tu treba zaznamenat cas v obchode do statistiky
        stanok.getPokladne().getPokladne()[osoba.getIdPokladne()] = true;
        if (osoba.isNechalTovarNaVydajni()) {
            osoba.setStav(StavyOsoby.IDE_SI_PRE_NADROZMERNY_TOVAR);
            stanok.naplanujUdalost(new PrevzatieNadrozmernehoTovaru(stanok, stanok.getSimCas() + stanok.getNahodnyJav().getSpatnePrevzatieTovaru(), osoba));
        } else {
            osoba.setStav(StavyOsoby.ODCHADZA);
        }
    }
}
