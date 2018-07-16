package com.fphoenix.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

/**
 * Created by alan on 18-4-23.
 */

public class ScaleButtonListener extends SimpleButtonListener {
    float scaleFactor=1.05f;
    float oldScaleX;
    float oldScaleY;

    @Override
    public boolean onTouchDown(InputEvent e, float x, float y) {
        if (super.onTouchDown(e, x, y)) {
            Actor actor = e.getTarget();
            oldScaleX = actor.getScaleX();
            oldScaleY = actor.getScaleY();
            actor.setScale(oldScaleX * scaleFactor, oldScaleY * scaleFactor);
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchUp(InputEvent e, float x, float y) {
        e.getTarget().setScale(oldScaleX, oldScaleY);
        return super.onTouchUp(e, x, y);
    }

    public float getScaleFactor() {
        return scaleFactor;
    }
    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}
