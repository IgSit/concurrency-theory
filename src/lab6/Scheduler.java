package lab6;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Scheduler implements Runnable {

    private final ReentrantLock lock;
    private long buffer;
    private final Queue<Task> incomingRequests;
    private final Queue<Task> waitingRequests;
    private final Condition queueCondition;
    private final long maxBufferSize;
    private boolean running;

    public Scheduler(long maxBufferSize) {
        this.lock = new ReentrantLock();
        this.buffer = 0;
        this.incomingRequests = new ArrayDeque<>();
        this.waitingRequests = new ArrayDeque<>();
        this.queueCondition = lock.newCondition();
        this.maxBufferSize = maxBufferSize;
    }

    public FutureResult<Long> enqueue(Task task) {
        return new FutureResult<>();
    }

    @Override
    public void run() {
        running = true;
    }
}
