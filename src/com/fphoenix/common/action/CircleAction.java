package com.fphoenix.common.action;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class CircleAction extends TemporalAction {
	float w=0;
	float startAngle=0;
	float R=0;
	float cx=0;
	float cy=0;
	public CircleAction(float cx, float cy, float R, float w, float startAngle, float duration){
		super(duration);
		w = (float)(2*Math.PI / duration);
	}
	public CircleAction(float duration){
		super(duration);
		w = (float)(2*Math.PI / duration);
	}
	public CircleAction setCenter(float cx, float cy){
		this.cx = cx;
		this.cy = cy;
		return this;
	}
	public CircleAction setRadius(float R){
		this.R = R;
		return this;
	}
	public CircleAction setW(float w){
		this.w = w;
		return this;
	}
	public float getW(){
		return w;
	}
	public CircleAction setStartAngle(float angle){
		this.startAngle = angle;
		return this;
	}
	@Override
	protected void update(float percent) {
		float t = getDuration() * percent;
		float angle = w * t + startAngle;
		float x = cx + (float)(R * Math.cos(angle));
		float y = cy + (float)(R * Math.sin(angle));
		actor.setPosition(x, y);
	}

}
