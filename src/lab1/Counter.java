package lab1;

public class Counter {
    private int counter;

    Counter() {
        this.counter = 0;
    }

    public synchronized void increment() {
        this.counter++;
    }

    public synchronized void decrement() {
        this.counter--;
    }

    public int getCounter() {
        return counter;
    }
}
