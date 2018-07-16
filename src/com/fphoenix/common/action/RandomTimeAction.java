package com.fphoenix.common.action;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Created by wuhao on 3/21/17.
 */
public class RandomTimeAction extends Action {
    float t0, t1;
    float duration;
    float elapsed = -1;
    public RandomTimeAction(float time0, float time1) {
        this.t0 = time0;
        this.t1 = time1;
    }

    @Override
    public boolean act(float v) {
        if (elapsed<0) {
            elapsed = 0;
            if (t0>=t1) {
                duration = t0;
            }else {
                duration = MathUtils.random(t0, t1);
            }
            return false;
        }
        elapsed += v;
        return elapsed >= duration;
    }
    public float getDuration() {
        return duration;
    }
    @Override
    public void reset() {
        super.reset();
        elapsed = -1;
    }

    @Override
    public void restart() {
        super.restart();
        elapsed = -1;
    }
}
