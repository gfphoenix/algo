package com.fphoenix.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

/**
 * Created by alan on 18-4-23.
 */

public class SimpleButtonListener extends IListener {
    protected float initX;
    protected float initY;
    final float radius = 48*48;

    @Override
    public boolean onTouchDown(InputEvent e, float x, float y) {
        initX = x;
        initY = y;
        return super.onTouchDown(e, x, y);
    }

    @Override
    public boolean onTouchUp(InputEvent e, float x, float y) {
        float dx = x - initX;
        float dy = y - initY;
        boolean ok = dx*dx+dy*dy <= radius;
        return ok && super.onTouchUp(e, x, y);
    }
}
