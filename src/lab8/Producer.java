package lab8;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.random.RandomGenerator;

public class Producer implements CSProcess {
    private final One2OneChannelInt[] buffers;
    private final RandomGenerator subscription;
    private boolean running;
    private final int id;

    public Producer(
            One2OneChannelInt[] buffers,
            BufferSubscriber subscriber,
            int id
    ) {
        this.buffers = buffers;
        this.subscription = subscriber.getBufferSubscription();
        this.id = id;
    }


    @Override
    public void run() {
        this.running = true;
        while (running) {
            int bufferIndex = subscription.nextInt(buffers.length);
            System.out.printf("Producer %d: Attempt buffer %d access\n", id, bufferIndex);
            buffers[bufferIndex].out().write(1);
            if (buffers[bufferIndex].in().read() == 1) {
                System.out.printf("Producer %d: I was accepted\n", id);
            }
            else {
                System.out.printf("Producer %d: I was rejected\n", id);
            }
        }
    }

    public One2OneChannelInt[] getBuffers() {
        return buffers;
    }

    public void stop() {
        running = false;
    }
}
