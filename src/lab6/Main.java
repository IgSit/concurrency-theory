package lab6;

import lab6.threads.Consumer;
import lab6.threads.Producer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int CLIENT_ITERATIONS = 100;
    private static final int SCHEDULER_ITERATIONS = 100;
    private static final int THREAD_TYPE_AMOUNT = 10;
    private static final int BUFFER_SIZE = 100;
    private static final int MAX_CHANGE = BUFFER_SIZE / 3;

    public static void main(String[] args) throws InterruptedException {
        Scheduler scheduler = new Scheduler(SCHEDULER_ITERATIONS, BUFFER_SIZE);
        List<Consumer> consumers = new ArrayList<>();
        List<Producer> producers = new ArrayList<>();

        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            producers.add(new Producer(CLIENT_ITERATIONS, MAX_CHANGE, scheduler));
            consumers.add(new Consumer(CLIENT_ITERATIONS, MAX_CHANGE, scheduler));
        }

        scheduler.start();
        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            producers.get(i).start();
            consumers.get(i).start();
        }

        Thread.sleep(5000);

        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            producers.get(i).stopThread();
            consumers.get(i).stopThread();
        }
        scheduler.stopThread();

        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            consumers.get(i).join();
            producers.get(i).join();
        }
        scheduler.join();

        long totalComputations = scheduler.getTotalComputations();
        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            totalComputations += consumers.get(i).getTotalComputations();
            totalComputations += producers.get(i).getTotalComputations();
        }

        System.out.println(totalComputations);
    }
}
