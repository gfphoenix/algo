package com.fphoenix.components;

/**
 * Created by wuhao on 8/11/16.
 */
public abstract class Component {
    ComponentActor actor;
    public ComponentActor getActor(){
        return actor;
    }
    public void enter(){} // called after set actor
    public void exit(){} // called before removed
    public void reset() {
        actor = null;
    }
    public abstract void remove();
}
