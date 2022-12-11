package lab6.ao.util;

public final class Computations {
    private final int iterations;

    public Computations(int iterations) {
        this.iterations = iterations;
    }

    public void compute() {
        long sum = 0;
        for (int i = 0; i < iterations; i++) {
            sum += Math.sin(2137.420);
        }
    }
}
