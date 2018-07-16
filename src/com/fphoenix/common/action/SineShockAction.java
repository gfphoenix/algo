package com.fphoenix.common.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SineShockAction extends Action {
//	private float phi=0f;
//	private float phiOff;
//	private float w;
	private float offX;
	private float offY;
	private float dt;
	private float t=0;
	private float oldx;
	private float oldy;
	
	private boolean begin=true;
	private boolean complete=false;
	
	public SineShockAction(float offX, float offY, float duration, int times, float phi){
		this.offX	= offX;
		this.offY	= offY;
//		this.phi	= phi;
		this.dt		= duration;
		if(times<=0)
			times = 1;
//		this.w		= (float) (2*Math.PI*times /duration);
//		this.phiOff = (float) -Math.sin(phi);
		this.begin	= true;
		this.complete = false;
	}
	public SineShockAction(float offX, float offY, float duration, int times){
		this(offX, offY, duration, times, 0);
	}
	public SineShockAction(float offX, float offY, float duration, float phi){
		this(offX, offY, duration, 1, phi);
	}
	public SineShockAction(float offX, float offY, float duration){
		this(offX, offY, duration, 1, 0);
	}
	private void begin() {
		Actor a = getActor();
		this.oldx	= a.getX();
		this.oldy	= a.getY();
		this.begin	= false;
	}
	private void complete(){
//		actor.setPosition(oldx, oldy);
		complete	= true;
	}
	@Override
	public boolean act(float delta) {
		if(begin){
			begin();
		}
		if(complete)
			return true;
		t	+= delta;
//		float xx = w*t + phi;
//		float s		= MathUtils.sin(xx)+phiOff;
//		actor.setPosition(oldx+offX*s, oldy+offY*s);
		
		float s = bounceTime(t/dt);
		actor.setPosition(oldx+offX*s, oldy+offY*s);
		
		if(t>=dt){
			complete();
		}
		return false;
	}
	
	float bounceTime(float time)
	{
	    if (time < 1 / 2.75)
	    {
	        return 7.5625f * time * time;
	    }
	    else if (time < 2 / 2.75)
	    {
	        time -= 1.5f / 2.75f;
	        return 7.5625f * time * time + 0.75f;
	    }
	    else if(time < 2.5 / 2.75)
	    {
	        time -= 2.25f / 2.75f;
	        return 7.5625f * time * time + 0.9375f;
	    }

	    time -= 2.625f / 2.75f;
	    return 7.5625f * time * time + 0.984375f;
	}

}
