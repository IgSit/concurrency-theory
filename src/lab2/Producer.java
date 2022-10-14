package lab2;

public class Producer extends Thread{
    private final Monitor monitor;

    public Producer(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            monitor.produce();
            System.out.println("Produced");
        }
    }
}
