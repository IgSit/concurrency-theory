package lab4.noHasWaiters;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int amount = 20;
        int maxBuffer = 10;
        int fatChange = 4;
        Monitor monitor = new Monitor(maxBuffer);

        List<Producer> producers = new ArrayList<>();
        List<Client> clients = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            producers.add(new Producer(monitor, 1));
            clients.add(new Client(monitor, 1));
        }

        producers.get(0).setProduction(fatChange);
        clients.get(0).setConsumption(fatChange);

        for (int i = 0; i < amount; i++) {
            producers.get(i).start();
            clients.get(i).start();
        }
    }


}
