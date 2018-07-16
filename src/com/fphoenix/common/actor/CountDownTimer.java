package com.fphoenix.common.actor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class CountDownTimer extends DigitActor {
	int M=1;
	float count;
	float left;
	boolean pause;
	boolean timeout;
	ScalableAnchorActor rolling = new ScalableAnchorActor(null){
		@Override
		public void act(float delta) {
			if(pause) return;
			super.act(delta);
		}
	};
	public CountDownTimer(BitmapFont fnt) {
		super(fnt);
		init(0);
	}
	public CountDownTimer(BitmapFont fnt, int fracN){
		super(fnt, fracN);
		init(fracN);
	}
	public void setCount(float cc){
		this.count = this.left = cc;
		updateNumber();
	}
	public void setRolling(TextureRegion region){
		rolling.setTextureRegion(region);
	}
	public ScalableAnchorActor getRolling(){return rolling;}
	private void init(int frac) {
		left = 0;
		pause = true;
		timeout = false;
		while(frac>0){
			M*=10;
			frac--;
		}
		rolling.setVisible(false);
		rolling.addAction(Actions.repeat(-1, Actions.rotateBy(-360, 1)));
	}
	
	@Override
	public void setStr(CharSequence str) {
	}
	public void start(){
		pause = false;
		rolling.setVisible(true);
	}
	public void pause(){
		pause = true;
	}
	public void resume(){
		pause = false;
	}
	public void stop(){
		pause = true;
		rolling.setVisible(false);
	}
	public void reset(){
		this.left = this.count;
		this.pause = true;
		updateNumber();
	}
	private void updateNumber() {
		int n = Math.round(this.left*M+.1f);
		setNumber(Math.max(n, 0));
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		if(timeout || pause) return;
		left -= delta;
		updateNumber();
		if(left<0){
			timeout = true;
			onTimeout();
		}
	}
	public void onTimeout(){
	}
}
