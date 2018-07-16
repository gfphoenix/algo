package misc;

public class FPS {
    private static final int N_SLOTS = 1<<5;
    private static final int SLOT_MASK = N_SLOTS-1;
    // more samples means more accuracy
    private long []times;
    private int index; // last frame slot
    private int frames;

    // hint_fps only affect the init fps
    public FPS(float hint_fps) {
        if (hint_fps<=0)
            throw new IllegalArgumentException("hint fps must be positive number");
        times = new long[N_SLOTS];
        index = frames = 0;
        long c = System.currentTimeMillis();
        long dt = (long)(1000 / hint_fps);
        if (dt==0) dt=1;
        times[index] = c;
        for (int i=times.length-1; i>0; i--) {
            c -= dt;
            times[i] = c;
        }
    }
    public FPS() {
        this(30);
    }
    private int next_index() {
        return (index+1) & SLOT_MASK;
    }
    public int getFrames() {
        return frames;
    }
    public int addFrame() {
        index = next_index();
        times[index] = System.currentTimeMillis();
        return ++frames;
    }
    public float fps() {
        int oldest_index = next_index();
        long dms = times[index] - times[oldest_index];
        // if dms is 0, it means the fps is too high, should use more samples or take record time every two or more samples,
        // but for normal apps, fps lower than 500 is safe
        return SLOT_MASK * 1000f / dms;
    }
}
