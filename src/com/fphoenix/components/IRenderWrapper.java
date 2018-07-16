package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by alan on 18-3-28.
 */

public interface IRenderWrapper {
    void draw(RenderComponent rc, SpriteBatch batch, float parentAlpha);
}
