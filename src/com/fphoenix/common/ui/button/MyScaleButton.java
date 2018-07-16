package com.fphoenix.common.ui.button;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class MyScaleButton extends MyBaseButton {
	public MyScaleButton(TextureRegion tr){
		super();
		setTextureRegion(tr);
	}
	private float oldsx;
	private float oldsy;
	private float scaleFactor=.85f;
	
	public MyScaleButton setScaleFactor(float scale){
		this.scaleFactor = scale;
		return this;
	}

    public float getScaleFactor() {
        return scaleFactor;
    }

    @Override
	protected boolean onTouchDown(InputEvent event, float x, float y) {
		boolean ok = super.onTouchDown(event, x, y);
		oldsx = getScaleX();
		oldsy = getScaleY();
		setScale(scaleFactor);
		return ok;
	}
	@Override
	protected boolean onTouchUp(InputEvent event, float x, float y) {
		setScale(oldsx, oldsy);
		return super.onTouchUp(event, x, y);
	}
}
