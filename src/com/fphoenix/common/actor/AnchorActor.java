package com.fphoenix.common.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnchorActor extends Actor {
	private float anchorX=.5f;
	private float anchorY=.5f;
	
	public float getAnchorX() {
		return anchorX;
	}
	public void setAnchorX(float anchorX) {
		this.anchorX = anchorX;
	}
	public float getAnchorY() {
		return anchorY;
	}
	public void setAnchorY(float anchorY) {
		this.anchorY = anchorY;
	}
	public void setAnchor(float anchorX, float anchorY){
		this.anchorX	= anchorX;
		this.anchorY	= anchorY;
	}
	public Actor hit2(float x, float y, boolean touchable, float s) {
		if(!touchable)
			return null;
		float w = getWidth()*s;
		float h = getHeight()*s;
		boolean ok = Math.abs(x)<=w/2 && Math.abs(y)<=h/2;
		return ok ? this : null;
	}
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if(!touchable)
			return null;
		x += getWidth()*anchorX;
		y += getHeight()*anchorY;
		return super.hit(x, y, touchable);
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if(!isVisible()) return;
		Color old = batch.getColor();
		float r = old.r;
		float g = old.g;
		float b = old.b;
		float a = old.a;
		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a*parentAlpha);
		drawMe(batch, parentAlpha);
		batch.setColor(r,g,b,a);
	}
	public void drawMe(SpriteBatch batch, float parentAlpha) {}
}
