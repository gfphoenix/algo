package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by alan on 18-4-23.
 */

public class NinePatchComponent extends RenderComponent {
    private NinePatchObject object;
    private float anchorX=.5f;
    private float anchorY=.5f;

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
    public void setAnchor(float anchorX, float anchorY){
        this.anchorX	= anchorX;
        this.anchorY	= anchorY;
    }
    public NinePatchComponent setup(NinePatchObject object) {
        this.object = object;
        return this;
    }
    public NinePatchComponent setup(TextureRegion mother, int l, int r, int t, int b) {
        NinePatchObject object = new NinePatchObject().setup(mother, l, r, t, b);
        return setup(object);
    }
    public NinePatchObject getNinePatchObject() {
        return object;
    }
    @Override
    public void drawC(SpriteBatch batch, float parentAlpha) {
        ComponentActor ca = getActor();
        batch.setColor(ca.getColor());
        batch.mulAlpha(parentAlpha);
        float w = ca.getWidth() * ca.getScaleX();
        float h = ca.getHeight() * ca.getScaleY();
        object.draw(batch, ca.getX(), ca.getY(), (int)w, (int)h, anchorX, anchorY);
    }
}
