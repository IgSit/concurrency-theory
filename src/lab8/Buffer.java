package lab8;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

public class Buffer implements CSProcess {

    private final One2OneChannelInt[] producers;
    private final One2OneChannelInt[] consumeRequests;
    private final One2OneChannelInt[] consumers;
    private final int bufferCapacity;
    private int bufferValue;
    private boolean running;

    public Buffer(
            One2OneChannelInt[] producers,
            One2OneChannelInt[] consumeRequests,
            One2OneChannelInt[] consumers,
            int bufferCapacity) {
        assert consumers.length == consumeRequests.length;

        this.producers = producers;
        this.consumeRequests = consumeRequests;
        this.consumers = consumers;
        this.bufferCapacity = bufferCapacity;
        this.bufferValue = 0;
    }

    @Override
    public void run() {
        running = true;
        final Guard[] guards = initGuards();
        final Alternative alternative = new Alternative(guards);

        while (running) {
            System.out.printf("Buffer: current value: %d\n", bufferValue);
            int index = alternative.fairSelect();
            if (isProducer(index)) {
                if (bufferValue < bufferCapacity) {
                    producers[index].out().write(1);
                    int production = producers[index].in().read();
                    assert production == 1;
                    bufferValue += production;
                    System.out.println("Buffer: Accepted producer");
                }
                else {
                    producers[index].out().write(0);
                    System.out.println("Buffer: rejected producer");
                }
            }
            else {
                if (bufferValue > 0) {
                    consumers[index].out().write(1);
                    int consumption = consumeRequests[index].in().read();
                    assert consumption == 1;
                    bufferValue -= consumption;
                    System.out.println("Buffer: accepted consumer");
                }
                else {
                    consumers[index].out().write(0);
                    System.out.println("Buffer: rejected consumer");
                }
            }
        }
    }

    public void stop() {
        running = false;
    }

    private Guard[] initGuards() {
        final Guard[] guards = new Guard[producers.length + consumers.length];
        int i = 0;
        for (One2OneChannelInt producer : producers) {
            guards[i] = producer.in();
            i++;
        }
        for (One2OneChannelInt consumerRequest : consumeRequests) {
            guards[i] = consumerRequest.in();
            i++;
        }
        return guards;
    }

    private boolean isProducer(int index) {
        return index < producers.length;
    }
}
