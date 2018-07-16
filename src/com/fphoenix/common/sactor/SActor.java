package com.fphoenix.common.sactor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by wuhao on 11/3/16.
 */
public class SActor extends Actor {
    public interface Draw {
        void draw(SActor actor, SpriteBatch batch, float parentAlpha);
    }
    private float anchorX;
    private float anchorY;
    private Draw drawable;

    public Draw getDrawable() {
        return drawable;
    }
    public void setDrawable(Draw drawable) {
        this.drawable = drawable;
    }

    public float getAnchorX() {
        return anchorX;
    }

    public float getAnchorY() {
        return anchorY;
    }

    public SActor setAnchorX(float anchorX) {
        this.anchorX = anchorX;
        return this;
    }

    public SActor setAnchorY(float anchorY) {
        this.anchorY = anchorY;
        return this;
    }
    public SActor setAnchor(float anchorX, float anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        return this;
    }

    @Override
    public final void draw(SpriteBatch batch, float parentAlpha) {
        if (drawable!=null)
            drawable.draw(this, batch, parentAlpha);
    }
}
