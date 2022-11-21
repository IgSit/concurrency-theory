package lab4.threeLocks;

public class Client extends Thread {
    private final Monitor monitor;
    private int consumption;
    private int monitorAccessCounter = 0;

    public Client(Monitor monitor, int consumption) {
        this.monitor = monitor;
        this.consumption = consumption;
    }

    public void setConsumption(int consumption) {
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
                sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
