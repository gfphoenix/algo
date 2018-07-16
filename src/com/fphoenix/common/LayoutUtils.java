package com.fphoenix.common;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.fphoenix.components.Clicker;
import com.fphoenix.components.ComponentActor;
import com.fphoenix.components.HitAnchorBoxComponent;
import com.fphoenix.components.IListener;
import com.fphoenix.components.RenderComponent;
import com.fphoenix.components.ScaleButtonListener;
import com.fphoenix.components.SimpleButtonListener;

/**
 * Created by alan on 18-5-3.
 */

public class LayoutUtils {
    public static <T extends Actor>  T add(Group g, T actor, float x, float y) {
        actor.setPosition(x, y);
        g.addActor(actor);
        return actor;
    }

    public static void addRow(Group g, float x0, float dX, float y, int off, int len, Actor ...actors) {
        if (off<0 || off>=len || off>=actors.length) throw new IllegalArgumentException("invalid off and len");
        for (int i=0, n=actors.length; off<n && i<len; off++,i++) {
            Actor actor = actors[off];
            if (actor!=null) {
                actor.setPosition(x0, y);
                g.addActor(actor);
            }
            x0 += dX;
        }
    }
    public static void addRowCenter(Group g, float xCenter, float dX, float y, int off, int len, Actor... actors) {
        if (len>0) {
            float x0 = xCenter - dX * (len - 1) / 2;
            addRow(g, x0, dX, y, off, len, actors);
        }
    }
    public static void addRow(Group g, float x0, float dX, float y, Actor... actors) {
        for (Actor actor : actors) {
            if (actor!=null) {
                actor.setPosition(x0, y);
                g.addActor(actor);
            }
            x0 += dX;
        }
    }
    public static void addRowCenter(Group g, float xCenter, float dX, float y, Actor... actors) {
        int len = actors.length;
        if (len>0) {
            float x0 = xCenter - dX * (len - 1) / 2;
            addRow(g, x0, dX, y, actors);
        }
    }
    public static void addCol(Group g, float x, float y0, float dY, Actor... actors) {
        for (Actor actor : actors) {
            if (actor!=null) {
                actor.setPosition(x, y0);
                g.addActor(actor);
            }
            y0 += dY;
        }
    }
    public static void addColCenter(Group g, float x, float yCenter, float dY, Actor...actors) {
        int len = actors.length;
        if (len>0) {
            float y0 = yCenter - dY * (len - 1) / 2;
            addCol(g, x, y0, dY, actors);
        }
    }
    public static void addAll(Group g, Actor...actors) {
        for (int i=0,n=actors.length; i<n; i++) {
            g.addActor(actors[i]);
        }
    }

    public static <T extends RenderComponent> T makeBtn(T image, int tag, Clicker clicker) {
        return makeBtn(image, tag, new SimpleButtonListener(), clicker);
    }
    public static <T extends RenderComponent> T makeBtnS(T image, int tag, Clicker clicker, boolean bubbleUp) {
        ScaleButtonListener listener = new ScaleButtonListener();
        listener.setBubbleUp(bubbleUp);
        return makeBtn(image, tag, listener, clicker);
    }
    public static <T extends RenderComponent> T makeBtnS(T image, int tag, Clicker clicker) {
        return makeBtn(image, tag, new ScaleButtonListener(), clicker);
    }
    public static <T extends RenderComponent> T makeBtn(T image, int tag, IListener l, Clicker clicker) {
        ComponentActor ca = image.tryAttachActor();
        ca.setTag(tag);
        ca.setHitable(HitAnchorBoxComponent.CENTER);
        l.setClicker(clicker);
        ca.addListener(l);
        return image;
    }
    public static ComponentActor makeBtn(ComponentActor ca, Clicker clicker, boolean bubbleUp) {
        return makeBtn(ca, new SimpleButtonListener(), bubbleUp, clicker);
    }
    public static ComponentActor makeBtn(ComponentActor ca, Clicker clicker) {
        return makeBtn(ca, new SimpleButtonListener(), clicker);
    }
    public static ComponentActor makeBtn(ComponentActor ca, IListener l, boolean bubbleUp, Clicker clicker) {
        l.setBubbleUp(bubbleUp);
        return makeBtn(ca, l, clicker);
    }
    public static ComponentActor makeBtn(ComponentActor ca, IListener l, Clicker clicker) {
        ca.setHitable(HitAnchorBoxComponent.CENTER);
        l.setClicker(clicker);
        ca.addListener(l);
        return ca;
    }
    public static Action autoScale(float s0, float s1, float dt) {
        Action scale1 = Actions.scaleTo(s1, s1, dt);
        Action scale2 = Actions.scaleTo(s0, s0, dt);
        return Actions.repeat(-1, Actions.sequence(scale1, scale2));
    }
    public static Action autoScale() {
        return autoScale(.95f, 1.05f, 1);
    }
}
