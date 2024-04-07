package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Osoby.TypZakaznika;
import MonteCarlo.Stanok;
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

        Stanok stanok = (Stanok)jadro;
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            stanok.getObsluzneMiesta().getOnlineObsluzne()[IDPokladne] = false;
        } else {
            stanok.getObsluzneMiesta().getNormalneObsluzne()[IDPokladne] = false;
        }
        osoba.setIdObsluzneho(IDPokladne);
        osoba.setStav(StavyOsoby.JE_OBSLUHOVANY);
        this.osoba.setNadrozmernaObjednavka(stanok.getNahodnyJav().getNechaTovarNaObsluznom());
        if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
            stanok.naplanujUdalost(new KoniecObsluhy(stanok, stanok.getSimCas() + stanok.getNahodnyJav().getCasNaPripravenieOnline(), osoba));
        } else {
            stanok.naplanujUdalost(new KoniecObsluhy(stanok, stanok.getSimCas() + stanok.getNahodnyJav().getCasNaNadiktovanieObjednavky() + stanok.getNahodnyJav().getTravnieObjednavky(), osoba));
        }
        if (!stanok.getOsobyQueue().isEmpty() && stanok.getObsluzneMiesta().zmestiSa(stanok.getAutomatIsEmpty()) && stanok.getAutomatIsEmpty()) {
            Osoba novaOsoba = stanok.getOsobyQueue().poll();
            //tu treba zaznamenat dlzku radu (pretoze sa meni velkost radu)
            //stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
            stanok.naplanujUdalost(new ZačiatokZadavaniaDoAutomatu(stanok, stanok.getSimCas(), novaOsoba));

        }
    }
}
