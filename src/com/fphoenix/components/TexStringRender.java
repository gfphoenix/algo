package com.fphoenix.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by alan on 18-5-15.
 */

public class TexStringRender extends RenderComponent {
    protected TextureString map;
    protected CharSequence str;
    private float anchorX = .5f;
    private float anchorY = .5f;

    public TexStringRender setup(TextureString strMap, CharSequence initStr) {
        this.map = strMap;
        this.str = initStr;
        update();
        return this;
    }

    public String getString() {
        return this.str.toString();
    }

    public void setString(CharSequence str) {
        this.str = str;
        update();
    }

    public TextureString getMap() {
        return map;
    }

    public TextureString setMap(TextureString ts) {
        TextureString old = this.map;
        this.map = ts;
        update();
        return old;
    }

    public TexStringRender update() {
        ComponentActor actor = getActor();
        if (actor != null) {
            actor.setSize(getWidth(), getHeight());
        }
        return this;
    }

    @Override
    public void enter() {
        super.enter();
        ComponentActor actor = getActor();
        actor.setSize(getWidth(), getHeight());
    }

    public float getAnchorX() {
        return anchorX;
    }

    public float getAnchorY() {
        return anchorY;
    }

    public TexStringRender setAnchor(float anchorX, float anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        return this;
    }
    public TexStringRender setAnchorX(float anchorX) {
        this.anchorX = anchorX;
        return this;
    }
    public TexStringRender setAnchorY(float anchorY) {
        this.anchorY = anchorY;
        return this;
    }

    public float getWidth() {
        if (str == null || str.length() == 0 || map == null)
            return 0;
        return map.getWidth(str);
    }

    public float getHeight() {
        if (str == null || str.length() == 0 || map == null)
            return 0;
        return map.getHeight();
    }

    public void drawC(SpriteBatch batch, float parentAlpha) {
        if (str == null || str.length() == 0)
            return;
        ComponentActor ca = getActor();
        final float w = ca.getWidth();
        final float h = ca.getHeight();
        final float sx = ca.getScaleX();
        final float sy = ca.getScaleY();
        final float ox = w * getAnchorX();
        final float oy = h * getAnchorY();
        final float x0 = ca.getX() - w * getAnchorX();
        final float y = ca.getY() - h * getAnchorY();
        final int len = str.length();
        float x = x0;
        batch.setColor(ca.getColor());
        batch.mulAlpha(parentAlpha);
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            if (ch == ' ') {
                x += map.getSpaceWidth();
                continue;
            }
            TextureRegion v = map.getTex(ch);
            if (v == null) {
                Gdx.app.log("BUG", "invalid char in TexStringActor::drawC = " + ch);
                continue;
            }
            batch.draw(v, x, y, x0 - x + ox, oy, v.getRegionWidth(), v.getRegionHeight(), sx, sy, ca.getRotation());
            x += v.getRegionWidth();
        }
    }
}
