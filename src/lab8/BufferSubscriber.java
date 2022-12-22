package lab8;

import java.util.Random;
import java.util.random.RandomGenerator;

public class BufferSubscriber {

    public RandomGenerator getBufferSubscription() {
        return new Random();
    }
}
