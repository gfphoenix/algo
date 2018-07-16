package com.fphoenix.common.ui.button;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class MyAutoScaleButton extends MyScaleButton {
	Action action;
	float big=1.05f;
	float small=.95f;
	float dt=1f;
	Action s1;
	Action s2;
	Action seq;
	public MyAutoScaleButton(TextureRegion tr) {
		super(tr);
		init();
	}
	private void init(){
		s1 = Actions.scaleTo(big, big, dt);
		s2 = Actions.scaleTo(small, small, dt);
		seq = Actions.sequence(s1, s2);
		action = Actions.repeat(-1, seq);
		addAction(action);
	}
	@Override
	protected boolean onTouchDown(InputEvent event, float x, float y) {
		removeAction(action);
		return super.onTouchDown(event, x, y);
	}
	@Override
	protected boolean onTouchUp(InputEvent event, float x, float y) {
		init();
		return super.onTouchUp(event, x, y);
	}
	@Override
	public final Actor hit(float x, float y, boolean touchable) {
		if(!touchable || region==null)
			return null;
		float w = region.getRegionWidth()*1.1f+20;
		float h = region.getRegionHeight()*1.1f+20;
//		x += w * getAnchorX();
//		y += h * getAnchorY();
		
		w /=2;
		h /=2;
		if(x>=-w && x<w && y>=-h && y<h){
			return this;
		};
		return null;
	}

}
