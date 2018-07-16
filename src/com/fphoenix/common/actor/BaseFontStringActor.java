package com.fphoenix.common.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseFontStringActor extends AnchorActor {
	
		protected CharSequence str;
		protected BitmapFont fnt;
		
		
		public BaseFontStringActor(BitmapFont fnt, CharSequence initStr){
			this.fnt = fnt;
			this.str = initStr;
			resetSize();
		}
		public BaseFontStringActor(BitmapFont fnt){
			this(fnt,null);
		}
		public void resetSize(){
			if(str==null){
				setSize(0, 0);
				return;
			}
			float oldx = fnt.getScaleX();
			float oldy = fnt.getScaleY();
			fnt.setScale(getScaleX(), getScaleY());
			BitmapFont.TextBounds b = fnt.getWrappedBounds(str,0);
			setSize(b.width, b.height);
			fnt.setScale(oldx, oldy);
		}
		public BitmapFont getFnt() {
			return fnt;
		}
		public void setFnt(BitmapFont fnt) {
			this.fnt = fnt;
		}

		public void setStr(CharSequence str){
			this.str = str;
			resetSize();
		}
		public CharSequence getStr(){
			return this.str;
		}
		@Override
		public void setScale(float scale) {
			super.setScale(scale);
			resetSize();
		}
		@Override
		public void setScale(float scaleX, float scaleY) {
			super.setScale(scaleX, scaleY);
			resetSize();
		}
		@Override
		public void setScaleX(float scaleX) {
			super.setScaleX(scaleX);
			resetSize();
		}
		@Override
		public void setScaleY(float scaleY) {
			super.setScaleY(scaleY);
			resetSize();
		}
		public abstract void drawFont(SpriteBatch batch, float parentAlpha);
		@Override
		public final void drawMe(SpriteBatch batch, float parentAlpha) {
			if(str==null || str.equals(""))
				return;
			float oldsx = fnt.getScaleX();
			float oldsy = fnt.getScaleY();
			Color old = fnt.getColor();
			
			Color c = getColor();
			float r = c.r;
			float g = c.g;
			float b = c.b;
			float a = c.a * parentAlpha;
			
			fnt.setColor(r, g, b, a);
			fnt.setScale(getScaleX(), getScaleY());
			drawFont(batch, parentAlpha);
			fnt.setScale(oldsx, oldsy);
			fnt.setColor(old);
		}
	}