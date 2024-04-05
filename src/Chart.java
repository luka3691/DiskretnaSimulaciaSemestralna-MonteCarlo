import MonteCarlo.ISimDelegate;
import MonteCarlo.SimJadro;
import MonteCarlo.Stanok;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Chart extends JFrame implements ISimDelegate {


    private AtomicBoolean isUpdatingStopped;
    private double minYValueA = Double.POSITIVE_INFINITY;
    private double maxYValueA = Double.NEGATIVE_INFINITY;
    private double minYValueB = Double.POSITIVE_INFINITY;
    private double maxYValueB = Double.NEGATIVE_INFINITY;
    private double minYValueC = Double.POSITIVE_INFINITY;
    private double maxYValueC = Double.NEGATIVE_INFINITY;
    private JTextField replicationInput;
    private JLabel radPredAutomatom;
    private JLabel jeAutomatObsadeny;
    private JLabel normalnyRadValue;
    private JLabel onlineRadValue;
    private JLabel osobaVAutomate;
    private ArrayList<JLabel> obsluzneMiestaNormalne;
    private ArrayList<JLabel> obsluzneMiestaOnline;
    private ArrayList<JLabel> pokladne;
    private ArrayList<JLabel> pokladneRady;

    private JLabel cas;
    private JTextField valueInput;
    private final double Y_AXIS_MARGIN = 0.1;
    private JButton startButton;
    private JButton startRandomButton;
    private JButton startZerosButton;
    private int numberOfValueA = 0;
    private int numberOfValueB = 0;
    private int numberOfValueC = 0;
    private int tickUnits = 10;

    private int pocetObsluznychMiest;
    private int pocetPokladni;

    public Chart(String title, int pocetObsluznychMiest, int pocetPokladni) {
        super(title);

        this.pocetPokladni = pocetPokladni;
        this.pocetObsluznychMiest = pocetObsluznychMiest;
        JPanel automatPanel = new JPanel();

        JLabel radPredAutomatomLabel = new JLabel("Počet ľudi pred automatom:");
        radPredAutomatom = new JLabel("Pocet");
        JLabel jeVAutomateLabel = new JLabel("Automat: ");
        jeAutomatObsadeny = new JLabel("OBSADENY");
        JLabel osobaVAutoLabel = new JLabel("ID osoby v automate: ");
        osobaVAutomate = new JLabel("0");
        automatPanel.add(radPredAutomatomLabel);
        automatPanel.add(radPredAutomatom);
        automatPanel.add(jeVAutomateLabel);
        automatPanel.add(jeAutomatObsadeny);
        automatPanel.add(osobaVAutoLabel);
        automatPanel.add(osobaVAutomate);
        jeAutomatObsadeny.setVisible(false);
        osobaVAutomate.setVisible(false);
        automatPanel.setLayout(new GridLayout(3, 2));
        getContentPane().setLayout(new GridLayout(4, 1));
        getContentPane().add(automatPanel);

        JPanel obsluznePanel = new JPanel();
        obsluzneMiestaNormalne = new ArrayList<>();
        obsluzneMiestaOnline = new ArrayList<>();
        int pocetOnlineObsluznych =  pocetObsluznychMiest / 3;
        int poceNormalnychObsluznych = pocetObsluznychMiest - pocetOnlineObsluznych;
        JLabel normalnyRad = new JLabel("Rad pre normálnych: ");
        normalnyRadValue = new JLabel("0");
        JLabel onlineRad = new JLabel("Rad pre online: ");
        onlineRadValue = new JLabel("0");
        obsluznePanel.add(normalnyRad);
        obsluznePanel.add(normalnyRadValue);
        obsluznePanel.add(onlineRad);
        obsluznePanel.add(onlineRadValue);
        for (int i = 0; i < poceNormalnychObsluznych; i++) {
            JLabel normalneObsluzne = new JLabel("Normálne obslužné č." + i  + ":");
            JLabel normalneObsluzneObsadene = new JLabel("OBSADENE");
            normalneObsluzneObsadene.setVisible(false);
            obsluzneMiestaNormalne.add(normalneObsluzneObsadene);
            obsluznePanel.add(normalneObsluzne);
            obsluznePanel.add(normalneObsluzneObsadene);
        }
        for (int i = 0; i < pocetOnlineObsluznych; i++) {
            JLabel onlineObsluzne = new JLabel("Online obslužné č." + i  + ":");
            JLabel onlineObsluzneObsadene = new JLabel("OBSADENE");
            onlineObsluzneObsadene.setVisible(false);
            obsluzneMiestaOnline.add(onlineObsluzneObsadene);
            obsluznePanel.add(onlineObsluzne);
            obsluznePanel.add(onlineObsluzneObsadene);
        }
        obsluznePanel.setLayout(new GridLayout(pocetObsluznychMiest+2, 2));
        getContentPane().add(obsluznePanel);

        JPanel pokladnePanel = new JPanel();
        pokladne = new ArrayList<>();
        pokladneRady = new ArrayList<>();
        for (int i = 0; i < pocetPokladni; i++) {
            JLabel pokladnaLabel = new JLabel("Pokladna č." + i  + ":");
            JLabel pokladnaRad = new JLabel("0");
            JLabel pokladnaStav = new JLabel("OBSADENE");
            pokladnaStav.setVisible(false);
            pokladne.add(pokladnaStav);
            pokladneRady.add(pokladnaRad);
            pokladnePanel.add(pokladnaLabel);
            pokladnePanel.add(pokladnaRad);
            pokladnePanel.add(pokladnaStav);
        }
        pokladnePanel.setLayout(new GridLayout(pocetPokladni, 3));
        getContentPane().add(pokladnePanel);

        JPanel topPanel = new JPanel();
        cas = new JLabel("CAS");
        topPanel.add(cas);
        topPanel.setLayout(new GridLayout());
        getContentPane().add(topPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        this.isUpdatingStopped = new AtomicBoolean(false);
        startSimulation(pocetObsluznychMiest, pocetPokladni);
    }
    private void disableButtons() {
        startButton.setEnabled(false);
    }

    private void startSimulation(int pocetObsluznychMiest, int pocetPokladni) {
        Stanok stanok = new Stanok(1,pocetObsluznychMiest, pocetPokladni);
        Thread simulatcia1 = new Thread(stanok::simuluj);
        stanok.registerDelegate(this);
//odstarovanie simulacie pre kazdu strategiu
        simulatcia1.start();
    }

    public void stopUpdating() {
        //zastavenie simulacie
        isUpdatingStopped.set(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Chart("Splatená suma", 15, 6));
    }


    @Override
    public void refresh(SimJadro simJadro) {
        Stanok sim = (Stanok) simJadro;
        cas.setText(String.valueOf(sim.getSimCas()));
        radPredAutomatom.setText(String.valueOf(sim.getOsobyQueue().size()));
        if (sim.getAutomatIsEmpty()) {
            jeAutomatObsadeny.setVisible(false);
            osobaVAutomate.setVisible(false);
        } else {
            jeAutomatObsadeny.setVisible(true);
            if (sim.getOsobaUAutomatu() != null)  {
                osobaVAutomate.setText(String.valueOf(sim.getOsobaUAutomatu().getID()));
            }

            osobaVAutomate.setVisible(true);
        }
        int pocetOnlineObsluznych =  pocetObsluznychMiest / 3;
        int poceNormalnychObsluznych = pocetObsluznychMiest - pocetOnlineObsluznych;
        normalnyRadValue.setText(String.valueOf(sim.getObsluzneMiesta().getOsobyQueue().size()));
        onlineRadValue.setText(String.valueOf(sim.getObsluzneMiesta().getOnlineQueue().size()));
        for (int i = 0; i < pocetOnlineObsluznych; i++) {
            if (sim.getObsluzneMiesta().getOnlineObsluzne()[i]) {
                obsluzneMiestaOnline.get(i).setVisible(false);
            } else {
                obsluzneMiestaOnline.get(i).setVisible(true);
            }
        }
        for (int i = 0; i < poceNormalnychObsluznych; i++) {
            if (sim.getObsluzneMiesta().getNormalneObsluzne()[i]) {
                obsluzneMiestaNormalne.get(i).setVisible(false);
            } else {
                obsluzneMiestaNormalne.get(i).setVisible(true);
            }
        }
        for (int i = 0; i < pocetPokladni; i++) {
            if (sim.getPokladne().getPokladne()[i]) {
                pokladne.get(i).setVisible(false);
            } else {
                pokladne.get(i).setVisible(true);
            }
            pokladneRady.get(i).setText(String.valueOf(sim.getPokladne().getRady()[i].size()));
        }

        try {
            Thread.sleep(50); // Sleep for 5 milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            // Handle the InterruptedException if required
            // For example, log it or rethrow it
        }
    }
}