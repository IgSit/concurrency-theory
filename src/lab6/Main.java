package lab6;

import lab5.monitors.AbstractMonitor;
import lab6.threads.Consumer;
import lab6.threads.Producer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Main {

    private static final int CLIENT_ITERATIONS = 100;
    private static final int SCHEDULER_ITERATIONS = 100;
    private static final int THREAD_TYPE_AMOUNT = 10;
    private static final int BUFFER_SIZE = 100;
    private static final int MAX_CHANGE = BUFFER_SIZE / 3;

    public static void main(String[] args) throws InterruptedException, IOException {
        String filename = "data.csv";
        File file = new File(filename);
        file.createNewFile();
        FileWriter writer = new FileWriter(filename);
        writeHeader(writer);

        for (int threadAmount: threadAmounts) {
            for (AbstractMonitor monitor: monitors) {
                for (int i = 0; i < repetitions; i++) {
                    String dataRow = getDataRow();
                    writer.write(dataRow);
                }
            }
        }
        List<Integer> sizes = List.of(5, 25, 50, 100);
        writer.close();
    }

    private static void writeHeader(FileWriter writer) throws IOException {
        StringJoiner titleJoiner = new StringJoiner(",");
        titleJoiner.
                add("Monitor").
                add("ThreadTypeAmount").
                add("TotalChange\n");
        writer.write(titleJoiner.toString());
    }

    private static String getDataRow() throws InterruptedException {
        StringJoiner joiner = new StringJoiner(",");
        String totalChange = String.valueOf(
                getSingleResult()
        );
        joiner.
                add("Active Object").
                add(String.valueOf(CLIENT_ITERATIONS)).
                add(String.valueOf(SCHEDULER_ITERATIONS)).
                add(totalChange + "\n");
        return joiner.toString();
    }

    private static int getSingleResult() throws InterruptedException {

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

        int totalComputations = scheduler.getTotalComputations();
        for (int i = 0; i < THREAD_TYPE_AMOUNT; i++) {
            totalComputations += consumers.get(i).getTotalComputations();
            totalComputations += producers.get(i).getTotalComputations();
        }

        return totalComputations;
    }
}
