package lab2;

public class Main {
    public static void main(String[] args) {
        Monitor monitor = new Monitor();

        Producer producer1 = new Producer(monitor);
        Producer producer2 = new Producer(monitor);
        Client client = new Client(monitor);

        producer1.start();
        producer2.start();
        client.start();
    }


}
