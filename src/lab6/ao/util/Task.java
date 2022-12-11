package lab6.ao.util;

public class Task {
    private final int change;
    private final FutureResult<Long> futureResult;

    public Task(int change, FutureResult<Long> futureResult) {
        this.change = change;
        this.futureResult = futureResult;
    }

    public int getChange() {
        return change;
    }

    public FutureResult<Long> getFutureResult() {
        return futureResult;
    }
}
