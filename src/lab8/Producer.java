package lab8;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.random.RandomGenerator;

public class Producer implements CSProcess {
    private final One2OneChannelInt[] buffers;
    private final RandomGenerator subscription;
    private boolean running;

    public Producer(
            One2OneChannelInt[] buffers,
            BufferSubscriber subscriber
    ) {
        this.buffers = buffers;
        this.subscription = subscriber.getBufferSubscription();
        this.running = false;
    }


    @Override
    public void run() {
        this.running = true;
        while (running) {
            int bufferIndex = subscription.nextInt(buffers.length);
            buffers[bufferIndex].out().write(1);
        }
    }

    public void stop() {
        running = false;
    }
}
