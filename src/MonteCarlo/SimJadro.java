package MonteCarlo;

import MonteCarlo.Udalosti.Udalost;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public abstract class SimJadro {
    private int numberOfReplications;
    protected volatile boolean stopRequested = false;
    protected volatile boolean slowRequested = true;
    private List<ISimDelegate> delegates;

    public SimJadro(int numberOfReplications) {
        this.numberOfReplications = numberOfReplications;
        delegates = new ArrayList<>();
    }

    public void simuluj() {
        beforeReps();
        for (int i = 0; i < numberOfReplications && !stopRequested; i++) {
            beforeRep();
            doRep();
            afterRep();
            if (!slowRequested) {
                refreshGUI();
            }
        }
        afterReps();
    }

    public void registerDelegate(ISimDelegate delegate)
    {
        delegates.add(delegate);
    }
    protected void refreshGUI()
    {
        for (ISimDelegate delegate : delegates)
        {
            delegate.refresh(this);
        }
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

    public void setSlowRequested(boolean slowRequested) {
        this.slowRequested = slowRequested;
    }

    public boolean isSlowRequested() {
        return slowRequested;
    }
}