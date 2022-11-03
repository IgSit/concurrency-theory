package lab4;

public class Producer extends Thread{
    private final Monitor monitor;
    private int production;
    private int monitorAccessCounter = 0;


    public Producer(Monitor monitor, int production) {
        this.monitor = monitor;
        this.production = production;
    }

    public void setProduction(int production) {
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
                sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
