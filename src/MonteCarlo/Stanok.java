package MonteCarlo;

import MonteCarlo.Rozdelenia.Deterministicke;
import MonteCarlo.Rozdelenia.Exponencialne;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Udalosti.PrichodZakaznika;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Stanok extends UdalostnaSimulacia{
    private Queue<Osoba> osobyQueue;
    private int personIndex;
    private double cas; // v sekundach

    private Deterministicke obsluha;
    private Exponencialne prichodLudi;
    //private Statistika priemerPocetLudi;
    private Statistika priemerCasVObchode;
    private Statistika priemerDlzkaRadu;
    private Statistika priemerPocetLudiCelkovy;
    private Statistika priemerCasVObchodeCelkovy;
    private Statistika priemerDlzkaRaduCelkovy;

    private boolean pokladnaIsEmpty;

    private double zaciatokCasu;
    private double koniecCasu;

    public Stanok(int numberOfReplications) {
        super(numberOfReplications);
        this.osobyQueue = new LinkedList<>();
        this.zaciatokCasu = 0;
        this.koniecCasu = 8*60;
    }

    @Override
    void beforeReps() {
        obsluha = new Deterministicke(4);
        prichodLudi = new Exponencialne(new Random(), (double)60/12);
        priemerPocetLudiCelkovy = new Statistika(false);
        priemerCasVObchodeCelkovy = new Statistika(false);
        priemerDlzkaRaduCelkovy = new Statistika(false);


    }

    @Override
    void beforeRep() {
        simCas = 0.0;
        this.personIndex = 0;
        priemerCasVObchode = new Statistika(false);
        priemerDlzkaRadu = new Statistika(true);
        pokladnaIsEmpty = true;
        udalostiQueue.add(new PrichodZakaznika(this, simCas + prichodLudi.sample()));
    }


    @Override
    void afterRep() {
        osobyQueue.clear();
        udalostiQueue.clear();

        priemerPocetLudiCelkovy.pridajZaznam(personIndex);
        priemerDlzkaRaduCelkovy.pridajZaznam(priemerDlzkaRadu.vypocitaj());
        priemerCasVObchodeCelkovy.pridajZaznam(priemerCasVObchode.vypocitaj());

    }
    @Override
    void afterReps() {
        System.out.println("Primer pocet ludi: " + priemerPocetLudiCelkovy.vypocitaj());
        System.out.println("Primer dlzka radu: " + priemerDlzkaRaduCelkovy.vypocitaj());
        System.out.println("Primer cas v obchode: " + priemerCasVObchodeCelkovy.vypocitaj());
    }

    public int getNewPersonIndex() {
        this.personIndex++;
        return this.personIndex;
    }

    public int getVolnaPokladna() {
        if (pokladnaIsEmpty) {
            return 1;
        } else {
            return 0;
        }
    }

    public Statistika getPriemerCasVObchode() {
        return priemerCasVObchode;
    }

    public Statistika getPriemerDlzkaRadu() {
        return priemerDlzkaRadu;
    }

    public Queue<Osoba> getOsobyQueue() {
        return osobyQueue;
    }

    public Deterministicke getObsluha() {
        return obsluha;
    }

    public Exponencialne getPrichodLudi() {
        return prichodLudi;
    }

    public double getKoniecCasu() {
        return koniecCasu;
    }

    public void setPokladnaIsEmpty(boolean pokladnaIsEmpty) {
        this.pokladnaIsEmpty = pokladnaIsEmpty;
    }
}
