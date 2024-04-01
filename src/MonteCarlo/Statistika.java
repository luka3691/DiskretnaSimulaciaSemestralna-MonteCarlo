package MonteCarlo;

import java.io.Console;

public class Statistika {
    //potrebana dlzka radu, cas v celom obchode, pocet ludi
    private int pocetZaznamov;
    private double celkovaSumaZaznamov;
    private double celkovaSumaVazenychZaznamov;

    private final boolean isWeighted;

    public Statistika(boolean isWeighted) {
        this.isWeighted = isWeighted;
        this.pocetZaznamov = 0;
        this.celkovaSumaZaznamov = 0;
        this.celkovaSumaVazenychZaznamov = 0;
    }

    public void pridajZaznam(double hodnota) {
        celkovaSumaZaznamov += hodnota;
        pocetZaznamov++;
    }

    public void pridajZaznam(double hodnota, double vaha) {
        celkovaSumaZaznamov += vaha;
        pocetZaznamov++;
        celkovaSumaVazenychZaznamov += hodnota * vaha;
    }
    public double vypocitaj() {
        if (isWeighted) {
            if (celkovaSumaZaznamov == 0) {
                return 0.0;
            }

            return celkovaSumaVazenychZaznamov / celkovaSumaZaznamov;
        } else {
            if (pocetZaznamov == 0){
                //throw
                System.out.println("Nevazeny error");
            }
            return celkovaSumaZaznamov / pocetZaznamov;
        }

    }
}
