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
    private final int id;
    private int accessCounter;

    public Buffer(
            One2OneChannelInt[] producers,
            One2OneChannelInt[] consumeRequests,
            One2OneChannelInt[] consumers,
            int bufferCapacity,
            int id) {
        assert consumers.length == consumeRequests.length;

        this.producers = producers;
        this.consumeRequests = consumeRequests;
        this.consumers = consumers;
        this.bufferCapacity = bufferCapacity;
        this.bufferValue = 0;
        this.id = id;
        accessCounter = 0;
    }

    @Override
    public void run() {
        running = true;
        final Guard[] guards = initGuards();
        final Alternative alternative = new Alternative(guards);

        while (running) {
            System.out.printf("Buffer %d: current value: %d, accessed: %d\n", id, bufferValue, accessCounter);
            int index = alternative.fairSelect();
            if (isProducer(index)) {
                if (bufferValue < bufferCapacity) {
                    int production = producers[index].in().read();
                    accessCounter += 1;
                    producers[index].out().write(1);
                    assert production == 1;
                    bufferValue += production;
                    System.out.printf("Buffer %d: Accepted producer %d\n", id, index);
                }
                else {
                    producers[index].out().write(0);
                    System.out.printf("Buffer %d: rejected producer %d\n", id, index);
                }
            }
            else {
                index -= producers.length;
                if (bufferValue > 0) {
                    int consumption = consumeRequests[index].in().read();
                    accessCounter += 1;
                    consumers[index].out().write(1);
                    assert consumption == 1;
                    bufferValue -= consumption;
                    System.out.printf("Buffer %d: Accepted consumer %d\n", id, index);
                }
                else {
                    consumers[index].out().write(0);
                    System.out.printf("Buffer %d: Rejected consumer %d\n", id, index);
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
