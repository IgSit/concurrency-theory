package lab6.locks.threads;

import lab6.locks.monitors.AbstractMonitor;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractThread extends Thread {
    protected AbstractMonitor monitor;
    protected int production;

    protected final AtomicBoolean running = new AtomicBoolean(false);

    public void stopThread() {
        running.set(false);
        this.interrupt();
    }
}
