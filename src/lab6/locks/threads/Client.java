package lab6.locks.threads;

import lab6.ao.util.Computations;
import lab6.locks.monitors.AbstractMonitor;

public class Client extends AbstractThread {
    private final AbstractMonitor monitor;
    private final int consumption;
    private final int iterations;
    private final Computations computations;
    private long totalComputations = 0;


    public Client(AbstractMonitor monitor, int consumption, int iterations) {
        this.monitor = monitor;
        this.consumption = consumption;
        this.iterations = iterations;
        this.computations = new Computations(iterations);
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()){
            monitor.consume(consumption);
            computations.compute();
            totalComputations += iterations / 50;
        }
    }

    public long getTotalComputations() {
        return totalComputations;
    }
}
