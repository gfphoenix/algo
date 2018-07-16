package com.fphoenix.common.action;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

/**
 * Created by wuhao on 2/16/17.
 */
public class PathAction extends TemporalAction {
    // start at at(0), and end at at(1)
    public interface Path {
        Vector2 at(float t, Vector2 v2);
    }
    public PathAction(Path path, float duration) {
        super(duration);
        this.path = path;
    }
    public PathAction(Path path, float duration, Interpolation interpolation) {
        super(duration, interpolation);
        this.path = path;
    }
    Path path;
    final Vector2 v2 = new Vector2();
    @Override
    protected void update(float v) {
        path.at(v, v2);
        actor.setPosition(v2.x, v2.y);
    }

    @Override
    public void restart() {
        super.restart();
        path.at(0, v2);
        if (actor!=null)
            actor.setPosition(v2.x, v2.y);
    }
}
