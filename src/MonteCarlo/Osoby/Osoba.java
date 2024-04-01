package MonteCarlo.Osoby;

public class Osoba {
    private double casPrichodu;
    private StavyOsoby stav;
    private int ID;
    private boolean nechalTovarNaVydajni = false;

    private TypZakaznika typZakaznika;

    private int idPokladne;
    private int idObsluzneho;

    private boolean nadrozmernaObjednavka;


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

    public void setNechalTovarNaVydajni(boolean nechalTovarNaVydajni) {
        this.nechalTovarNaVydajni = nechalTovarNaVydajni;
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

    public boolean isNadrozmernaObjednavka() {
        return nadrozmernaObjednavka;
    }

    public void setNadrozmernaObjednavka(boolean nadrozmernaObjednavka) {
        this.nadrozmernaObjednavka = nadrozmernaObjednavka;
    }


}
