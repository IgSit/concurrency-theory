package lab5;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random random = new Random();
        random.setSeed(420);


//        int amount = 20;
//        int maxBuffer = 10;
//        AbstractMonitor monitor = new FourCondMonitor(maxBuffer);
//
//        List<Producer> producers = new ArrayList<>();
//        List<Client> clients = new ArrayList<>();
//
//        for (int i = 0; i < amount; i++) {
//            producers.add(new Producer(monitor, 1));
//            clients.add(new Client(monitor, 1));
//        }
//
//        for (int i = 0; i < amount; i++) {
//            producers.get(i).start();
//            clients.get(i).start();
//        }
    }

    private static int getRandomInt(Random random) {
        return random.nextInt(10);
    }


}
