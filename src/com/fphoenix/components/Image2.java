package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by alan on 18-5-3.
 */

public class Image2 extends Image{
    TextureRegion slave;
    float offX;
    float offY;
    public Image2 setSlaveRegion(TextureRegion region) {
        this.slave = region;
        return this;
    }

    public TextureRegion getSlaveRegion() {
        return slave;
    }

    public void setOffX(float offX) {
        this.offX = offX;
    }

    public void setOffY(float offY) {
        this.offY = offY;
    }

    public float getOffX() {
        return offX;
    }

    public float getOffY() {
        return offY;
    }
    public Image2 setOff(float offX, float offY) {
        this.offX = offX;
        this.offY = offY;
        return this;
    }

    @Override
    public void drawRegion(SpriteBatch batch, float parentAlpha) {
        super.drawRegion(batch, parentAlpha);
        if (slave==null) return;
        ComponentActor actor = getActor();
        float w = slave.getRegionWidth();
        float h = slave.getRegionHeight();
        float ox = w * anchorX;
        float oy = h * anchorY;
        float x = actor.getX() - ox + offX;
        float y = actor.getY() - oy + offY;
        batch.draw(slave, x, y, ox, oy,
                w, h, actor.getScaleX(), actor.getScaleY(),
                actor.getRotation());
    }
}
