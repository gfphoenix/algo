package com.fphoenix.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan on 18-3-21.
 */

public class ComponentActor extends Actor {
    Map<String, Object> objectMap;
    RenderComponent _renderComponent;
    Hitable _hit;
    SnapshotArray<BaseBehavior> behaviors = new SnapshotArray<BaseBehavior>();
    float timeScale = 1.0f;

    public ComponentActor reset() {
        objectMap = null;
        _renderComponent = null;
        _hit = null;
        behaviors.clear();

        setName(null);
        setTouchable(Touchable.enabled);
        setVisible(true);
        setScale(1);
        setColor(Color.WHITE);
        setPosition(0,0);
        setSize(0,0);
        setRotation(0);
        remove();

        return this;
    }

    public float getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }

    public Hitable getHitable() {
        return _hit;
    }
    public ComponentActor setHitable(Hitable hitable) {
        this._hit = hitable;
        return this;
    }

    public RenderComponent getRenderComponent() {
        return _renderComponent;
    }

    public void setRenderComponent(RenderComponent renderComponent) {
        if (_renderComponent !=renderComponent) {
            if (_renderComponent !=null) {
                _renderComponent.exit();
                _renderComponent.actor = null;
            }
            if (renderComponent!=null) {
                renderComponent.actor = this;
                renderComponent.enter();
            }
            _renderComponent = renderComponent;
        }
    }
    public <T> T getObject(String key, Class<T> clazz) {
        if (objectMap==null) return null;
        Object object = objectMap.get(key);
        if (clazz.isAssignableFrom(object.getClass())) {
            return (T)object;
        }
        throw new ClassCastException("cannot cast object to "+clazz+" with key="+key);
    }
    public void setObject(String key, Object object) {
        if (objectMap==null) objectMap = new HashMap<String, Object>();
        objectMap.put(key, object);
    }
    public Object removeObject(String key) {
        if (objectMap!=null) {
            return objectMap.remove(key);
        }
        return null;
    }

    private void check_owner(Component c, ComponentActor go) {
        if (c.getActor() != go)
            throw new IllegalStateException("illegal owner state");
    }

    public void addComponent(Component c) {
        check_owner(c, null);
        if (c instanceof RenderComponent) setRenderComponent(((RenderComponent) c));
        else if (c instanceof BaseBehavior) addBehavior(((BaseBehavior) c));
        else throw new IllegalArgumentException("unknown component type");
    }
    public void removeComponent(Component c) {
        if (c instanceof RenderComponent) setRenderComponent(null);
        else if (c instanceof BaseBehavior) removeBehavior(((BaseBehavior) c));
        else throw new IllegalArgumentException("BUG: unknown component type");
    }
    public void addBehavior(BaseBehavior behavior) {
        check_owner(behavior, null);
        behaviors.add(behavior);
        behavior.actor = this;
        behavior.enter();
    }

    // when spine skeleton used, it has two different C, one for drawC, the other for update animation
    // it may double remove a same behavior
    public void removeBehavior(BaseBehavior behavior) {
        if (behavior.actor!=this) return;
        this.behaviors.removeValue(behavior, true);
        behavior.exit();
        behavior.actor = null;
    }
    public <T extends BaseBehavior> T getBehavior(Class<T> clazz) {
        for (int i=0,n=behaviors.size; i<n; i++) {
            BaseBehavior bb = behaviors.get(i);
            if (bb.getClass()==clazz)
                return (T)bb;
        }
        return null;
    }

    public void destroy() {
        for (int i=behaviors.size-1; i>=0; i--) {
            BaseBehavior bb = behaviors.get(i);
            bb.exit();
            bb.actor = null;
        }
        behaviors.clear();
        if (_renderComponent !=null) {
            _renderComponent.exit();
            _renderComponent.actor = null;
        }
    }

    @Override
    public void act(float delta) {
        delta *= timeScale;
        super.act(delta);
        // safe for each, component can be removed in bb.update()
        behaviors.begin();
        BaseBehavior last = null;
        for (int i = behaviors.size-1; i >= 0 && i<behaviors.size; i--) {
            BaseBehavior bb = behaviors.get(i);
            if(bb!=last) {
                if (bb.update(delta)) {
                    behaviors.removeIndex(i);
                }
            }
            last = bb;
        }
        behaviors.end();
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (!touchable || getTouchable()!= Touchable.enabled) return null;
        return _hit != null ? _hit.hit(this, x, y) : super.hit(x, y, touchable);
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (!isVisible()) return;
        RenderComponent rc = _renderComponent;
        if (rc != null)
            rc.draw(batch, parentAlpha);
    }
}
