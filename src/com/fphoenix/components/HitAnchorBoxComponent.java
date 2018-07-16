package com.fphoenix.components;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by alan on 18-4-23.
 */

public class HitAnchorBoxComponent extends HitComponent {
    public static final HitAnchorBoxComponent CENTER = new HitAnchorBoxComponent();

    @Override
    public Actor hit(ComponentActor ca, float x, float y) {
        final float w = ca.getWidth();
        final float h = ca.getHeight();
        x += w*anchorX;
        y += h*anchorY;
        boolean touchable = x>=0 && x<=w && y>=0 && y<=h;
        return touchable ? ca : null;
    }
    public void setAnchor(float anchorX, float anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }
    public float getAnchorX() {
        return anchorX;
    }

    public float getAnchorY() {
        return anchorY;
    }

    private float anchorX=.5f;
    private float anchorY=.5f;
}
