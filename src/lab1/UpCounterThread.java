package lab1;

public class UpCounterThread extends Thread implements ICounterThread {
    private final Counter counter;

    UpCounterThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void  run() {
        for (int i = 0; i < reps; i++) {
            counter.increment();
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
