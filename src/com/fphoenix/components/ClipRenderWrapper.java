package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by alan on 18-3-29.
 */

public class ClipRenderWrapper implements IRenderWrapper {
    @Override
    public void draw(RenderComponent rc, SpriteBatch batch, float parentAlpha) {
        ComponentActor actor = rc.getActor();
        if (actor==null) return;
        batch.flush();
        actor.clipBegin(x, y, width, height);
        rc.drawC(batch, parentAlpha);
        batch.flush();
        actor.clipEnd();
    }
    public void setRectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
    public void setOrigin(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    float x, y, width, height;
}
