package lab5.threads;

import lab5.monitors.AbstractMonitor;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractThread extends Thread {
    protected AbstractMonitor monitor;
    protected int production;
    protected int monitorAccessCounter = 0;
    protected final AtomicBoolean running = new AtomicBoolean(false);

    public void stopThread() {
        running.set(false);
        this.interrupt();
    }
}
