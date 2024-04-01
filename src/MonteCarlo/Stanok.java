package MonteCarlo;

import MonteCarlo.Rozdelenia.Deterministicke;
import MonteCarlo.Rozdelenia.Exponencialne;
import MonteCarlo.Osoby.Osoba;
import MonteCarlo.Rozdelenia.SpojiteRovnomerne;
import MonteCarlo.Udalosti.PrichodZakaznika;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Stanok extends UdalostnaSimulacia{
    private Queue<Osoba> osobyQueue;
    private int personIndex;
    private double cas; // v sekundach
    private ObsluzneMiesta obsluzneMiesta;

    private Statistika priemerCasVObchode;
    private Statistika priemerDlzkaRadu;
    private Statistika priemerPocetLudiCelkovy;
    private Statistika priemerCasVObchodeCelkovy;
    private Statistika priemerDlzkaRaduCelkovy;
    private Statistika priemerCakanieVRadeCelkovy;
    private Statistika priemerCakanieVRade;

    private boolean automatIsEmpty;

    private double zaciatokCasu;
    private double koniecCasu;
    private NahodneJavy nahodnyJav;
    private Pokladne pokladne;

    public Stanok(int numberOfReplications, int pocetObsluznychMiest, int pocetPokladni) {
        super(numberOfReplications);
        this.osobyQueue = new LinkedList<>();
        this.zaciatokCasu = 0;
        this.koniecCasu = 8*60;
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


    }

    @Override
    void beforeRep() {
        simCas = 0.0;
        this.personIndex = 0;
        priemerCasVObchode = new Statistika(false);
        priemerDlzkaRadu = new Statistika(true);
        priemerCakanieVRade = new Statistika(false);
        automatIsEmpty = true;
        udalostiQueue.add(new PrichodZakaznika(this, simCas + nahodnyJav.getPrichodLudi()));
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
}
