package lab8;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.Random;

public class Consumer implements CSProcess {
    private final One2OneChannelInt[] consumeRequests;
    private final One2OneChannelInt[] buffers;
    private final Random subscription;
    private boolean running;
    private final int id;

    public Consumer(
            One2OneChannelInt[] consumeRequests,
            One2OneChannelInt[] buffers,
            BufferSubscriber subscriber,
            int id) {
        this.consumeRequests = consumeRequests;
        this.buffers = buffers;
        this.subscription = subscriber.getBufferSubscription();
        this.id = id;
    }

    @Override
    public void run() {
        this.running = true;
        while (running) {
            int bufferIndex = subscription.nextInt(buffers.length);
            System.out.printf("Consumer %d: Attempt buffer %d access\n", id, bufferIndex);
            consumeRequests[bufferIndex].out().write(0);
            if (buffers[bufferIndex].in().read() == 1) {
                System.out.printf("Consumer %d: I was accepted\n", id);
            }
            else {
                System.out.printf("Consumer %d: I was rejected\n", id);
            }
        }
    }

    public One2OneChannelInt[] getConsumeRequests() {
        return consumeRequests;
    }

    public One2OneChannelInt[] getBuffers() {
        return buffers;
    }

    public void stop() {
        running = false;
    }
}
