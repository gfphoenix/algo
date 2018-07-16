package com.fphoenix.components;

/**
 * Created by alan on 18-3-21.
 */

public class DynamicBehavior extends BaseBehavior {
    @Override
    public boolean update(float dt) {
        ComponentActor ca = getActor();
        float x = ca.getX() + vx * dt;
        float y = ca.getY() + vy * dt;
        vx += ax * dt;
        vy += ay * dt;
        ca.setPosition(x, y);
        return false;
    }

    public float vx;
    public float vy;
    public float ax;
    public float ay;
}
