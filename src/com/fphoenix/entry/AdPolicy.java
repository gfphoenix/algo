package com.fphoenix.entry;

/**
 * Created by wuhao on 3/2/17.
 */
public interface AdPolicy {
    void addInterrupt(boolean consume);
    long getMaxFreeTime();

    boolean addIntVideo(boolean consume);

    public static class AdFree implements AdPolicy{
        @Override
        public void addInterrupt(boolean consume) {
        }
        @Override
        public long getMaxFreeTime() {
            return Long.MAX_VALUE;
        }
        public boolean addIntVideo(boolean consume){
            return true;
        }

    }
}
