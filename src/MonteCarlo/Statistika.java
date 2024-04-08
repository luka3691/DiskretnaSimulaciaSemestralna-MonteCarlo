package MonteCarlo;

import java.util.ArrayList;

public class Statistika {
    //potrebana dlzka radu, cas v celom obchode, pocet ludi
    private int pocetZaznamov;
    private double maxVaha;
    private double maxVahaSquared;
    private double celkovaSumaVazenychZaznamov;

    private final boolean isWeighted;

    public Statistika(boolean isWeighted) {
        this.isWeighted = isWeighted;
        this.pocetZaznamov = 0;
        this.maxVaha = 0;
        this.maxVahaSquared = 0;
        this.celkovaSumaVazenychZaznamov = 0;
    }

    public void pridajZaznam(double hodnota) {
        maxVaha += hodnota;
        maxVahaSquared += Math.pow(hodnota, 2);
        pocetZaznamov++;
    }

    public void pridajZaznam(double hodnota, double vaha) {
        if (maxVaha < vaha) {
            maxVaha = vaha;
        }
        pocetZaznamov++;
        celkovaSumaVazenychZaznamov += hodnota * (vaha - 60*9);
    }
    public double vypocitaj() {
        if (isWeighted) {
            if (maxVaha == 0) {
                return 0.0;
            }
            return celkovaSumaVazenychZaznamov / maxVaha ;
        } else {
            if (pocetZaznamov == 0) {
                //throw
                System.out.println("Nevazeny error");
            }
            return maxVaha / pocetZaznamov;
        }

    }

    public double[] getIntervalSpolahlivosti() {
        double s = Math.sqrt((maxVahaSquared - (Math.pow(maxVaha,2)/pocetZaznamov))/(pocetZaznamov-1));
        double priemer = vypocitaj();
        double rozdiel = (s * 1.96) / Math.sqrt(pocetZaznamov);
        return new double[]{priemer - rozdiel, priemer + rozdiel};
    }

    public double getVytazenie(double trvanieSim) {
        return maxVaha / trvanieSim;
    }

}
