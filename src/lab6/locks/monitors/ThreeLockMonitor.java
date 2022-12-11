package lab6.locks.monitors;

import lab6.ao.util.Computations;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreeLockMonitor extends AbstractMonitor {
    private final Lock consumerLock = new ReentrantLock();
    private final Lock producerLock = new ReentrantLock();
    private final Lock innerLock = new ReentrantLock();
    private final Condition condition = innerLock.newCondition();
    private int bufferCount = 0;
    private final int maxBuffer;

    private final int iterations;
    private int totalComputations = 0;
    private final Computations computations;

    public ThreeLockMonitor(int maxBuffer, int iterations) {
        this.maxBuffer = maxBuffer;
        this.iterations = iterations;
        this.computations = new Computations(iterations);
    }

    public void consume(int consumption) {
        super.consume(consumption);
        consumerLock.lock();
        innerLock.lock();
        try {
            while (!canConsume(consumption)) {
                condition.await();
            }
            bufferCount -= consumption;
            computations.compute();
            totalComputations += iterations / 50;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            consumerLock.unlock();
            innerLock.unlock();
        }
    }

    public void produce(int production) {
        super.produce(production);
        producerLock.lock();
        innerLock.lock();
        try {
            while (!canProduce(production)) {
                condition.await();
            }
            bufferCount += production;
            computations.compute();
            totalComputations += iterations / 50;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            producerLock.unlock();
            innerLock.unlock();
        }
    }

    public int getTotalComputations() {
        return totalComputations;
    }

    @Override
    public String toString() {
        return "ThreeLockMonitor";
    }

    private boolean canProduce(int production) {
        return bufferCount + production <= maxBuffer;
    }

    private boolean canConsume(int consumption) {
        return bufferCount > consumption;
    }
}
