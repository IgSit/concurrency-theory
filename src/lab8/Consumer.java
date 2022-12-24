package lab8;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.random.RandomGenerator;

public class Consumer implements CSProcess {
    private final One2OneChannelInt[] consumeRequests;
    private final One2OneChannelInt[] buffers;
    private final RandomGenerator subscription;
    private boolean running;

    public Consumer(
            One2OneChannelInt[] consumeRequests,
            One2OneChannelInt[] buffers,
            BufferSubscriber subscriber) {
        this.consumeRequests = consumeRequests;
        this.buffers = buffers;
        this.subscription = subscriber.getBufferSubscription();
    }

    @Override
    public void run() {
        this.running = true;
        while (running) {
            int bufferIndex = subscription.nextInt(buffers.length);
            consumeRequests[bufferIndex].out().write(0);
            buffers[bufferIndex].in().read();
        }
    }
    public void stop() {
        running = false;
    }
}
