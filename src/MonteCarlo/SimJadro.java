package MonteCarlo;

import MonteCarlo.Udalosti.Udalost;

import java.util.PriorityQueue;

public abstract class SimJadro {
    private int numberOfReplications;
    private volatile boolean stopRequested = false;


    public SimJadro(int numberOfReplications) {
        this.numberOfReplications = numberOfReplications;
    }

    public void simuluj() {
        beforeReps();
        for (int i = 0; i < numberOfReplications && !stopRequested; i++) {
            beforeRep();
            doRep();
            afterRep();
        }
        afterReps();
    }

    abstract void doRep();

    abstract void beforeReps();

    abstract void afterReps();

    abstract void beforeRep();

    abstract void afterRep();

    public int getNumberOfReplications() {
        return numberOfReplications;
    }

    public void stopSimulaciu() {
        stopRequested = true;
    }


}