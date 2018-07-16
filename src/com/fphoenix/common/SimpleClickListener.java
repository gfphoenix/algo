package com.fphoenix.common;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class SimpleClickListener extends InputListener {
	Actor actor;
	public void setActor(Actor a){
		this.actor = a;
	}
	Actor getActor(){
		return actor;
	}
	public SimpleClickListener(Actor a) {
		this.actor = a;
	}
	@Override
	public boolean touchDown(InputEvent event, float x, float y,
			int pointer, int button) {
		event.setBubbles(false);
		onTouchDown();
		return true;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y,
			int pointer, int button) {
		onTouchUp();
		if(actor!=null && actor.hit(x,y,true)!=null)
			onClick();
	}
	protected void onTouchDown(){
	}
	protected void onTouchUp() {
	}
	public void onClick() {
	}
}
