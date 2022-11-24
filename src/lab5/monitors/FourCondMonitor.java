package lab5.monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FourCondMonitor extends AbstractMonitor{
    private int bufferCount = 0;
    private boolean isFirstConsumerWaiting = false;
    private boolean isFirstProducerWaiting = false;
    private final int maxBuffer;
    private final Lock lock = new ReentrantLock();

    private final Condition firstProducerCond = lock.newCondition();
    private final Condition restProducerCond = lock.newCondition();
    private final Condition firstConsumerCond = lock.newCondition();
    private final Condition restConsumerCond = lock.newCondition();


    public FourCondMonitor(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    public void produce(int production) {
        super.produce(production);
        try {
            lock.lock();

            while (isFirstProducerWaiting) {
                restProducerCond.await();
            }
            while (!canProduce(production)) {
                isFirstProducerWaiting = true;
                firstProducerCond.await();
            }
            bufferCount += production;
            isFirstProducerWaiting = false;
            restProducerCond.signal();
            firstConsumerCond.signal();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public void consume(int consumption) {
        super.consume(consumption);
        try {
            lock.lock();

            while (isFirstConsumerWaiting) {
                restConsumerCond.await();
            }
            while (!canConsume(consumption)) {
                isFirstConsumerWaiting = true;
                firstConsumerCond.await();
            }
            bufferCount -= consumption;
            isFirstConsumerWaiting = false;
            restConsumerCond.signal();
            firstProducerCond.signal();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "FourCondMonitor";
    }

    private boolean canProduce(int production) {
        return bufferCount + production <= maxBuffer;
    }

    private boolean canConsume(int consumption) {
        return bufferCount > consumption;
    }
}
