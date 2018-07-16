package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by wuhao on 8/11/16.
 */
public class Image  extends RenderComponent {
    TextureRegion region;
    protected boolean flipX;
    protected boolean flipY;
    protected float anchorX = .5f;
    protected float anchorY = .5f;

    @Override
    public void reset() {
        super.reset();
        region = null;
        flipX = flipY = false;
        anchorX = anchorY = .5f;
    }

    public float getAnchorX() {
        return anchorX;
    }

    public void setAnchorX(float anchorX) {
        this.anchorX = anchorX;
    }

    public float getAnchorY() {
        return anchorY;
    }

    public void setAnchorY(float anchorY) {
        this.anchorY = anchorY;
    }
    public void setAnchor(float anchorX, float anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public TextureRegion getRegion() {
        return region;
    }
    public Image setRegion(TextureRegion region) {
        return setRegion(region, true);
    }
    public Image setRegion(TextureRegion region, boolean resize) {
        this.region = region;
        ComponentActor actor = getActor();
        if (region!=null && actor!=null && resize) {
            actor.setSize(region.getRegionWidth(), region.getRegionHeight());
        }
        return this;
    }

    public boolean isFlipX() {
        return flipX;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }

    @Override
    public void enter() {
        if (region!=null) {
            ComponentActor actor = getActor();
            actor.setSize(region.getRegionWidth(), region.getRegionHeight());
        }
    }

    @Override
    public final void drawC(SpriteBatch batch, float parentAlpha) {
        if (region==null) return;
        batch.setColor(getActor().getColor());
        batch.mulAlpha(parentAlpha);
        drawRegion(batch, parentAlpha);
    }
    public static void drawRegion(ComponentActor actor, TextureRegion region, SpriteBatch batch, float anchorX, float anchorY, float off_x, float off_y) {
        float w = actor.getWidth();
        float h = actor.getHeight();
        float ox = w * anchorX;
        float oy = h * anchorY;
        float x = actor.getX() - ox + off_x;
        float y = actor.getY() - oy + off_y;
        batch.draw(region, x, y, ox, oy,
                w, h, actor.getScaleX(), actor.getScaleY(),
                actor.getRotation());
    }
    public void drawRegion(SpriteBatch batch, float parentAlpha) {
        ComponentActor actor = getActor();
        float w = actor.getWidth();
        float h = actor.getHeight();
        float ox = w * anchorX;
        float oy = h * anchorY;
        float x = actor.getX() - ox;
        float y = actor.getY() - oy;
        boolean swapU = flipX ^ region.isFlipX();
        boolean swapV = flipY ^ region.isFlipY();
        region.flip(swapU, swapV);
        batch.draw(region, x, y, ox, oy,
                w, h, actor.getScaleX(), actor.getScaleY(),
                actor.getRotation());
        region.flip(swapU, swapV);
    }
}
