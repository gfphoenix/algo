package com.fphoenix.common.action.path;

import com.badlogic.gdx.math.Vector2;
import com.fphoenix.common.action.PathAction;

/**
 * Created by wuhao on 2/16/17.
 */
public class Bezier3 implements PathAction.Path {
    float x0, y0; // start
    float x1, y1;
    float x2, y2; // end
    @Override
    public Vector2 at(float t, Vector2 v2) {
        float t1 = 1-t;
        float a = t1*t1;
        float b = 2*t*t1;
        float c = t*t;
        v2.x = a * x0 + b * x1 + c * x2;
        v2.y = a * y0 + b * y1 + c * y2;
        return v2;
    }
    public Bezier3 setStart(float x, float y) {
        x0 = x;
        y0 = y;
        return this;
    }
    public Bezier3 setEnd(float x, float y) {
        x2 = x;
        y2 = y;
        return this;
    }
    public Bezier3 setPoints(float startX, float startY, float endX, float endY) {
        x0 = startX;
        y0 = startY;
        x1 = endX;
        y1 = endY;
        return this;
    }
    public Bezier3 setControl(float x, float y) {
        x1 = x;
        y1 = y;
        return this;
    }
}
