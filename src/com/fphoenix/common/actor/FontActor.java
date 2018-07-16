package com.fphoenix.common.actor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FontActor extends BaseFontStringActor {

	public FontActor(BitmapFont fnt, CharSequence initStr){
		super(fnt, initStr);
	}
	public FontActor(BitmapFont fnt){
		super(fnt,null);
	}
	
	@Override
	public void drawFont(SpriteBatch batch, float parentAlpha) {
		fnt.draw(batch, str, getX()-getWidth()*getAnchorX(), getY()+(1-getAnchorY())*getHeight());
	}
}