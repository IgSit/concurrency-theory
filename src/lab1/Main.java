package lab1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter();
        List<UpCounterThread> upCounterThreads = new ArrayList<>();
        List<DownCounterThread> downCounterThreads = new ArrayList<>();
        int number_of_threads = 100;
        for (int i = 0; i < number_of_threads; i++) {
            upCounterThreads.add(new UpCounterThread(counter));
            downCounterThreads.add(new DownCounterThread(counter));
        }

        for (int i = 0; i < number_of_threads; i++) {
            upCounterThreads.get(i).start();
            downCounterThreads.get(i).start();
        }

        try {
            for (int i = 0; i < number_of_threads; i++) {
                upCounterThreads.get(i).join();
                downCounterThreads.get(i).join();
            }
        } catch (InterruptedException e) {
            System.out.println(counter.getCounter());
            throw new RuntimeException(e);
        }

        System.out.println(counter.getCounter());
    }
}