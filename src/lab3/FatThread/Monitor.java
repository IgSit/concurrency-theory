package lab3.FatThread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private int bufferCount = 0;
    private final int maxBuffer;
    private final Lock lock = new ReentrantLock();
    private final Condition producerCond = lock.newCondition();
    private final Condition consumerCond = lock.newCondition();

    public Monitor(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    public void produce(int production) {
        lock.lock();
        try {
            while (!canProduce(production)) {
                producerCond.await();
            }
            bufferCount += production;
            consumerCond.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }
    }

    public void consume(int consumption) {
        lock.lock();
        try {
            while (!canConsume(consumption)) {
                consumerCond.await();
            }
            bufferCount -= consumption;
            producerCond.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }
    }

    private boolean canProduce(int production) {
        return bufferCount + production <= maxBuffer;
    }

    private boolean canConsume(int consumption) {
        return bufferCount > consumption;
    }
}
