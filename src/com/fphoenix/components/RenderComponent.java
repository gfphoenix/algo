package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by wuhao on 8/11/16.
 */
public class RenderComponent extends Component {
    public void setActor(ComponentActor actor) {
        this.actor = actor;
    }
    public void draw(SpriteBatch batch, float parentAlpha) {
        drawC(batch, parentAlpha);
    }
    public void drawC(SpriteBatch batch, float parentAlpha){}
    public ComponentActor tryAttachActor() {
        ComponentActor ca = getActor();
        if (ca == null) {
            ca = new ComponentActor();
            ca.setRenderComponent(this);
        }
        return ca;
    }
    @Override
    public void remove() {
        if (actor!=null)
            actor.setRenderComponent(null);
    }
}
