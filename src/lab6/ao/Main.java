package lab6.ao;

import lab6.ao.threads.Consumer;
import lab6.ao.threads.Producer;
import lab6.ao.threads.Scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Main {

    private static int CLIENT_ITERATIONS = 100;
    private static int SCHEDULER_ITERATIONS = 100;
    private static final int THREAD_TYPE_AMOUNT = 10;
    private static final int BUFFER_SIZE = 100;
    private static final int MAX_CHANGE = BUFFER_SIZE / 3;

    public static void main(String[] args) throws InterruptedException, IOException {
        String filename = "ao.csv";
        File file = new File(filename);
        file.createNewFile();
        FileWriter writer = new FileWriter(filename);
        writeHeader(writer);

        List<Integer> sizes = List.of(50, 250, 500, 1000, 2000);

        for (int schedulerSize: sizes) {
            for (int threadSize : sizes) {
                SCHEDULER_ITERATIONS = schedulerSize;
                CLIENT_ITERATIONS = threadSize;
                int reps = 5;
                long total = 0;
                for (int i = 0; i < reps; i++) {
                    total += getSingleResult() / reps;
                }
                String totalChange = String.valueOf(total);
                writer.write(getDataRow(totalChange));
            }
        }
        writer.close();
    }

    private static void writeHeader(FileWriter writer) throws IOException {
        StringJoiner titleJoiner = new StringJoiner(",");
        titleJoiner
            .add("Monitor")
            .add("ClientIterations")
            .add("SchedulerIterations")
            .add("TotalChange\n");
        writer.write(titleJoiner.toString());
    }

    private static String getDataRow(String totalChange) {
        StringJoiner joiner = new StringJoiner(",");
        joiner
            .add("ActiveObject")
            .add(String.valueOf(CLIENT_ITERATIONS))
            .add(String.valueOf(SCHEDULER_ITERATIONS))
            .add(totalChange + "\n");
        return joiner.toString();
    }

    private static long getSingleResult() throws InterruptedException {

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

        Thread.sleep(1000);

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

        return totalComputations;
    }
}
