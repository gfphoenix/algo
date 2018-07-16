package com.fphoenix.common.actor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FontWrapActor extends BaseFontStringActor {

	private float wrapWidth;
	TextBounds bounds = new TextBounds();
	public FontWrapActor(BitmapFont fnt) {
		super(fnt);
	}
	public FontWrapActor(BitmapFont fnt, String initStr){
		super(fnt, initStr);
	}
	public float getWrapWidth(){
		return wrapWidth;
	}
	public void setWrapWidth(float wrapWidth){
		this.wrapWidth = wrapWidth;
		updateBounds();
	}
	private void updateBounds() {
		if(str==null || str.length()==0){
			bounds.width = bounds.height = 0;
			return;
		}
		fnt.getWrappedBounds(getStr(), wrapWidth, bounds);
	}
	@Override
	public void setStr(CharSequence text) {
		super.setStr(text);
		updateBounds();
	}

	@Override
	public void drawFont(SpriteBatch batch, float parentAlpha) {
		float x = getX() - bounds.width * getAnchorX();
		float y = getY()+(1-getAnchorY())*bounds.height;
		fnt.drawWrapped(batch, str, x, y, wrapWidth);
	}

}
