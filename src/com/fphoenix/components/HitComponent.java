package com.fphoenix.components;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by wuhao on 8/11/16.
 */
public abstract class HitComponent implements Hitable {
    public abstract Actor hit(ComponentActor ca, float x, float y);

    public static final HitComponent SELF = new HitComponent() {
        @Override
        public Actor hit(ComponentActor ca, float x, float y) {
            return ca;
        }
    };
    public static final HitComponent NONE = new HitComponent() {
        @Override
        public Actor hit(ComponentActor ca, float x, float y) {
            return null;
        }
    };
}
