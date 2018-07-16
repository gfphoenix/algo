package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by alan on 18-4-20.
 */

public class ProgressBar extends RenderComponent {
    protected TextureRegion background;
    protected TextureRegion foreground;
    protected TextureRegion tmp;
    private float anchorX = .5f;
    private float anchorY = .5f;
    private float off_left;
    private float off_bottom;
    private boolean back_front = false; // draw background in front

    public ProgressBar setBackground(TextureRegion background) {
        this.background = background;
        return this;
    }
    public ProgressBar setForeground(TextureRegion foreground) {
        this.foreground = foreground;
        this.tmp = new TextureRegion(foreground, 0, 0, 0, foreground.getRegionHeight());
        return this;
    }
    public ProgressBar setPercent(float percent){
        float t = MathUtils.clamp(percent, 0, 1);
        int w = MathUtils.round(t*foreground.getRegionWidth());
        tmp.setRegionWidth(w);
        return this;
    }
    public float getPercent() {
        return tmp.getRegionWidth()/(float)foreground.getRegionWidth();
    }
    public ProgressBar setAnchor(float anchorX, float anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        return this;
    }

    public boolean isBackFront() {
        return back_front;
    }
    public ProgressBar setBackFront(boolean front) {
        this.back_front = front;
        return this;
    }

    public void setAnchorX(float anchorX) {
        this.anchorX = anchorX;
    }

    public void setAnchorY(float anchorY) {
        this.anchorY = anchorY;
    }

    public float getAnchorX() {
        return anchorX;
    }

    public float getAnchorY() {
        return anchorY;
    }

    public ProgressBar setOffset(float off_left, float off_bottom) {
        this.off_left = off_left;
        this.off_bottom = off_bottom;
        return this;
    }
    // put background and foreground aligned
    public ProgressBar autoConfig() {
        off_left = (background.getRegionWidth()-foreground.getRegionWidth())/2f;
        off_bottom = (background.getRegionHeight()-foreground.getRegionHeight())/2f;
        return this;
    }


    @Override
    public void drawC(SpriteBatch batch, float parentAlpha) {
        if(background==null)
            return;

        ComponentActor ca = getActor();
        batch.setColor(ca.getColor());

        // drawC background
        float w = background.getRegionWidth();
        float h = background.getRegionHeight();
        float ox = w * anchorX;
        float oy = h * anchorY;
        float x = actor.getX() - ox;
        float y = actor.getY() - oy;
        final float sx = ca.getScaleX();
        final float sy = ca.getScaleY();
        final float rot = ca.getRotation();
        if (back_front) {
            // drawC foreground
            batch.draw(tmp, x + off_left, y + off_bottom, ox - off_left, oy - off_bottom,
                    tmp.getRegionWidth(), tmp.getRegionHeight(),
                    sx, sy, rot);
            batch.draw(background, x, y, ox, oy,
                    w, h,
                    sx, sy, rot);
        }else {
            batch.draw(background, x, y, ox, oy,
                    w, h,
                    sx, sy, rot);
            // drawC foreground
            batch.draw(tmp, x + off_left, y + off_bottom, ox - off_left, oy - off_bottom,
                    tmp.getRegionWidth(), tmp.getRegionHeight(),
                    sx, sy, rot);
        }
    }
}
