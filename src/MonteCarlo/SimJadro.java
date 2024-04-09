package MonteCarlo;

import MonteCarlo.Udalosti.Udalost;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public abstract class SimJadro {
    private int numberOfReplications;
    protected volatile boolean stopRequested = false;
    protected volatile boolean slowRequested = true;
    protected volatile boolean isPaused = false;
    protected int cisloReplikacie = 0;
    private List<ISimDelegate> delegates;

    public SimJadro(int numberOfReplications) {
        this.numberOfReplications = numberOfReplications;
        delegates = new ArrayList<>();
    }

    public void simuluj() {
        beforeReps();
        for (cisloReplikacie = 0; cisloReplikacie <= numberOfReplications && !stopRequested; cisloReplikacie++) {
            beforeRep();
            doRep();
            afterRep();
            if (!slowRequested && cisloReplikacie % 1000 == 0 && cisloReplikacie > 1000) {
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

    public void setSlowRequested(boolean slowRequested) {
        this.slowRequested = slowRequested;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void setStopRequested(boolean stopRequested) {
        this.stopRequested = stopRequested;
    }

    public boolean isSlowRequested() {
        return slowRequested;
    }

    public int getCisloReplikacie() {
        return cisloReplikacie;
    }
}