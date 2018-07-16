package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by wuhao on 8/11/16.
 */
public abstract class BMComponent extends RenderComponent {
    BitmapFont font;
    CharSequence text;
    float anchorX = .5f;
    float anchorY = .5f;
    float offsetX;
    float offsetY;

    public BitmapFont getFont() {
        return font;
    }

    public BMComponent setFont(BitmapFont font) {
        this.font = font;
        return this;
    }

    public CharSequence getText() {
        return text;
    }

    public BMComponent setText(CharSequence text) {
        this.text = text;
        return this;
    }
    public BMComponent setAnchor(float anchorX, float anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        return this;
    }

    public float getAnchorX() {
        return anchorX;
    }

    public float getAnchorY() {
        return anchorY;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public BMComponent setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public BMComponent setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public BMComponent setOffset(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return this;
    }

    @Override
    public final void drawC(SpriteBatch batch, float parentAlpha) {
        if (text==null || text.length()==0) return;
        ComponentActor actor = getActor();
        font.setColor(actor.getColor());
        font.mulAlpha(parentAlpha);
        float scaleX = font.getScaleX();
        float scaleY = font.getScaleY();
        font.setScale(actor.getScaleX(), actor.getScaleY());
        font.setColor(actor.getColor());
        drawSelf(batch);
        font.setScale(scaleX, scaleY);
    }
    public void drawSelf(SpriteBatch batch){}
}
