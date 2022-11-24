package lab5;

import lab5.monitors.AbstractMonitor;
import lab5.monitors.FourCondMonitor;
import lab5.monitors.ThreeLockMonitor;
import lab5.threads.Client;
import lab5.threads.Producer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        int maxBuffer = 1000;
        AbstractMonitor fourCondMonitor = new FourCondMonitor(maxBuffer);
        AbstractMonitor threeLockMonitor = new ThreeLockMonitor(maxBuffer);

        List<AbstractMonitor> monitors = List.of(fourCondMonitor, threeLockMonitor);
        List<Integer> threadAmounts = List.of(2, 10, 50, 100);

        int repetitions = 5;

        Random random = new Random();
        random.setSeed(420);

        String filename = "data.csv";
        File file = new File(filename);
        file.createNewFile();
        FileWriter writer = new FileWriter(filename);
        writeHeader(writer);

        for (int threadAmount: threadAmounts) {
            for (AbstractMonitor monitor: monitors) {
                for (int i = 0; i < repetitions; i++) {
                    String dataRow = getDataRow(threadAmount, monitor, random);
                    writer.write(dataRow);
                }
            }
        }
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

    private static String getDataRow(int threadAmount, AbstractMonitor monitor, Random random) throws InterruptedException {
        StringJoiner joiner = new StringJoiner(",");
        String totalChange = String.valueOf(
                getSingleResult(threadAmount, 450, 2000, monitor, random)
        );
        String threadAmountString = String.valueOf(threadAmount);
        joiner.
                add(monitor.toString()).
                add(threadAmountString).
                add(totalChange + "\n");
        return joiner.toString();
    }

    private static int getSingleResult(
            int threadTypeAmount,
            int maxChange,
            int execTime,
            AbstractMonitor monitor,
            Random random
    ) throws InterruptedException {

        List<Producer> producers = new ArrayList<>();
        List<Client> clients = new ArrayList<>();

        for (int i = 0; i < threadTypeAmount; i++) {
            producers.add(new Producer(monitor, getRandomInt(random, maxChange)));
            clients.add(new Client(monitor, getRandomInt(random, maxChange)));
        }

        for (int i = 0; i < threadTypeAmount; i++) {
            producers.get(i).start();
            clients.get(i).start();
        }

        Thread.sleep(execTime);

        for (int i = 0; i < threadTypeAmount; i++) {
            producers.get(i).stopThread();
            clients.get(i).stopThread();
        }

        for (int i = 0; i < threadTypeAmount; i++) {
            producers.get(i).join();
            clients.get(i).join();
        }

        return monitor.getTotalChange();
    }

    private static int getRandomInt(Random random, int max) {
        return random.nextInt(max);
    }


}
