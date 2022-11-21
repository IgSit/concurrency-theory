package lab4.threeLocks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private final Lock consumerLock = new ReentrantLock();
    private final Lock producerLock = new ReentrantLock();
    private final Lock innerLock = new ReentrantLock();
    private final Condition condition = innerLock.newCondition();
    private int bufferCount = 0;
    private final int maxBuffer;

    public void consume(int consumption) {
        consumerLock.lock();
        innerLock.lock();
        try {
            while (!canConsume(consumption)) {
                condition.await();
            }
            bufferCount -= consumption;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            consumerLock.unlock();
            innerLock.unlock();
        }
    }

    public void produce(int production) {
        producerLock.lock();
        innerLock.lock();
        try {
            while (!canProduce(production)) {
                condition.await();
            }
            bufferCount += production;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            producerLock.unlock();
            innerLock.unlock();
        }
    }

    public Monitor(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    private boolean canProduce(int production) {
        return bufferCount + production <= maxBuffer;
    }

    private boolean canConsume(int consumption) {
        return bufferCount > consumption;
    }
}
