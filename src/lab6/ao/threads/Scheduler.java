package lab6.ao.threads;

import lab6.ao.util.Computations;
import lab6.ao.util.FutureResult;
import lab6.ao.util.Task;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Scheduler extends Thread {

    private final int iterations;
    private long totalComputations = 0;
    private final Computations computations;
    private final ReentrantLock lock;
    private long buffer;
    private final Queue<Task> incomingRequests;
    private final Queue<Task> waitingRequests;
    private final Condition queueCondition;
    private final long maxBufferSize;
    private boolean running;

    public Scheduler(int iterations, long maxBufferSize) {
        this.iterations = iterations;
        this.computations = new Computations(iterations);
        this.lock = new ReentrantLock();
        this.buffer = 0;
        this.incomingRequests = new LinkedBlockingQueue<>();
        this.waitingRequests = new ArrayDeque<>();
        this.queueCondition = lock.newCondition();
        this.maxBufferSize = maxBufferSize;
    }

    public void enqueue(Task task) {
        incomingRequests.add(task);
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            doPresentTask();
        }
    }

    public void stopThread() {
        this.running = false;
        this.interrupt();
    }

    public long getTotalComputations() {
        return totalComputations;
    }

    private void doPresentTask() {
        try {
         lock.lock();

         while (waitingRequests.isEmpty() && incomingRequests.isEmpty()) {
             queueCondition.await();
         }
         Optional<Task> optionalTask = getTask();
         if (optionalTask.isPresent()) {
             computations.compute();
             totalComputations += iterations / 50;
             Task task = optionalTask.get();
             buffer += task.getChange();
             FutureResult<Long> result = task.getFutureResult();
             result.set(420L);
         }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private Optional<Task> getTask() {
        if (!waitingRequests.isEmpty()) {
            Task task = waitingRequests.peek();
            if (canPerformTask(task)) {
                task = waitingRequests.poll();
                return Optional.of(task);
            }
        }
        if (!incomingRequests.isEmpty()) {
            Task task = incomingRequests.peek();
            if (canPerformTask(task)) {
                task = incomingRequests.poll();
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    private boolean canPerformTask(Task task) {
        return 0 <= buffer + task.getChange() && buffer + task.getChange() <= maxBufferSize;
    }
}
