package com.fphoenix.common.actor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;

public class DigitActor extends FontActor {
	long dest;
	long showValue;
	int fracN=-1; // 小数部分, -1表示不显示，>=0，表示显示的小数位数： 2. 2.0, 2.01
	StringBuffer sb = new StringBuffer(16);
	public DigitActor(BitmapFont fnt) {
		super(fnt);
	}
	public DigitActor(BitmapFont fnt, int fracN){
		super(fnt);
		this.fracN = fracN;
	}

	public long getShowValue(){return showValue;}
	public long getNumber(){return dest;}
	private void setShowNumber(long n){
		this.showValue = n;
		if(fracN<0){
			this.str = ""+showValue;
		}else{
			int w = fracN;
			long i = showValue;
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
		resetSize();
	}
	public void setNumber(long n){
		this.dest = n;
		setShowNumber(n);
	}
	public void onDone(){
	}
	public void setNumber(long N, final float duration){
		if(duration<=Float.MIN_VALUE){
			setNumber(N);
			return;
		}
		this.dest = N;
		final long d = this.dest-this.showValue;
		final long initShowValue = this.showValue;
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
				if(done){
					showValue = dest;
					onDone();
				}
				setShowNumber(showValue);
				return done;
			}
		});
	}
}
