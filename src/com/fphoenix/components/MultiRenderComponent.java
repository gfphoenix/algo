package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhao on 8/25/16.
 */
public class MultiRenderComponent extends RenderComponent {
    List<RenderComponent> list;
    public MultiRenderComponent() {
        list = new ArrayList<RenderComponent>();
    }
    public MultiRenderComponent(RenderComponent ...L) {
        list = new ArrayList<RenderComponent>();
        if (L!=null)
            for (RenderComponent rc : L)
                list.add(rc);
    }
    public void add(RenderComponent renderComponent) {
        list.add(renderComponent);
        ComponentActor actor = getActor();
        if (actor != null) {
            renderComponent.actor = actor;
            renderComponent.enter();
        }
    }
    public void remove(RenderComponent renderComponent) {
        boolean ok = list.remove(renderComponent);
        if (ok && getActor()!=null) {
            renderComponent.exit();
            renderComponent.actor = null;
        }
    }
    public void clear() {
        if (getActor() != null)
            for (RenderComponent rc : list) {
                rc.exit();
                rc.actor = null;
            }
        list.clear();
    }
    public int size() {
        return list.size();
    }
    public RenderComponent get(int index) {
        return list.get(index);
    }

    @Override
    public void enter() {
        super.enter();
        ComponentActor actor = getActor();
        for (RenderComponent rc : list) {
            rc.actor = actor;
            rc.enter();
        }
    }

    @Override
    public void exit() {
        for (RenderComponent rc : list) {
            rc.exit();
            rc.actor = null;
        }
        super.exit();
    }

    @Override
    public void drawC(SpriteBatch batch, float parentAlpha) {
        for (RenderComponent rc : list) {
            rc.drawC(batch, parentAlpha);
        }
    }
}
