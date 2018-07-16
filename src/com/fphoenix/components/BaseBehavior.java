package com.fphoenix.components;

/**
 * Created by wuhao on 8/11/16.
 */
public abstract class BaseBehavior extends Component {
    public abstract boolean update(float dt);
    @Override
    public void remove() {
        if (actor!=null)
            actor.removeBehavior(this);
    }
}
