package lab5.threads;

import lab5.monitors.AbstractMonitor;

public class Client extends Thread {
    private final AbstractMonitor monitor;
    private final int consumption;
    private int monitorAccessCounter = 0;

    public Client(AbstractMonitor monitor, int consumption) {
        this.monitor = monitor;
        this.consumption = consumption;
    }

    @Override
    public void run() {
        for (;;) {
            monitor.consume(consumption);
            monitorAccessCounter += 1;
            if (consumption > 1) System.out.print("I'm fat - ");
            System.out.println("Consumed. Access no: " + monitorAccessCounter);
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
