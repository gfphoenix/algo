package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by alan on 18-5-26.
 */
// actor has no size
public class MiscImage extends RenderComponent {
    Array<ImageData> slaves = new Array<ImageData>();
    @Override
    public void reset() {
        super.reset();
        slaves.clear();
    }
    public MiscImage addImageData(ImageData imageData) {
        if (imageData!=null && imageData.region!=null)
            slaves.add(imageData);
        return this;
    }
    public MiscImage addImageData(TextureRegion region) {
        return addImageData(region, 0, 0);
    }
    public MiscImage addImageData(TextureRegion region, float offsetX, float offsetY) {
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
    public void drawC(SpriteBatch batch, float parentAlpha) {
        ComponentActor actor = getActor();
        batch.setColor(actor.getColor());
        batch.mulAlpha(parentAlpha);

        float x = actor.getX();
        float y = actor.getY();
        float w,h, ox,oy;

        ImageData imageData;
        TextureRegion region;

        for (int i=0,n=slaves.size; i<n; i++) {
            imageData = slaves.get(i);
            region = imageData.region;
            if (region != null) {
                w = region.getRegionWidth();
                h = region.getRegionHeight();
                batch.draw(region, x-w/2 + imageData.offsetX, y-h/2 + imageData.offsetY, w, h);
            }
        }
    }
}
