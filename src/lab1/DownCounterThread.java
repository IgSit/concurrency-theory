package lab1;

public class DownCounterThread extends Thread implements ICounterThread {
    private final Counter counter;

    DownCounterThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < reps; i++) {
            counter.decrement();
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
