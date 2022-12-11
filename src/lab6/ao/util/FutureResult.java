package lab6.ao.util;

public class FutureResult<T> {
    private volatile T result = null;

    public boolean isDone() {
        return result != null;
    }

    public T get() {
        return result;
    }

    public void set(T value) {
        this.result = value;
    }
}
