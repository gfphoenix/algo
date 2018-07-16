package com.fphoenix.components;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by alan on 18-4-23.
 */

public interface Hitable {
    Actor hit(ComponentActor ca, float localX, float localY);
}
