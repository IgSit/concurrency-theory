package lab8;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.random.RandomGenerator;

public class Producer implements CSProcess {
    private One2OneChannelInt[] buffers;
    private One2OneChannelInt[] bufferAvailabilities;
    private final RandomGenerator bufferAccess;
    private boolean running;

    public Producer(
            One2OneChannelInt[] buffers,
            One2OneChannelInt[] bufferAvailabilities,
            BufferSubscriber subscriber
    ) {
        this.buffers = buffers;
        this.bufferAvailabilities = bufferAvailabilities;
        this.bufferAccess = subscriber.getBufferSubscription();
        this.running = false;
    }


    @Override
    public void run() {
        this.running = true;
        while (running) {
            int bufferIndex = bufferAccess.nextInt(buffers.length);
            if (bufferAvailabilities[bufferIndex].in().read() == 1) {
                buffers[bufferIndex].out().write(1);
            }
        }
    }

    public void stop() {
        running = false;
    }
}
