import MonteCarlo.ISimDelegate;
import MonteCarlo.SimJadro;
import MonteCarlo.Stanok;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIPorovnanie implements ISimDelegate {
    private int pocetObsluznych;
    private int pocetReplikacii;
    private ArrayList<Integer> replicationCounter;
    private final int minPokladni = 2;
    private final int maxPokladni = 6;
    private XYChart chart;

private ArrayList<List<Integer>> xValues;
private ArrayList<List<Double>> yValues;
    private XChartPanel<XYChart> chartPanel;
    public GUIPorovnanie(int pocetObsluznych, int pocetReplikacii) {
        this.pocetObsluznych = pocetObsluznych;
        this.pocetReplikacii = pocetReplikacii;
        JFrame frame = new JFrame("JTable Demo");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chart = new XYChartBuilder().width(800).height(600).title(getClass().getSimpleName()).xAxisTitle("Replikácia").yAxisTitle("Čakanie v rade").build();
        JPanel panel = new JPanel();
        chartPanel = new XChartPanel<>(chart);
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);
        //chart.getStyler().setXAxisTickMarkSpacingHint(1000);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    public void startSimulation() {
        ArrayList<Thread> threads = new ArrayList<>();
        replicationCounter = new ArrayList<>();
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();

        for (int i = minPokladni; i <= maxPokladni; i++) {
            Stanok stanok = new Stanok(pocetReplikacii, pocetObsluznych, i);
            stanok.setSlowRequested(false);
            stanok.registerDelegate(this);
            Thread thread = new Thread(stanok::simuluj);
            threads.add(thread);
            replicationCounter.add(0);
            List<Integer> x = new ArrayList<>();
            x.add(0);
            xValues.add(x);
            List<Double> y = new ArrayList<>();
            y.add(0.0);
            yValues.add(y);
            chart.addSeries(String.valueOf(i), xValues.get(i - minPokladni), yValues.get(i - minPokladni));

        }



        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).start();
        }
    }

    @Override
    public synchronized void refresh(SimJadro simJadro) {
        SwingUtilities.invokeLater(() -> {
            Stanok sim = (Stanok) simJadro;
            int tempPocetPokladni = sim.getPocetPokladni();
            int i = tempPocetPokladni - minPokladni;
            if (replicationCounter.get(i) == 0) {
                xValues.get(i).removeFirst();
                yValues.get(i).removeFirst();
            }
            xValues.get(i).add(replicationCounter.get(i));
            replicationCounter.set(i, replicationCounter.get(i) +1000);
            yValues.get(i).add(sim.getPriemerDlzkaRaduCelkovy().vypocitaj());
            chart.updateXYSeries(String.valueOf(tempPocetPokladni), xValues.get(i), yValues.get(i), null);
            chartPanel.revalidate();
            chartPanel.repaint();
        });
    }
}
