package lab5.threads;

import lab5.monitors.AbstractMonitor;

public class Producer extends AbstractThread{
    private final AbstractMonitor monitor;
    private final int production;
    private int monitorAccessCounter = 0;


    public Producer(AbstractMonitor monitor, int production) {
        this.monitor = monitor;
        this.production = production;
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()){
            monitor.produce(production);
            monitorAccessCounter += 1;
            System.out.println("Produced. Access no: " + monitorAccessCounter);
            try {
                sleep(10);
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
