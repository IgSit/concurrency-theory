package lab7.zad1;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

public class PCMain {

    public PCMain ()
    { // Create channel object
        final One2OneChannelInt channel = Channel.one2oneInt();
// Create and run parallel construct with a list of processes
        CSProcess[] procList = { new Producer(channel), new Consumer(channel) };
// Processes
        Parallel par = new Parallel(procList); // PAR construct
        par.run(); // Execute processes in parallel
    }

    public static void main (String[] args) {
        new PCMain();
    }
}
