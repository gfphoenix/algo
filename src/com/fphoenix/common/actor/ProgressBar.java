package com.fphoenix.common.actor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class ProgressBar extends AnchorActor {
	TextureRegion background;
	TextureRegion foreground;
	TextureRegion tmp;
	public ProgressBar(TextureRegion background, TextureRegion foreground) {
		this.background = background;
		this.foreground = foreground;
		this.tmp = new TextureRegion(foreground, 0, 0, 0, foreground.getRegionHeight());
	}
	public void setPercent(float percent){
		float t = MathUtils.clamp(percent, 0, 1);
		int w = MathUtils.round(t*foreground.getRegionWidth());
		tmp.setRegionWidth(w);
	}
	@Override
	public void drawMe(SpriteBatch batch, float parentAlpha) {
		if(background==null)
			return;
		float w = background.getRegionWidth();
		float h = background.getRegionHeight();
		float x = getX() - w * getAnchorX();
		float y = getY() - h * getAnchorY();
		batch.draw(background, x, y);
		h = tmp.getRegionHeight();
		y = getY() - h * getAnchorY();
		batch.draw(tmp, x, y);
	}
	@Override
	public float getWidth() {return background.getRegionWidth();}
	@Override
	public float getHeight() {return background.getRegionHeight();}
}
