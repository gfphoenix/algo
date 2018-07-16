package com.fphoenix.common.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;

public class LayerGroup extends Group {
	public LayerGroup(){
		super();
		setTransform(false);
	}
	@Override
	public void setRotation(float degrees){error();}
	@Override
	public void setScale(float scale) {error();}
	@Override
	public void setScale(float scaleX, float scaleY) {error();}
	@Override
	public void setScaleX(float scaleX) {error();}
	@Override
	public void setScaleY(float scaleY) {error();}
	
	private void error(){
		Gdx.app.log("ERROR", "should not set rotation or scale in LayerGroup");
	}
}
