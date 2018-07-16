package com.fphoenix.components;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Created by alan on 18-4-23.
 */

public class DigitCounterAction extends Action {
    public interface Updater {
        // current progress percent, and absolute val
        void update(float percent, long abs_val);
    }
    @Override
    public boolean act(float delta) {
        elapsed += delta;
        if (elapsed>=duration) {
            updater.update(1, to);
            return true;
        }
        float percent = elapsed / duration;
        current = (long)(from + (to-from)*percent);
        updater.update(percent, current);
        return false;
    }

    @Override
    public void restart() {
        super.restart();
        elapsed = 0;
    }

    public DigitCounterAction setRange(long from, long to) {
        this.from = from;
        this.to = to;
        this.current = from;
        return this;
    }
    public DigitCounterAction setDuration(float seconds) {
        this.elapsed = 0;
        this.duration = seconds;
        return this;
    }
    public DigitCounterAction setUpdater(Updater updater) {
        this.updater = updater;
        return this;
    }

    long from;
    long to;
    long current;
    float duration;
    float elapsed;
    Updater updater;
}
