package lab5.monitors;

public abstract class AbstractMonitor {
    private int totalChange = 0;

    public void produce(int production) {
        totalChange += production;
    }

    public void consume(int consumption) {
        totalChange += consumption;
    }

    public int getTotalChange() {
        return totalChange;
    }
}
