package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by alan on 18-5-16.
 */

public class LightComponent extends NinePatchComponent {
    final Array<ImageData> slaves = new Array<ImageData>();

    @Override
    public void reset() {
        super.reset();
        slaves.clear();
    }

    public LightComponent addImageData(ImageData imageData) {
        if (imageData != null && imageData.region != null)
            slaves.add(imageData);
        return this;
    }

    public LightComponent addImageData(TextureRegion region) {
        return addImageData(region, 0, 0);
    }

    public LightComponent addImageData(TextureRegion region, float offsetX, float offsetY) {
        ImageData data = new ImageData();
        data.region = region;
        data.offsetX = offsetX;
        data.offsetY = offsetY;
        slaves.add(data);
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
    public void drawC(SpriteBatch batch, float parentAlpha) {
        super.drawC(batch, parentAlpha);

        ComponentActor actor = getActor();
        final float anchorX = getAnchorX();
        final float anchorY = getAnchorY();
        float w = actor.getWidth();
        float h = actor.getHeight();
        float ox = w * anchorX;
        float oy = h * anchorY;
        float x;// = actor.getX() - ox;
        float y;// = actor.getY() - oy;
        float sx = actor.getScaleX();
        float sy = actor.getScaleY();
        float rot = actor.getRotation();

        // drawC slaves
//        w /= region.getRegionWidth();
//        h /= region.getRegionHeight();
        for (int i = 0, n = slaves.size; i < n; i++) {
            ImageData data = slaves.get(i);
            TextureRegion r = data.region;
            if (r == null) continue;
            float ww = r.getRegionWidth();
            float hh = r.getRegionHeight();
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
