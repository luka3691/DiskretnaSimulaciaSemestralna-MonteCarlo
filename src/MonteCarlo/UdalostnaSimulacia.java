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
        while (!udalostiQueue.isEmpty()) {
            Udalost udalost = udalostiQueue.poll();
            if (udalost.getCasUdalosti() < simCas) {
                //throw zly cas error
                System.out.println("Zly cas!");
            }
            simCas = udalost.getCasUdalosti();

            udalost.execute();
            if (slowRequested) {
                refreshGUI();
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
