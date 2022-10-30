package lab3;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Monitor monitor = new Monitor();
        int amount = 20;

        List<Producer> producers = new ArrayList<>();
        List<Client> clients = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            producers.add(new Producer(monitor));
            clients.add(new Client(monitor));
        }

        for (int i = 0; i < amount; i++) {
            producers.get(i).start();
            clients.get(i).start();
        }
    }


}
