package MonteCarlo.Osoby;

public class Osoba {
    private double casPrichodu;
    private StavyOsoby stav;
    private int ID;

    public Osoba(StavyOsoby stav, double casPrichodu, int ID) {
        this.stav = stav;
        this.casPrichodu = casPrichodu;
        this.ID = ID;
    }

    public void setStav(StavyOsoby stav) {
        this.stav = stav;
    }

    public double getCasPrichodu() {
        return casPrichodu;
    }
}
