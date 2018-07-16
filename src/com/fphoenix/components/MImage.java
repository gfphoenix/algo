package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by alan on 18-4-23.
 */

public class MImage extends Image {
    Array<ImageData> slaves = new Array<ImageData>();

    @Override
    public void reset() {
        super.reset();
        slaves.clear();
    }
    public MImage addImageData(ImageData imageData) {
        if (imageData!=null && imageData.region!=null)
            slaves.add(imageData);
        return this;
    }
    public MImage addImageData(TextureRegion region) {
        return addImageData(region, 0, 0);
    }
    public MImage addImageData(TextureRegion region, float offsetX, float offsetY) {
        if (region!=null) {
            ImageData data = new ImageData();
            data.region = region;
            data.offsetX = offsetX;
            data.offsetY = offsetY;
            slaves.add(data);
        }
        return this;
    }
    public int n_slaves() {
        return slaves.size;
    }
    public ImageData get(int idx) {
        return slaves.get(idx);
    }
    public Array<ImageData> getSlaves() {
        return slaves;
    }
    @Override
    public void enter() {
        if (region!=null) {
            ComponentActor actor = getActor();
            actor.setSize(region.getRegionWidth(), region.getRegionHeight());
        }
    }

    public void drawRegion(SpriteBatch batch, float parentAlpha) {
        ComponentActor actor = getActor();
        float w = actor.getWidth();
        float h = actor.getHeight();
        float ox = w * anchorX;
        float oy = h * anchorY;
        float x = actor.getX() - ox;
        float y = actor.getY() - oy;
        float sx = actor.getScaleX();
        float sy = actor.getScaleY();
        float rot = actor.getRotation();
        boolean swapU = flipX ^ region.isFlipX();
        boolean swapV = flipY ^ region.isFlipY();
        region.flip(swapU, swapV);
        batch.draw(region, x, y, ox, oy,
                w, h, sx, sy, rot);
        region.flip(swapU, swapV);

        // drawC slaves
        w /= region.getRegionWidth();
        h /= region.getRegionHeight();
        for (int i=0,n=slaves.size; i<n; i++) {
            ImageData data = slaves.get(i);
            TextureRegion r = data.region;
            if (r==null) continue;
            float ww = w*r.getRegionWidth();
            float hh = h*r.getRegionHeight();
            ox = ww * anchorX;
            oy = hh * anchorY;
            x = actor.getX() - ox + data.offsetX;
            y = actor.getY() - oy + data.offsetY;

            batch.draw(r, x, y,
                    ox, oy,
                    ww, hh, sx, sy, rot);
        }
    }
}
