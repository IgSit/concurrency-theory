package lab6;

import java.util.Random;

public class Producer extends Thread{

    private final int maxChange;
    private final Random random;
    private final Scheduler scheduler;
    private boolean running;
    private long taskCounter;

    public Producer(int maxChange, Scheduler scheduler) {
        this.random = new Random();
        this.maxChange = maxChange;
        this.scheduler = scheduler;
        this.taskCounter = 0;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            int production = getRandomLength();
            Task task = new Task(production);
            FutureResult<Long> futureResult = scheduler.enqueue(task);

            performAsyncComputations(futureResult);
        }
    }

    private void performAsyncComputations(FutureResult<Long> futureResult) {
        while (running && !futureResult.isDone()) {
            Computations.compute();
            taskCounter += 1;
        }
    }

    private int getRandomLength() {
        return random.nextInt(1, maxChange + 1);
    }

    public void stopThread() {
        this.running = false;
    }

    public long getTaskCounter() {
        return taskCounter;
    }
}
