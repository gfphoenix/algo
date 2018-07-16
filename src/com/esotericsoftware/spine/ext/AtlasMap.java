package com.esotericsoftware.spine.ext;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by wuhao on 3/2/17.
 */
public class AtlasMap implements AttachmentMapper {
    TextureAtlas ta;
    public AtlasMap(TextureAtlas ta) {
        this.ta = ta;
    }

    public TextureAtlas getAtlas() {
        return ta;
    }

    @Override
    public TextureRegion map(String name) {
        return ta.findRegion(name);
    }
}
