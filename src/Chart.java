import MonteCarlo.ISimDelegate;
import MonteCarlo.SimJadro;
import MonteCarlo.Stanok;
import MonteCarlo.UdalostnaSimulacia;


import javax.swing.*;
import java.awt.*;
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


    public Chart(String title) {
        super(title);

        JPanel chartPanel = new JPanel();

        JLabel radPredAutomatomLabel = new JLabel("Počet ľudi pred automatom:");
        radPredAutomatom = new JLabel("Pocet");
        JLabel jeVAutomateLabel = new JLabel("Automat: ");
        jeAutomatObsadeny = new JLabel("OBSADENY");
        chartPanel.add(radPredAutomatomLabel);
        chartPanel.add(radPredAutomatom);
        chartPanel.add(jeVAutomateLabel);
        chartPanel.add(jeAutomatObsadeny);
        chartPanel.setLayout(new GridLayout(2, 2));

        getContentPane().add(chartPanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        cas = new JLabel("CAS");
        topPanel.add(cas);
        topPanel.setLayout(new GridLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        this.isUpdatingStopped = new AtomicBoolean(false);
        startSimulation();
    }
    private void disableButtons() {
        startButton.setEnabled(false);
    }

    private void startSimulation() {
        Stanok stanok = new Stanok(1,5, 5);
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
        SwingUtilities.invokeLater(() -> new Chart("Splatená suma"));
    }


    @Override
    public void refresh(SimJadro simJadro) {
        Stanok sim = (Stanok) simJadro;
        cas.setText(String.valueOf(sim.getSimCas()));
        radPredAutomatom.setText(String.valueOf(sim.getOsobyQueue().size()));

        try {
            Thread.sleep(50); // Sleep for 5 milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            // Handle the InterruptedException if required
            // For example, log it or rethrow it
        }
    }
}