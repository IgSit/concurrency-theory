package lab6;

public final class Computations {
    private static final int ITERATIONS = 50;

    public static void compute() {
        long sum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum += Math.sin(2137.420);
        }
    }
}
