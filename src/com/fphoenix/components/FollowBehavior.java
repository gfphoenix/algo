package com.fphoenix.components;

/**
 * Created by alan on 18-3-21.
 */

public class FollowBehavior extends BaseBehavior {
    @Override
    public void reset() {
        super.reset();
        followee = null;
        offsetX = offsetY = 0;
    }

    public ComponentActor getFollowee() {
        return followee;
    }

    public FollowBehavior setFollowee(ComponentActor followee) {
        this.followee = followee;
        return this;
    }
    public FollowBehavior setOffset(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return this;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    @Override
    public boolean update(float dt) {
        if (followee==null || getActor()==null) return true;
        float x = followee.getX() + offsetX;
        float y = followee.getY() + offsetY;
        ComponentActor actor = getActor();
        actor.setPosition(x, y);
        return false;
    }
    ComponentActor followee;
    float offsetX;
    float offsetY;
}
