package misc;

import java.util.Date;

public class LimitRate {
    private int limit_rate; // count per second
    private float last_count;
    private long last_time;

    public LimitRate(int limit_rate) {
        this.limit_rate = limit_rate;
        this.last_count = 0;
        this.last_time = -1;
    }

    public boolean test(long current_time) {
        if (last_count>=1) return true;
        last_count += (current_time - last_time) * limit_rate / 1000f;
        if (last_count>limit_rate) {
            last_count = limit_rate;
        }
        last_time = current_time;
        return last_count>=1;
    }
    public boolean tryConsume(long current_time) {
        if (test(current_time)) {
            last_count--;
            return true;
        }
        return false;
    }
    public int getLimitRate() {
        return limit_rate;
    }

    public static int randomN(int N) {
        return (int)(N*Math.random());
    }
    public static void main(String[] args) throws InterruptedException {
        LimitRate rate = new LimitRate(5);
        while (true) {
            Thread.sleep(randomN(100)+50);
            if (rate.tryConsume(System.currentTimeMillis())) {
                System.out.printf("req OK %s\n", new Date());
            }
        }
    }
}
