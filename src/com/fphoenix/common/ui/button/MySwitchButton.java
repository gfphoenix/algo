package com.fphoenix.common.ui.button;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class MySwitchButton extends MyBaseButton {
	private TextureRegion normal;
	private TextureRegion selected;
	private TextureRegion disabled = null;

	public MySwitchButton(TextureRegion normal, TextureRegion selected,
			TextureRegion disabled, boolean active) {
		this.normal = normal;
		this.selected = selected;
		this.disabled = disabled;
		setActive(active);
		if (!active)
			setTextureRegion(disabled);
		else
			setTextureRegion(normal);
	}

	public MySwitchButton(TextureRegion normal) {
		this(normal, normal, null, true);
	}

	public MySwitchButton(TextureRegion normal, TextureRegion selected) {
		this(normal, selected, null, true);
	}

	public MySwitchButton(TextureRegion normal, TextureRegion selected,
			TextureRegion disabled) {
		this(normal, selected, disabled, true);
	}
	public void setTextures(TextureRegion normal){
		setTextures(normal, null);
	}
	public void setTextures(TextureRegion normal, TextureRegion selected){
		setTextures(normal, selected, null);
	}
	public void setTextures(TextureRegion normal, TextureRegion selected, TextureRegion disabled){
		TextureRegion r = getTextureRegion();
		if(r==this.normal){
			setTextureRegion(normal);
		}else if(r==this.selected){
			setTextureRegion(selected);
		}else{
			setTextureRegion(disabled);
		}
		this.normal = normal;
		this.selected = selected;
		this.disabled = disabled;
	}

	public void setActive(boolean active) {
		if (isActive() ^ active) {
			if (active)
				setTextureRegion(normal);
			else
				setTextureRegion(disabled);
		}
		super.setActive(active);
	}
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		return hit2(x, y, touchable, 1.2f);
	}
	@Override
	public void onClick() {
	}
	@Override
	protected boolean onTouchDown(InputEvent event, float x, float y) {
		setTextureRegion(selected);
		return super.onTouchDown(event, x, y);
	}
	protected boolean onTouchUp(InputEvent event, float x, float y) {
		setTextureRegion(normal);
		return super.onTouchUp(event, x, y);
	};
	@Override
	public float getWidth() {
		return region.getRegionWidth()*getScaleX();
	}
	@Override
	public float getHeight() {
		return region.getRegionHeight()*getScaleX();
	}
}
