package lab3;

public class Producer extends Thread{
    private final Monitor monitor;

    public Producer(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        for (;;) {
            monitor.produce();
            System.out.println("Produced");
        }
    }
}
