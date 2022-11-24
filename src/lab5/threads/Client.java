package lab5.threads;

import lab5.monitors.AbstractMonitor;

public class Client extends AbstractThread {
    private final AbstractMonitor monitor;
    private final int consumption;
    private int monitorAccessCounter = 0;


    public Client(AbstractMonitor monitor, int consumption) {
        this.monitor = monitor;
        this.consumption = consumption;
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()){
            monitor.consume(consumption);
            monitorAccessCounter += 1;
            System.out.println("Consumed. Access no: " + monitorAccessCounter);
            try {
                sleep(10);
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
