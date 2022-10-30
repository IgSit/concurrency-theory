package lab3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private int bufferCount = 0;
    private final int maxBuffer = 10;
    private final int maxChange = 5;
    private final Lock lock = new ReentrantLock();
    private final Condition producerCond = lock.newCondition();
    private final Condition consumerCond = lock.newCondition();

    public void produce() {
        lock.lock();
        try {
            int change = currentChange();
            while (!canProduce(change)) {
                producerCond.await();
            }
            bufferCount += change;
            consumerCond.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }
    }

    public void consume() {
        lock.lock();
        try {
            int change = currentChange();
            while (!canConsume(change)) {
                consumerCond.await();
            }
            bufferCount -= change;
            producerCond.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }
    }

    private int currentChange() {
        return (int) (maxChange * Math.random());
    }

    private boolean canProduce(int production) {
        return bufferCount + production <= maxBuffer;
    }

    private boolean canConsume(int consumption) {
        return bufferCount > consumption;
    }
}
