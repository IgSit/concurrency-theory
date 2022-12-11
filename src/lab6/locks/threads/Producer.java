package lab6.locks.threads;

import lab6.ao.util.Computations;
import lab6.locks.monitors.AbstractMonitor;

public class Producer extends AbstractThread {
    private final AbstractMonitor monitor;
    private final int production;
    private final int iterations;
    private final Computations computations;
    private long totalComputations = 0;


    public Producer(AbstractMonitor monitor, int production, int iterations) {
        this.monitor = monitor;
        this.production = production;
        this.iterations = iterations;
        this.computations = new Computations(iterations);
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()){
            monitor.produce(production);
            computations.compute();
            totalComputations += iterations / 50;
        }
    }

    public long getTotalComputations() {
        return totalComputations;
    }
}
