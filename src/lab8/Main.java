package lab8;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

public class Main {
    private static final int PRODUCERS = 20;
    private static final int CONSUMERS = 20;
    private static final int BUFFERS = 10;
    private static final int BUFFER_CAPACITY = 5;

    public static void main(String[] args) throws InterruptedException {
        Producer[] producers = new Producer[PRODUCERS];
        Consumer[] consumers = new Consumer[CONSUMERS];
        Buffer[] buffers = new Buffer[BUFFERS];
        CSProcess[] processes = new CSProcess[PRODUCERS + CONSUMERS + BUFFERS];
        BufferSubscriber subscriber = new BufferSubscriber();

        int processIndex = 0;

        for (int i = 0; i < PRODUCERS; i++) {
            One2OneChannelInt[] bufferChannels = new One2OneChannelInt[BUFFERS];
            for (int j = 0; j < BUFFERS; j++) {
                bufferChannels[j] = Channel.one2oneInt();
            }
            producers[i] = new Producer(bufferChannels, subscriber, i);
            processes[processIndex] = producers[i];
            processIndex++;
        }

        for (int i = 0; i < CONSUMERS; i++) {
            One2OneChannelInt[] consumeRequests = new One2OneChannelInt[BUFFERS];
            One2OneChannelInt[] bufferChannels = new One2OneChannelInt[BUFFERS];
            for (int j = 0; j < BUFFERS; j++) {
                bufferChannels[j] = Channel.one2oneInt();
                consumeRequests[j] = Channel.one2oneInt();
            }
            consumers[i] = new Consumer(consumeRequests, bufferChannels, subscriber, i);
            processes[processIndex] = consumers[i];
            processIndex++;
        }

        for (int i = 0; i < BUFFERS; i++) {
            One2OneChannelInt[] producerChannels = new One2OneChannelInt[PRODUCERS];
            One2OneChannelInt[] consumerRequests = new One2OneChannelInt[CONSUMERS];
            One2OneChannelInt[] consumerChannels = new One2OneChannelInt[CONSUMERS];

            for (int j = 0; j < PRODUCERS; j++) {
                producerChannels[j] = producers[j].getBuffers()[i];
            }

            for (int j = 0; j < CONSUMERS; j++) {
                consumerRequests[j] = consumers[j].getConsumeRequests()[i];
                consumerChannels[j] = consumers[j].getBuffers()[i];
            }

            buffers[i] = new Buffer(
              producerChannels,
              consumerRequests,
              consumerChannels,
              BUFFER_CAPACITY,
              i
            );
            processes[processIndex] = buffers[i];
            processIndex++;
        }

        Parallel parallel = new Parallel(processes);
        parallel.run();

        Thread.sleep(1000);

        parallel.removeAllProcesses();
        parallel.releaseAllThreads();

        for (int i = 0; i < PRODUCERS; i++) {
            producers[i].stop();
        }
        for (int i = 0; i < CONSUMERS; i++) {
            consumers[i].stop();
        }
        for (int i = 0; i < BUFFERS; i++) {
            buffers[i].stop();
        }
    }
}
