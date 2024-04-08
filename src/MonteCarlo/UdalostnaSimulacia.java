package MonteCarlo;

import MonteCarlo.Udalosti.Udalost;
import MonteCarlo.Udalosti.UdalostComparator;

import java.util.PriorityQueue;

public class UdalostnaSimulacia extends SimJadro{
    protected PriorityQueue<Udalost> udalostiQueue;
    protected double simCas = 0;
    public UdalostnaSimulacia(int numberOfReplications) {
        super(numberOfReplications);
        this.udalostiQueue = new PriorityQueue<>(new UdalostComparator());
    }

    @Override
    void doRep() {
        while (!udalostiQueue.isEmpty() && !stopRequested) {
            Udalost udalost = udalostiQueue.poll();
            if (udalost.getCasUdalosti() < simCas) {
                //throw zly cas error
                System.out.println("Zly cas!");
            }
            if (slowRequested) {
                refreshGUI();
            }
            simCas = udalost.getCasUdalosti();

            udalost.execute();

            while (isPaused) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    void beforeReps() {

    }

    @Override
    void afterReps() {

    }

    @Override
    void beforeRep() {

    }

    @Override
    void afterRep() {

    }
    public void naplanujUdalost(Udalost udalost) {
        udalostiQueue.add(udalost);
    }

    public double getSimCas() {
        return simCas;
    }
}
