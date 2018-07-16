package com.esotericsoftware.spine.ext;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by wuhao on 3/7/17.
 */
public class DummyAttachmentMapper implements AttachmentMapper {
    TextureRegion region;
    public DummyAttachmentMapper(TextureRegion region) {
        if (region==null) {
            throw new NullPointerException("region cannot be null");
        }
        this.region = region;
    }

    public TextureRegion getRegion() {
        return region;
    }

    @Override
    public TextureRegion map(String path) {
        return region;
    }
}
