package lab6;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int THREAD_TYPE_AMOUNT = 10;
    private static final int BUFFER_SIZE = 100;

    private static final int MAX_CHANGE = BUFFER_SIZE / 3;

    public static void main(String[] args) throws InterruptedException {
        Scheduler scheduler = new Scheduler(BUFFER_SIZE);
        List<Consumer> consumers = new ArrayList<>();
        List<Producer> producers = new ArrayList<>();

        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            producers.add(new Producer(MAX_CHANGE, scheduler));
            consumers.add(new Consumer(MAX_CHANGE, scheduler));
        }

        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            producers.get(i).start();
            consumers.get(i).start();
        }

        Thread.sleep(5000);

        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            producers.get(i).stopThread();
            consumers.get(i).stopThread();
        }

        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            consumers.get(i).join();
            producers.get(i).join();
        }
    }
}
