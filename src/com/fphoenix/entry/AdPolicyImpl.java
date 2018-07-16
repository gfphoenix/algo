package com.fphoenix.entry;

import com.fphoenix.platform.PlatformUtils;

public class AdPolicyImpl implements AdPolicy{
    short video_count=1;
	short count;
    long lastTime;
    long lastVideoTime;

    // 1 minute
    static final long MAX_TIME = 90 * 1000;
    static final int MAX_COUNT = 3;
    static final short MAX_VIDEO_COUNT = 2;
    static final long MAX_VIDEO_TIME = 90 * 1000;

    // make sure that the first attempt should show the ad
    public AdPolicyImpl() {
        this.count = 0;
        this.lastTime = -1;
    }

	private boolean shouldShowAd(long c) {
		return count >= MAX_COUNT || (lastTime>0 && (c-lastTime)>=MAX_TIME);
	}
    private void reset(long c) {
        count = 0;
        lastTime = c;
        if (c<0)
            lastTime = System.currentTimeMillis();
    }
	public void addInterrupt(boolean consume) {
		count++;
        long c = System.currentTimeMillis();
        if (consume && shouldShowAd(c)) {
            show();
            reset(c);
        }
	}

    public boolean addIntVideo(boolean consume) {
        boolean ok = ++video_count >= MAX_VIDEO_COUNT;
        long tmp = System.currentTimeMillis();
        ok = ok || (tmp - lastVideoTime >= MAX_VIDEO_COUNT);
        if (consume && ok) {
            video_count = 0;
            lastVideoTime = tmp;
        }
        return ok;
    }

    @Override
    public long getMaxFreeTime() {
        return MAX_TIME;
    }

    private void show() {
        PlatformUtils.showFullAd();
    }
}
