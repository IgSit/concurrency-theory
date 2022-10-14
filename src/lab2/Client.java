package lab2;

public class Client extends Thread {
    private final Monitor monitor;

    public Client(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            monitor.consume();
            System.out.println("Consumed");
        }
    }
}
