package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by alan on 18-3-28.
 */

public class RenderWrapperComponent extends RenderComponent {
    protected RenderComponent rc;
    private IRenderWrapper wrapper;
    boolean enableWrapper = true;
    public RenderWrapperComponent setRenderComponent(RenderComponent rc) {
        if (rc.getActor() != null)
            throw new IllegalStateException("component can only be added one time");
        ComponentActor actor = getActor();
        if (actor==null) {
            this.rc = rc;
        }else {
            if (this.rc!=null) {
                this.rc.exit();
                this.rc.actor = null;
            }
            this.rc = rc;
            rc.actor = actor;
            rc.enter();
        }
        return this;
    }
    public RenderComponent getRenderComponent() {
        return rc;
    }
    public IRenderWrapper getWrapper() {
        return wrapper;
    }
    public void setWrapper(IRenderWrapper wrapper) {
        this.wrapper = wrapper;
    }
    public void setEnableWrapper(boolean enable) {
        this.enableWrapper = enable;
    }
    public boolean isEnableWrapper() {
        return enableWrapper;
    }
    @Override
    public void enter() {
        super.enter();
        rc.actor = getActor();
        rc.enter();
    }

    @Override
    public void exit() {
        rc.exit();
        rc.actor = null;
        super.exit();
    }

    @Override
    public void reset() {
        super.reset();
        rc = null;
    }

    @Override
    public void remove() {
        super.remove();
        rc = null;
    }
    @Override
    public void drawC(SpriteBatch batch, float parentAlpha) {
        if (wrapper!=null && enableWrapper)
            wrapper.draw(rc, batch, parentAlpha);
        else
            rc.drawC(batch, parentAlpha);
    }
}
