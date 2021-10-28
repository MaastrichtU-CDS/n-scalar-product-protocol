package Performance;

import java.util.ArrayList;

public class Result {
    private int n;
    private long time;

    public Result(int n, long time) {
        this.n = n;
        this.time = time;
    }

    public int getN() {
        return n;
    }

    public long getTime() {
        return time;
    }

    public static long average(ArrayList<Result> results) {
        return results.parallelStream().map(x -> x.getTime()).reduce(Long::sum).get() / results.size();
    }

    public static double std(long average, ArrayList<Result> results) {
        return Math.sqrt(
                results.parallelStream().map(x -> (average - x.getTime()) * (average - x.getTime())).reduce(Long::sum)
                        .get() / results.size());
    }
}
