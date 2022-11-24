package lab5.threads;


import lab5.monitors.AbstractMonitor;

public class Producer extends Thread{
    private final AbstractMonitor monitor;
    private final int production;
    private int monitorAccessCounter = 0;


    public Producer(AbstractMonitor monitor, int production) {
        this.monitor = monitor;
        this.production = production;
    }

    @Override
    public void run() {
        for (;;) {
            monitor.produce(production);
            monitorAccessCounter += 1;
            if (production > 1) System.out.print("I'm fat - ");
            System.out.println("Produced. Access no: " + monitorAccessCounter);
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
