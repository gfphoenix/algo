package com.fphoenix.common.ui.button;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.fphoenix.common.action.SettableProtocol;
import com.fphoenix.common.actor.AnchorActor;

public class MyBaseButton extends AnchorActor implements SettableProtocol {
    private boolean active = true;
    private boolean bubbleUp = false;
    private int tag;
    private int soundID = -1;
    private Clicker clicker;
    protected TextureRegion region = null;
    protected InputListener listener;
    private float initX, initY;
    private float touchRadius = 16;

    public MyBaseButton(boolean active) {
        this.active = active;
        this.setTouchable(Touchable.enabled);
        addHandler();
    }

    public interface Clicker {
        void click(MyBaseButton button, int tag);
    }

    public MyBaseButton() {
        this(true);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected boolean isActive() {
        return active;
    }

    public float getTouchRadius() {
        return touchRadius;
    }

    public void setTouchRadius(float touchRadius) {
        this.touchRadius = touchRadius;
    }

    public void setBubble(boolean enableBubbleUp) {
        this.bubbleUp = enableBubbleUp;
    }
    public MyBaseButton setSoundID(int soundID) {
        this.soundID = soundID;
        return this;
    }

    public int getSoundID() {
        return soundID;
    }

    protected void onUpdateRegion() {
        if (region == null)
            return;
        float w = region.getRegionWidth();
        float h = region.getRegionHeight();
        setSize(w, h);
        float ox = getAnchorX() * w;
        float oy = getAnchorY() * h;
        setOrigin(ox, oy);
    }

    public TextureRegion getTextureRegion() {
        return region;
    }

    public void setTextureRegion(TextureRegion region) {
        this.region = region;
        onRegionChanged();
    }

    public void onRegionChanged() {
        onUpdateRegion();
    }

    public MyBaseButton setClicker(Clicker clicker, int tag) {
        this.clicker = clicker;
        this.tag = tag;
        return this;
    }

    public void onClick() {
//        S.play(soundID);
        if (clicker != null)
            clicker.click(this, tag);
    }

    protected boolean onTouchDown(InputEvent event, float x, float y) {
        initX = event.getStageX();
        initY = event.getStageY();
        return true;
    }
    protected void onTouchMove(InputEvent event, float x, float y) {
    }
    protected boolean onTouchUp(InputEvent event, float x, float y) {
        float dx = event.getStageX() - initX;
        float dy = event.getStageY() - initY;
        return dx*dx + dy*dy <= touchRadius * touchRadius;
    }

    private boolean isBubbleUp() {
        return bubbleUp;
    }

    private void addHandler() {
        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                event.setBubbles(isBubbleUp());
                if (!active)
                    return false;
                return onTouchDown(event, x, y);
//					return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                onTouchMove(event, x, y);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                boolean ok = onTouchUp(event, x, y);
                if (ok && hit(x, y, getTouchable() == Touchable.enabled) != null) {
                    addAction(new Action() {
                        int n = 0;

                        @Override
                        public boolean act(float delta) {
                            // delay a frame, to show a normal state. i.e. button become back.
                            if (n < 1) {
                                n++;
                                return false;
                            }
                            onClick();
                            return true;
                        }
                    });
                }
            }
        });
    }

    @Override
    public void drawMe(SpriteBatch batch, float parentAlpha) {
        if (region == null)
            return;
        float w = getWidth();
        float h = getHeight();

        float x = getX() - w * getAnchorX();
        float y = getY() - h * getAnchorY();
        batch.draw(region, x, y, getOriginX(), getOriginY(), w, h, getScaleX(), getScaleY(), getRotation());
    }
}
