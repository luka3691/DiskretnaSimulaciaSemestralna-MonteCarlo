package MonteCarlo;

import MonteCarlo.Osoby.OsobaComparatorNoPriority;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Udalosti.PrichodZakaznika;

import java.util.*;

public class Stanok extends UdalostnaSimulacia{
    private PriorityQueue<Osoba> osobyQueue;
    private int personIndex;
    private ObsluzneMiesta obsluzneMiesta;

    private Statistika priemerCasVObchode;
    private Statistika priemerDlzkaRadu;
    private Statistika priemerPocetLudiCelkovy;
    private Statistika priemerCasVObchodeCelkovy;
    private Statistika priemerDlzkaRaduCelkovy;
    private Statistika priemerCakanieVRadePredAutomatomCalkovy;
    private Statistika priemerCakanieVRadePredAutomatom;


    private Statistika priemerPoslednyOdchod;
    private ArrayList<Statistika> priemerVytazenostPokladni;
    private ArrayList<Statistika> priemerVytazenostObsluznychOnline;
    private ArrayList<Statistika> priemerVytazenostObsluznychOstatne;
    private ArrayList<Statistika> priemerDlzkaRadovPriPokladniach;
    private Statistika priemerVytazenieAutomatu;
    private Statistika priemerVytazenieAutomatuCelkove;
    private ArrayList<Statistika> priemerVytazenostPokladniCelkove;
    private ArrayList<Statistika> priemerVytazenostObsluznychOnlineCelkove;
    private ArrayList<Statistika> priemerVytazenostObsluznychOstatneCelkove;
    private ArrayList<Statistika> priemerDlzkaRadovPriPokladniachCelkove;
    private Osoba osobaUAutomatu = null;

    private boolean automatIsEmpty;

    private double zaciatokCasu;
    private double koniecCasu;
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
        priemerDlzkaRaduCelkovy = new Statistika(true);
        priemerCakanieVRadePredAutomatomCalkovy = new Statistika(false);
        priemerPoslednyOdchod = new Statistika(false);
        priemerVytazenostObsluznychOnlineCelkove = new ArrayList<>();
        priemerVytazenostPokladniCelkove = new ArrayList<>();
        priemerDlzkaRadovPriPokladniachCelkove = new ArrayList<>();
        priemerVytazenostObsluznychOstatneCelkove = new ArrayList<>();

        for (int i = 0; i < pocetPokladni; i++) {
            priemerVytazenostPokladniCelkove.add(new Statistika(false));
            priemerDlzkaRadovPriPokladniachCelkove.add(new Statistika(true));
        }
        for (int i = 0; i < obsluzneMiesta.getOnlineObsluzne().length; i++) {
            priemerVytazenostObsluznychOnlineCelkove.add(new Statistika(false));
        }
        for (int i = 0; i < obsluzneMiesta.getNormalneObsluzne().length; i++) {
            priemerVytazenostObsluznychOstatneCelkove.add(new Statistika(false));
        }

    priemerVytazenieAutomatuCelkove = new Statistika(false);
    }

    @Override
    void beforeRep() {
        priemerCasVObchode = new Statistika(false);
        priemerDlzkaRadu = new Statistika(true);
        priemerCakanieVRadePredAutomatom = new Statistika(false);
        priemerVytazenieAutomatu = new Statistika(false);
        stavyOsob = new ArrayList<>();
        priemerVytazenostObsluznychOnline = new ArrayList<>();
        priemerVytazenostPokladni = new ArrayList<>();
        priemerDlzkaRadovPriPokladniach = new ArrayList<>();
        priemerVytazenostObsluznychOstatne = new ArrayList<>();
        for (int i = 0; i < pocetPokladni; i++) {
            priemerVytazenostPokladni.add(new Statistika(false));
            priemerDlzkaRadovPriPokladniach.add(new Statistika(true));
        }
        for (int i = 0; i < obsluzneMiesta.getOnlineObsluzne().length; i++) {
            priemerVytazenostObsluznychOnline.add(new Statistika(false));
        }
        for (int i = 0; i < obsluzneMiesta.getNormalneObsluzne().length; i++) {
            priemerVytazenostObsluznychOstatne.add(new Statistika(false));
        }
        simCas = 0.0;
        this.personIndex = 0;
        automatIsEmpty = true;
        udalostiQueue.add(new PrichodZakaznika(this, zaciatokCasu + nahodnyJav.getPrichodLudi()));

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
        priemerVytazenieAutomatuCelkove.pridajZaznam(priemerVytazenieAutomatu.getVytazenie(koniecCasu - zaciatokCasu));

        for (int i = 0; i < pocetPokladni; i++) {
            priemerVytazenostPokladniCelkove.get(i).pridajZaznam(priemerVytazenostPokladni.get(i).getVytazenie(simCas - zaciatokCasu));
            priemerDlzkaRadovPriPokladniachCelkove.get(i).pridajZaznam(priemerDlzkaRadovPriPokladniach.get(i).vypocitaj());
        }
        for (int i = 0; i < obsluzneMiesta.getOnlineObsluzne().length; i++) {
            priemerVytazenostObsluznychOnlineCelkove.get(i).pridajZaznam(priemerVytazenostObsluznychOnline.get(i).getVytazenie(simCas-zaciatokCasu));
        }
        for (int i = 0; i < obsluzneMiesta.getNormalneObsluzne().length; i++) {
            priemerVytazenostObsluznychOstatneCelkove.get(i).pridajZaznam(priemerVytazenostObsluznychOstatne.get(i).getVytazenie(simCas-zaciatokCasu));
        }

    }
    @Override
    void afterReps() {
        System.out.println("Primer pocet ludi: " + priemerPocetLudiCelkovy.vypocitaj());
        System.out.println("Primer dlzka radu: " + priemerDlzkaRaduCelkovy.vypocitaj());
        System.out.println("Primer cas v obchode: " + priemerCasVObchodeCelkovy.vypocitaj());
        System.out.println("Priemer cakanie pred automatom: " + priemerCakanieVRadePredAutomatomCalkovy.vypocitaj());
        System.out.println("Posledny cas odchodu: " + priemerPoslednyOdchod.vypocitaj());
        System.out.println("Vytazenie automatau:" + priemerVytazenieAutomatuCelkove.vypocitaj()*100);
        for (int i = 0; i < pocetPokladni; i++) {
            System.out.println(priemerVytazenostPokladniCelkove.get(i).vypocitaj()*100);
            priemerDlzkaRadovPriPokladniachCelkove.get(i).pridajZaznam(priemerDlzkaRadovPriPokladniach.get(i).vypocitaj());
        }
        for (int i = 0; i < obsluzneMiesta.getOnlineObsluzne().length; i++) {
            System.out.println(priemerVytazenostObsluznychOnlineCelkove.get(i).vypocitaj());
        }
        for (int i = 0; i < obsluzneMiesta.getNormalneObsluzne().length; i++) {
            System.out.println(priemerVytazenostObsluznychOstatneCelkove.get(i).vypocitaj());
        }
        System.out.println(Arrays.toString(priemerCasVObchodeCelkovy.getIntervalSpolahlivosti()));
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

    public Statistika getPriemerVytazenieAutomatu() {
        return priemerVytazenieAutomatu;
    }

    public ArrayList<Statistika> getPriemerVytazenostPokladni() {
        return priemerVytazenostPokladni;
    }

    public ArrayList<Statistika> getPriemerVytazenostObsluznychOnline() {
        return priemerVytazenostObsluznychOnline;
    }

    public ArrayList<Statistika> getPriemerVytazenostObsluznychOstatne() {
        return priemerVytazenostObsluznychOstatne;
    }

    public int getPersonIndex() {
        return personIndex;
    }

    public Statistika getPriemerPocetLudiCelkovy() {
        return priemerPocetLudiCelkovy;
    }

    public Statistika getPriemerCasVObchodeCelkovy() {
        return priemerCasVObchodeCelkovy;
    }

    public Statistika getPriemerCakanieVRadePredAutomatomCalkovy() {
        return priemerCakanieVRadePredAutomatomCalkovy;
    }

    public Statistika getPriemerPoslednyOdchod() {
        return priemerPoslednyOdchod;
    }

    public Statistika getPriemerVytazenieAutomatuCelkove() {
        return priemerVytazenieAutomatuCelkove;
    }

    public ArrayList<Statistika> getPriemerVytazenostPokladniCelkove() {
        return priemerVytazenostPokladniCelkove;
    }

    public ArrayList<Statistika> getPriemerVytazenostObsluznychOnlineCelkove() {
        return priemerVytazenostObsluznychOnlineCelkove;
    }

    public ArrayList<Statistika> getPriemerVytazenostObsluznychOstatneCelkove() {
        return priemerVytazenostObsluznychOstatneCelkove;
    }

    public ArrayList<Statistika> getPriemerDlzkaRadovPriPokladniachCelkove() {
        return priemerDlzkaRadovPriPokladniachCelkove;
    }

    public double getZaciatokCasu() {
        return zaciatokCasu;
    }
}
