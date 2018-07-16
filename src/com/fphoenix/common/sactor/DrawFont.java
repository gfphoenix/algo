package com.fphoenix.common.sactor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by wuhao on 11/3/16.
 */
public class DrawFont implements SActor.Draw {
    BitmapFont fnt;
    CharSequence label;
    public DrawFont(BitmapFont fnt, CharSequence label) {
        this.fnt = fnt;
        this.label = label;
        updateText();
    }
    protected void updateText() {

    }
    @Override
    public void draw(SActor actor, SpriteBatch batch, float parentAlpha) {
        if (label==null || label.length()==0) return;
        Color c = actor.getColor();
        float r = c.r;
        float g = c.g;
        float b = c.b;
        float a = c.a * parentAlpha;

        fnt.setColor(r, g, b, a);
        float oldScaleX = fnt.getScaleX();
        float oldScaleY = fnt.getScaleY();


        fnt.setScale(actor.getScaleX(), actor.getScaleY());
        drawFont(actor, batch);
        fnt.setScale(oldScaleX, oldScaleY);
    }
    protected void drawFont(SActor actor, SpriteBatch batch) {
        float x = actor.getX() - actor.getWidth()*actor.getAnchorX();
        float y = actor.getY() + actor.getHeight()*(1-actor.getAnchorY());
        fnt.draw(batch, label, x, y);
    }
}
