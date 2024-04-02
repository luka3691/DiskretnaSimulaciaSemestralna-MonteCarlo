package MonteCarlo.Osoby;

public class Osoba {
    private double casPrichodu;
    private StavyOsoby stav;
    private int ID;
    private boolean nechalTovarNaVydajni = false;

    private TypZakaznika typZakaznika;

    private int idPokladne;
    private int idObsluzneho;



    public Osoba(StavyOsoby stav, double casPrichodu, int ID, TypZakaznika typZakaznika) {
        this.stav = stav;
        this.casPrichodu = casPrichodu;
        this.ID = ID;
        this.typZakaznika = typZakaznika;
    }

    public void setStav(StavyOsoby stav) {
        this.stav = stav;
    }

    public double getCasPrichodu() {
        return casPrichodu;
    }

    public boolean isNechalTovarNaVydajni() {
        return nechalTovarNaVydajni;
    }


    public TypZakaznika getTypZakaznika() {
        return typZakaznika;
    }

    public void setTypZakaznika(TypZakaznika typZakaznika) {
        this.typZakaznika = typZakaznika;
    }

    public int getIdPokladne() {
        return idPokladne;
    }

    public void setIdPokladne(int idPokladne) {
        this.idPokladne = idPokladne;
    }

    public int getIdObsluzneho() {
        return idObsluzneho;
    }

    public void setIdObsluzneho(int idObsluzneho) {
        this.idObsluzneho = idObsluzneho;
    }


    public void setNadrozmernaObjednavka(boolean nadrozmernaObjednavka) {
        this.nechalTovarNaVydajni = nadrozmernaObjednavka;
    }

    public int getID() {
        return ID;
    }
}
