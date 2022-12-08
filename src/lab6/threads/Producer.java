package lab6.threads;

import lab6.util.Computations;
import lab6.util.FutureResult;
import lab6.Scheduler;
import lab6.util.Task;

import java.util.Random;

public class Producer extends Thread{

    private final int iterations;
    private int totalComputations = 0;
    private final Computations computations;
    private final int maxChange;
    private final Random random;
    private final Scheduler scheduler;
    private boolean running;

    public Producer(int iterations, int maxChange, Scheduler scheduler) {
        this.iterations = iterations;
        this.computations = new Computations(iterations);
        this.random = new Random();
        this.maxChange = maxChange;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            int production = getRandomLength();
            Task task = new Task(production, new FutureResult<>());
            scheduler.enqueue(task);

            performAsyncComputations(task.getFutureResult());
        }
    }

    private void performAsyncComputations(FutureResult<Long> futureResult) {
        while (running && !futureResult.isDone()) {
            computations.compute();
            totalComputations += iterations;
        }
    }

    private int getRandomLength() {
        return random.nextInt(maxChange + 1);
    }

    public void stopThread() {
        this.running = false;
        this.interrupt();
    }

    public int getTotalComputations() {
        return totalComputations;
    }
}
