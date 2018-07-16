package com.fphoenix.components;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class IListener extends InputListener {
	private Clicker clicker;
	boolean bubbleUp = false;
	protected boolean active = true;
	public void setActive(boolean active){
		this.active = active;
	}
	public boolean isActive(){return this.active;}
	public IListener setClicker(Clicker clicker) {
		this.clicker = clicker;
		return this;
	}

	public boolean isBubbleUp() {
		return bubbleUp;
	}

	public void setBubbleUp(boolean bubbleUp) {
		this.bubbleUp = bubbleUp;
	}

	public boolean onTouchDown(InputEvent e, float x, float y) {
		return active;
	}

	public boolean onTouchUp(InputEvent e, float x, float y) {
		return true;
	}

	public void onClick(Actor actor) {
		if (clicker!=null)
			clicker.click(actor);
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer,
			int button) {
		boolean ok = active && onTouchDown(event, x, y);
		if (ok) event.setBubbles(isBubbleUp());
		return ok;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer,
			int button) {
		boolean ok = onTouchUp(event, x, y);
		final Actor a = event.getTarget();
		if (!ok || a == null)
			return;
		a.addAction(new Action() {
			int n = 0;
			@Override
			public boolean act(float delta) {
				// delay a frame, to show a normal state. i.e. button become
				// back.
				if (n < 1) {
					n++;
					return false;
				}
				onClick(actor);
				return true;
			}
		});
	}
}
