package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by alan on 18-4-20.
 */

public class XpatchImage extends RenderComponent {
    private XpatchObject object;
    public XpatchImage init(TextureRegion tex, int leftWidth, int rightWidth) {
        object = new XpatchObject().init(tex, leftWidth, rightWidth);
        return this;
    }
    public XpatchImage init(XpatchObject object) {
        this.object = object;
        return this;
    }

    public XpatchObject getXObject() {
        return object;
    }

    @Override
    public void drawC(SpriteBatch batch, float parentAlpha) {
        ComponentActor ca = getActor();
        object.draw(batch, ca.getX(), ca.getY(), ca.getWidth(), ca.getScaleX(), ca.getScaleY(), ca.getRotation());
    }
}
