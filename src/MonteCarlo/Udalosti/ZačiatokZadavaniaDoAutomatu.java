package MonteCarlo.Udalosti;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Osoby.StavyOsoby;
import MonteCarlo.Stanok;
import MonteCarlo.UdalostnaSimulacia;

public class ZačiatokZadavaniaDoAutomatu extends Udalost {
    private Osoba osoba;
    public ZačiatokZadavaniaDoAutomatu(UdalostnaSimulacia jadro, double casUdalosti, Osoba osoba) {

        super(jadro, casUdalosti);
        this.osoba = osoba;
    }

    @Override
    public void execute() {
        Stanok stanok = (Stanok)jadro;
        stanok.setAutomatIsEmpty(false);
        stanok.getPriemerDlzkaRadu().pridajZaznam(stanok.getOsobyQueue().size(), stanok.getSimCas());
        osoba.setStav(StavyOsoby.ZADAVANIE_DO_AUTOMATU);
        stanok.setOsobaUAutomatu(osoba);
        stanok.naplanujUdalost(new KoniecZdavaniaDoAutomatu(stanok, stanok.getSimCas()+stanok.getNahodnyJav().getCasZadavaniaDoAutomatu(), osoba));
    }
}
