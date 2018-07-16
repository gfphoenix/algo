package com.fphoenix.common.action;

import com.badlogic.gdx.scenes.scene2d.Action;

public class RunOnceAction extends Action {
	Runnable fn;
	public RunOnceAction(Runnable run){
		this.fn = run;
	}
	@Override
	public boolean act(float delta) {
		if(fn!=null)
			fn.run();
		return true;
	}
}
