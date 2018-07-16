package com.fphoenix.common.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.fphoenix.common.TextureString;

public class TexDigiActor extends TexStringActor {
	int dest;
	int showValue;
	int fracN=-1; // 小数部分, -1表示不显示，>=0，表示显示的小数位数： 2. 2.0, 2.01
	StringBuffer sb = new StringBuffer(16);
	public TexDigiActor(TextureString ts) {
		super(ts);
	}
	public TexDigiActor(TextureString ts, int fracN){
		super(ts);
		this.fracN = fracN;
	}
	@Override
	public void setString(CharSequence str) {
	}
	public int getShowValue(){return showValue;}
	public int getNumber(){return dest;}
	private void setShowNumber(int n){
		this.showValue = n;
		if(fracN<0){
			this.str = ""+showValue;
		}else{
			int w = fracN;
			int i = showValue;
			sb.setLength(0);
			while(w-->0){
				i/=10;
			}
			sb.append(i);
			sb.append('.');
			w = fracN;
			int idx = sb.length();
			i = showValue;
			while(w-->0){
				sb.insert(idx, i%10);
				i /= 10;
			}
			str = sb.toString();
		}
//		resetSize();
	}
	public void setNumber(int n){
		this.dest = n;
		setShowNumber(n);
	}
	public void setNumber(int N, final float duration){
		this.dest = N;
		final int d = this.dest-this.showValue;
		final int initShowValue = this.showValue;
		clearActions();
		addAction(new Action() {
			float elapsed=0;
			@Override
			public boolean act(float delta) {
				elapsed += delta;
				float percent = elapsed/duration;
				percent = MathUtils.clamp(percent, 0, 1);
				showValue = initShowValue + (int)Math.round(d * percent);
				boolean done = percent >= 1;
				if(done)
					showValue = dest;
				setShowNumber(showValue);
				return done;
			}
		});
	}
}
