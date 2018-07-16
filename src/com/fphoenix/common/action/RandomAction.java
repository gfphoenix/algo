package com.fphoenix.common.action;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class RandomAction {

	public static class RandomCircleAction extends TemporalAction {
		@Override
		protected void update(float percent) {
			// TODO Auto-generated method stub
			float len = len();
			float d = degree();
			float dx = (float) (len * Math.cos(d));
			float dy = (float) (len * Math.sin(d));
			actor.setPosition(dx+x, dy+y);
		}
		private float radius;
		private float x;
		private float y;
		private float len(){
			return (float)Math.random()*radius;
		}
		private float degree(){
			return (float) (2*Math.random()*Math.PI);
		}
		public RandomCircleAction(float radius, float duration){
			super(duration);
			this.radius = radius;
		}
		public RandomCircleAction(float radius){
			super();
			this.radius = radius;
		}
		@Override
		protected void begin() {
			// TODO Auto-generated method stub
			super.begin();
			x = actor.getX();
			y = actor.getY();
		}
		@Override
		protected void end() {
			// TODO Auto-generated method stub
			super.end();
			actor.setPosition(x, y);
		}
		
	}
	public static class RandomRectAction extends TemporalAction {
		private float x0;
		private float y0;
		private float w;
		private float h;
		
		@Override
		protected void begin() {
			// TODO Auto-generated method stub
			super.begin();
			x0 = actor.getX();
			y0 = actor.getY();
		}
		@Override
		protected void end() {
			// TODO Auto-generated method stub
			super.end();
			actor.setPosition(x0, y0);
		}
		@Override
		protected void update(float percent) {
			// TODO Auto-generated method stub
			float dx = (float) (Math.random()*2*w - w);
			float dy = (float) (Math.random()*2*h - h);
			actor.setPosition(x0+dx, y0+dy);
		}
		public RandomRectAction(float w, float h){
			super();
			init(w,h);
		}
		public RandomRectAction(float w, float h, float duration){
			super(duration);
			init(w,h);
		}
		private void init(float w, float h){
			this.w = Math.abs(w);
			this.h = Math.abs(h);
		}
	}

}
