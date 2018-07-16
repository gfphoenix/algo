package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.esotericsoftware.spine.SkeletonRenderer;

/**
 * Created by wuhao on 3/1/17.
 */
public class PolyGroup extends Group {
    public interface PolyDrawable {
        void drawPoly(PolygonSpriteBatch batch, SkeletonRenderer renderer);
    }

    public PolyGroup() {
        setTransform(false);
    }

    public PolygonSpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(PolygonSpriteBatch batch) {
        this.batch = batch;
    }

    public SkeletonRenderer getRenderer() {
        return renderer;
    }

    PolygonSpriteBatch batch;
    final SkeletonRenderer renderer = new SkeletonRenderer();

    @Override
    public final void draw(SpriteBatch batch, float parentAlpha) {
        boolean t = isTransform();
        if (t) {
            this.applyTransform(batch, this.computeTransform());
        }
        batch.end();
        this.batch.setProjectionMatrix(batch.getProjectionMatrix());
        this.batch.setTransformMatrix(batch.getTransformMatrix());
        this.batch.setColor(getColor());
        if (!this.batch.isBlendingEnabled()) {
            this.batch.enableBlending();
        }
        this.batch.begin();

        drawPolygons(parentAlpha);

        this.batch.end();
        batch.begin();
        if (t) {
            this.resetTransform(batch);
        }
    }

    // begin and end are set
    protected void drawPolygons(float parentAlpha) {
        parentAlpha *= this.getColor().a;
        SnapshotArray<Actor> children = this.getChildren();
        Actor[] actors = children.begin();
        Rectangle cullingArea = null;
        boolean transform = isTransform();
        if (cullingArea != null) {
            // Draw children only if inside culling area.
            float cullLeft = cullingArea.x;
            float cullRight = cullLeft + cullingArea.width;
            float cullBottom = cullingArea.y;
            float cullTop = cullBottom + cullingArea.height;
            if (transform) {
                for (int i = 0, n = children.size; i < n; i++) {
                    Actor child = actors[i];
                    if (!child.isVisible()) continue;
                    float cx = child.getX(), cy = child.getY();
                    if (cx <= cullRight && cy <= cullTop && cx + child.getWidth() >= cullLeft && cy + child.getHeight() >= cullBottom)
                        drawChild(child, parentAlpha);
                }
            } else {
                // No transform for this group, offset each child.
                float offsetX = getX(), offsetY = getY();
                setPosition(0, 0);
                for (int i = 0, n = children.size; i < n; i++) {
                    Actor child = actors[i];
                    if (!child.isVisible()) continue;
                    float cx = child.getX(), cy = child.getY();
                    if (cx <= cullRight && cy <= cullTop && cx + child.getWidth() >= cullLeft && cy + child.getHeight() >= cullBottom) {
                        child.setPosition(cx + offsetX, cy + offsetY);
                        drawChild(child, parentAlpha);
                        child.setPosition(cx, cy);
                    }
                }
                setPosition(offsetX, offsetY);
            }
        } else {
            // No culling, drawC all children.
            if (transform) {
                for (int i = 0, n = children.size; i < n; i++) {
                    Actor child = actors[i];
                    if (!child.isVisible()) continue;
                    drawChild(child, parentAlpha);
                }
            } else {
                // No transform for this group, offset each child.
                float offsetX = getX(), offsetY = getY();
                setPosition(0, 0);
                for (int i = 0, n = children.size; i < n; i++) {
                    Actor child = actors[i];
                    if (!child.isVisible()) continue;
                    float cx = child.getX(), cy = child.getY();
                    child.setPosition(cx + offsetX, cy + offsetY);
                    drawChild(child, parentAlpha);
                    child.setPosition(cx, cy);
                }
                setPosition(offsetX, offsetY);
            }
        }
        children.end();
    }

    @Override
    public void addActor(Actor actor) {
        if (actor instanceof PolyDrawable) {
            super.addActor(actor);
        } else if (actor instanceof ComponentActor) {
            Component c = ((ComponentActor) actor).getRenderComponent();
            if (c instanceof SkeletonComponent)
                super.addActor(actor);
            else {
                throw new RuntimeException("not skeleton component");
            }
        } else {
            throw new RuntimeException("unknown actor to render by poly-group");
        }
    }

    protected void drawChild(Actor actor, float parentAlpha) {
        if (actor instanceof PolyDrawable)
            ((PolyDrawable) actor).drawPoly(batch, renderer);
        else if (actor instanceof ComponentActor) {
            drawComponentActor(((ComponentActor) actor));
        }
    }

    protected void drawComponentActor(ComponentActor actor) {
        Component c = actor.getRenderComponent();
        if (c instanceof PolyDrawable)
            ((PolyDrawable) c).drawPoly(batch, renderer);
    }
}