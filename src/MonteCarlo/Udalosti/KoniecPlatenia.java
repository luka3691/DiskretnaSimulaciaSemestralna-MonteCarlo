package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Predajna;
import MonteCarlo.UdalostnaSimulacia;

public class KoniecPlatenia extends Udalost{
    Osoba osoba;
    public KoniecPlatenia(UdalostnaSimulacia jadro, double casUdalosti, Osoba osoba) {
        super(jadro, casUdalosti);
        this.osoba = osoba;

    }

    @Override
    public void execute() {
        Predajna predajna = (Predajna)jadro;
        //tu treba zaznamenat cas v obchode do statistiky
        predajna.getPokladne().getPokladne()[osoba.getIdPokladne()] = true;
        if (osoba.isNechalTovarNaVydajni()) {
            osoba.setStav(StavyOsoby.IDE_SI_PRE_NADROZMERNY_TOVAR);
            predajna.naplanujUdalost(new PrevzatieNadrozmernehoTovaru(predajna, predajna.getSimCas() + predajna.getNahodnyJav().getSpatnePrevzatieTovaru(), osoba));
        } else {
            osoba.setStav(StavyOsoby.ODCHADZA);
            predajna.getPriemerCasVObchode().pridajZaznam(predajna.getSimCas() - osoba.getCasPrichodu());
            int pocetObsluzenych = predajna.getPocetObsluzenychZakaznikov() + 1;
            predajna.setPocetObsluzenychZakaznikov(pocetObsluzenych);
        }
        if (!predajna.getPokladne().getRady()[osoba.getIdPokladne()].isEmpty()) {
            //stanok.getPriemerDlzkaRadovPriPokladniach().get(osoba.getIdPokladne()).pridajZaznam(stanok.getPokladne().getRady()[osoba.getIdPokladne()].size(), stanok.getSimCas());
            Osoba osobaNova = predajna.getPokladne().getRady()[osoba.getIdPokladne()].poll();
            predajna.getPriemerDlzkaRadovPriPokladniach().get(osoba.getIdPokladne()).pridajZaznam(predajna.getPokladne().getRady()[osoba.getIdPokladne()].size(), predajna.getSimCas());
            predajna.naplanujUdalost(new ZačiatokPlatenia(predajna, predajna.getSimCas(), osobaNova, osoba.getIdPokladne()));
        }
        predajna.setStavyOsob(osoba.toArray());
    }
}
