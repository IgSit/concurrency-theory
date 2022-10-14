package lab2;

public class Monitor {
    private int bufferCount = 0;

    public synchronized void produce() {
        while (!canProduce()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bufferCount++;
        notify();
    }

    public synchronized void consume() {
        while (!canConsume()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bufferCount--;
        notify();
    }

    private synchronized boolean canProduce() {
        return bufferCount == 0;
    }

    private synchronized boolean canConsume() {
        return bufferCount >= 1;
    }
}
