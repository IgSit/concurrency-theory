package lab6.locks;

import lab6.locks.monitors.AbstractMonitor;
import lab6.locks.monitors.ThreeLockMonitor;
import lab6.locks.threads.Client;
import lab6.locks.threads.Producer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class Main {

    private static int CLIENT_ITERATIONS = 100;
    private static int SCHEDULER_ITERATIONS = 100;
    private static final int THREAD_TYPE_AMOUNT = 10;
    private static final int BUFFER_SIZE = 100;
    private static final int MAX_CHANGE = BUFFER_SIZE / 3;

    public static void main(String[] args) throws InterruptedException, IOException {
        String filename = "3lock.csv";
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
                .add("ThreeLock")
                .add(String.valueOf(CLIENT_ITERATIONS))
                .add(String.valueOf(SCHEDULER_ITERATIONS))
                .add(totalChange + "\n");
        return joiner.toString();
    }

    private static long getSingleResult() throws InterruptedException {
        AbstractMonitor monitor = new ThreeLockMonitor(BUFFER_SIZE, SCHEDULER_ITERATIONS);

        List<Producer> producers = new ArrayList<>();
        List<Client> consumers = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            producers.add(new Producer(monitor, getRandomInt(random), CLIENT_ITERATIONS));
            consumers.add(new Client(monitor, getRandomInt(random), CLIENT_ITERATIONS));
        }

        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            producers.get(i).start();
            consumers.get(i).start();
        }

        Thread.sleep(1000);

        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            producers.get(i).stopThread();
            consumers.get(i).stopThread();
        }

        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            consumers.get(i).join();
            producers.get(i).join();
        }

        long totalComputations = monitor.getTotalChange();
        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            totalComputations += consumers.get(i).getTotalComputations();
            totalComputations += producers.get(i).getTotalComputations();
        }

        return totalComputations;
    }

    private static int getRandomInt(Random random) {
        return random.nextInt(Main.MAX_CHANGE);
    }


}
