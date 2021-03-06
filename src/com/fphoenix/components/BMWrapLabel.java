package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by alan on 18-6-6.
 */

public class BMWrapLabel extends BMComponent {
    @Override
    public void drawSelf(SpriteBatch batch) {
        ComponentActor actor = getActor();
        CharSequence text = getText();
        BitmapFont fnt = getFont();
        BitmapFont.TextBounds bounds = fnt.getBounds(text);
        float xx = actor.getX() - bounds.width * anchorX + offsetX;
        float yy = actor.getY() + bounds.height * (1 - anchorY) + offsetY;
//        fnt.draw(batch, text, xx, yy);
        fnt.drawWrapped(batch, text, xx, yy, wrapL);
    }

    public float getWrapLength() {
        return wrapL;
    }

    public BMWrapLabel setWrapLength(float length) {
        wrapL = length;
        return this;
    }
    float wrapL;
}
