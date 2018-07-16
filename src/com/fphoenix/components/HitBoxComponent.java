package com.fphoenix.components;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by wuhao on 8/11/16.
 */
public class HitBoxComponent extends HitComponent {
    public float offX, offY;
    public float width, height;

    public void set(float offX, float offY, float width, float height) {
        this.offX = offX;
        this.offY = offY;
        this.width = width;
        this.height = height;
    }

    @Override
    public Actor hit(ComponentActor ca, float x, float y) {
        boolean ok = offX <=x && x<= offX +width
                && offY <=y && y<= offY +height;
        return ok ? ca : null;
    }

    public void reset() {
        offX = offY = 0;
        width = height = 0;
    }

    @Override
    public String toString() {
        return "offset=("+offX+", "+offY+"), size="+width+"x"+height;
    }
}
