package lab3;

public class Client extends Thread {
    private final Monitor monitor;

    public Client(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        for (;;) {
            monitor.consume();
            System.out.println("Consumed");
        }
    }
}
