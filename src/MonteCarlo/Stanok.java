package MonteCarlo;

import MonteCarlo.Osoby.OsobaComparatorNoPriority;
import MonteCarlo.Rozdelenia.Deterministicke;
import MonteCarlo.Rozdelenia.Exponencialne;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Rozdelenia.SpojiteRovnomerne;
import MonteCarlo.Udalosti.PrichodZakaznika;

import java.util.*;

public class Stanok extends UdalostnaSimulacia{
    private PriorityQueue<Osoba> osobyQueue;
    private int personIndex;
    private double cas; // v sekundach
    private ObsluzneMiesta obsluzneMiesta;

    private Statistika priemerCasVObchode;
    private Statistika priemerDlzkaRadu;
    private Statistika priemerPocetLudiCelkovy;
    private Statistika priemerCasVObchodeCelkovy;
    private Statistika priemerDlzkaRaduCelkovy;
    private Statistika priemerCakanieVRadePredAutomatomCalkovy;
    private Statistika priemerCakanieVRadePredAutomatom;
    private Statistika priemerCakanieVRadeCelkovy;
    private Statistika priemerCakanieVRade;
    private Statistika priemerPoslednyOdchod;
    private ArrayList<Statistika> priemerVytazenostPokladni;
    private ArrayList<Statistika> priemerVytazenostObsluznych;
    private ArrayList<Statistika> priemerDlzkaRadovPriPokladniach;
    private Statistika priemerVytazenieAutomatu;
    private Statistika priemerVytazenieAutomatuCelkove;
    private ArrayList<Statistika> priemerVytazenostPokladniCelkove;
    private ArrayList<Statistika> priemerVytazenostObsluznychCelkove;
    private ArrayList<Statistika> priemerDlzkaRadovPriPokladniachCelkove;
    private Osoba osobaUAutomatu = null;

    private boolean automatIsEmpty;

    private double zaciatokCasu;
    private double koniecCasu;
    private double poslednyCasOdchodu;
    private NahodneJavy nahodnyJav;
    private Pokladne pokladne;
    private int pocetObsluznych;
    private int pocetPokladni;
    private ArrayList<String> stavyOsob;

    public Stanok(int numberOfReplications, int pocetObsluznychMiest, int pocetPokladni) {
        super(numberOfReplications);
        this.pocetObsluznych = pocetObsluznychMiest;
        this.pocetPokladni = pocetPokladni;
        this.osobyQueue = new PriorityQueue<>(new OsobaComparatorNoPriority());
        this.zaciatokCasu = 9*60;
        this.koniecCasu = 17*60;
        obsluzneMiesta = new ObsluzneMiesta(pocetObsluznychMiest);
        pokladne = new Pokladne(pocetPokladni);

    }

    @Override
    void beforeReps() {

        nahodnyJav = new NahodneJavy();

        priemerPocetLudiCelkovy = new Statistika(false);
        priemerCasVObchodeCelkovy = new Statistika(false);
        priemerDlzkaRaduCelkovy = new Statistika(false);
        priemerCakanieVRadeCelkovy = new Statistika(false);
        priemerCakanieVRadePredAutomatomCalkovy = new Statistika(false);
        priemerPoslednyOdchod = new Statistika(false);
        priemerVytazenostObsluznychCelkove = new ArrayList<>();
        priemerVytazenostPokladniCelkove = new ArrayList<>();
        priemerDlzkaRadovPriPokladniachCelkove = new ArrayList<>();
        for (int i = 0; i < pocetPokladni; i++) {
            priemerVytazenostPokladniCelkove.add(new Statistika(false));
            priemerDlzkaRadovPriPokladniachCelkove.add(new Statistika(false));
        }
        for (int i = 0; i < pocetObsluznych; i++) {
            priemerVytazenostObsluznychCelkove.add(new Statistika(false));
        }

    priemerVytazenieAutomatuCelkove = new Statistika(false);
    }

    @Override
    void beforeRep() {
        simCas = 0.0;
        this.personIndex = 0;
        priemerCasVObchode = new Statistika(false);
        priemerDlzkaRadu = new Statistika(true);
        priemerCakanieVRade = new Statistika(false);
        priemerCakanieVRadePredAutomatom = new Statistika(false);
        automatIsEmpty = true;
        udalostiQueue.add(new PrichodZakaznika(this, zaciatokCasu + nahodnyJav.getPrichodLudi()));
        poslednyCasOdchodu = 0.0;
        stavyOsob = new ArrayList<>();
        priemerVytazenostObsluznych = new ArrayList<>();
        priemerVytazenostPokladni = new ArrayList<>();
        priemerDlzkaRadovPriPokladniach = new ArrayList<>();
        for (int i = 0; i < pocetPokladni; i++) {
            priemerVytazenostPokladni.add(new Statistika(false));
            priemerDlzkaRadovPriPokladniach.add(new Statistika(false));
        }
        for (int i = 0; i < pocetObsluznych; i++) {
            priemerVytazenostObsluznych.add(new Statistika(false));
        }
        priemerVytazenieAutomatu = new Statistika(false);
    }


    @Override
    void afterRep() {
        osobyQueue.clear();
        udalostiQueue.clear();

        priemerPocetLudiCelkovy.pridajZaznam(personIndex);
        priemerDlzkaRaduCelkovy.pridajZaznam(priemerDlzkaRadu.vypocitaj());
        priemerCasVObchodeCelkovy.pridajZaznam(priemerCasVObchode.vypocitaj());
        priemerCakanieVRadePredAutomatomCalkovy.pridajZaznam(priemerCakanieVRadePredAutomatom.vypocitaj());
        priemerPoslednyOdchod.pridajZaznam(simCas);
        for (int i = 0; i < pocetPokladni; i++) {
            priemerVytazenostPokladniCelkove.get(i).pridajZaznam(priemerVytazenostPokladni.get(i).vypocitaj());
            priemerDlzkaRadovPriPokladniachCelkove.get(i).pridajZaznam(priemerDlzkaRadovPriPokladniach.get(i).vypocitaj());
        }
        for (int i = 0; i < pocetObsluznych; i++) {
            priemerVytazenostObsluznychCelkove.get(i).pridajZaznam(priemerVytazenostObsluznych.get(i).vypocitaj());
        }
        priemerVytazenieAutomatuCelkove.pridajZaznam(priemerVytazenieAutomatu.vypocitaj());

    }
    @Override
    void afterReps() {
        System.out.println("Primer pocet ludi: " + priemerPocetLudiCelkovy.vypocitaj());
        System.out.println("Primer dlzka radu: " + priemerDlzkaRaduCelkovy.vypocitaj());
        System.out.println("Primer cas v obchode: " + priemerCasVObchodeCelkovy.vypocitaj());
        System.out.println("Priemer cakanie pred automatom: " + priemerCakanieVRadePredAutomatomCalkovy.vypocitaj());
        System.out.println("Posledny cas odchodu: " + priemerPoslednyOdchod.vypocitaj());
    }

    public int getNewPersonIndex() {
        this.personIndex++;
        return this.personIndex;
    }

    public boolean getAutomatIsEmpty() {
        return automatIsEmpty;
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

    public NahodneJavy getNahodnyJav() {
        return nahodnyJav;
    }

    public double getKoniecCasu() {
        return koniecCasu;
    }

    public void setAutomatIsEmpty(boolean automatIsEmpty) {
        this.automatIsEmpty = automatIsEmpty;
    }

    public ObsluzneMiesta getObsluzneMiesta() {
        return obsluzneMiesta;
    }

    public Pokladne getPokladne() {
        return pokladne;
    }

    public Osoba getOsobaUAutomatu() {
        return osobaUAutomatu;
    }

    public void setOsobaUAutomatu(Osoba osobaUAutomatu) {
        this.osobaUAutomatu = osobaUAutomatu;
    }

    public void setPoslednyCasOdchodu(double poslednyCasOdchodu) {
        this.poslednyCasOdchodu = poslednyCasOdchodu;
    }

    public Statistika getPriemerCakanieVRadePredAutomatom() {
        return priemerCakanieVRadePredAutomatom;
    }

    public int getPocetObsluznych() {
        return pocetObsluznych;
    }

    public int getPocetPokladni() {
        return pocetPokladni;
    }

    public Statistika getPriemerDlzkaRaduCelkovy() {
        return priemerDlzkaRaduCelkovy;
    }

    public void setStavyOsob(ArrayList<String> stavyOsob) {
        this.stavyOsob.clear();
        this.stavyOsob = stavyOsob;
    }

    public ArrayList<String> getStavyOsob() {
        return stavyOsob;
    }
}
