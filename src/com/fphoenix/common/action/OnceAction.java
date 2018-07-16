package com.fphoenix.common.action;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;

public abstract class OnceAction extends Action {
	private boolean began=false;
	
	protected abstract boolean isComplete();
	protected boolean isBegan(){return began;}
	protected void setBegan(boolean b){began=b;}
	@Override
	public boolean act(float delta) {
		// TODO Auto-generated method stub
		if(isComplete())
			return true;
		if(!isBegan()){
			setBegan(true);
			begin();
		}
		update(delta);
		if(isComplete()){
			end();
			return true;
		}		
		return false;
	}
	protected void begin()	{}
	protected void end(){}
	protected void update(float delta){}

	public static class NAction extends OnceAction {
		private int n=0;
		@Override
		protected boolean isComplete() {
			// TODO Auto-generated method stub
			return n<=0;
		}
		private Runnable fn;
		public NAction(int n, Runnable run){
			this.n = n;
			this.fn = run;
		}
		@Override
		protected void update(float delta) {
			// TODO Auto-generated method stub
			if(fn!=null)
				fn.run();
			n--;
		}
	}
	public static class TAction extends OnceAction {
		private float time=0f;
		private float duration=0f;
		
		public TAction(float duration, Runnable run){
			this.duration = duration;
			this.fn = run;
		}
		@Override
		protected boolean isComplete() {
			// TODO Auto-generated method stub
			return time>=duration;
		}
		private Runnable fn;
		public float getTime(){
			return time;
		}
		@Override
		protected void update(float delta) {
			// TODO Auto-generated method stub
			if(fn!=null)
				fn.run();
			time += delta;
		}
	}
	public static class PercentAction extends OnceAction {
		float time;
		float dt;
		Interpolation inter;
//		private Runnable fn;
		@Override
		protected void update(float delta) {
			// TODO Auto-generated method stub
			time += delta;
			float per = time/dt;
			if(inter!=null)
				per = inter.apply(per);
			run(per);
		}
		protected void run(float percent){
			
		}
		@Override
		protected boolean isComplete() {
			// TODO Auto-generated method stub
			return time>=dt;
		}
		
		public PercentAction(float duration){
			dt = duration;
		}
		public PercentAction(float duration, Interpolation interpolation){
			dt = duration;
			this.inter = interpolation;
		}
	}

}
