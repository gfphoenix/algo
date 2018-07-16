package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by alan on 18-5-26.
 */

public class ImageData {
    TextureRegion region;
    float offsetX, offsetY;

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffset(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public ImageData setRegion(TextureRegion region) {
        this.region = region;
        return this;
    }
}
