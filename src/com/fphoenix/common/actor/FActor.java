package com.fphoenix.common.actor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

public class FActor extends Group{

	protected TextureRegion region;
	protected final Vector2 v2 = new Vector2();
	protected float ax=0f;
	protected float ay=0f;
	private float anchorX=.5f;
	private float anchorY=.5f;
	
	public FActor(TextureRegion region){
		setRegion(region);
	}
	public void setRegion(TextureRegion region, boolean needResize) {
		// TODO Auto-generated method stub
		this.region = region;
		if(needResize)
			onUpdateRegion();
	}
	public void setRegion(TextureRegion region){
		setRegion(region, true);
	}
	protected TextureRegion getRegion(){
		return region;
	}
	private void onUpdateRegion(){
		if(region==null)
			return;
		float w = region.getRegionWidth();
		float h = region.getRegionHeight();
		setSize(w, h);
		float ox = getAnchorX() * w;
		float oy = getAnchorY() * h;
		setOrigin(ox, oy);
		updateXY();
	}
	@Override
	public void setX(float x) {
		// TODO Auto-generated method stub
		ax = x;
		updateXY();
	}
	@Override
	public void setY(float y) {
		// TODO Auto-generated method stub
		ay = y;
		updateXY();
	}
	@Override
	public void setPosition(float x, float y) {
		// TODO Auto-generated method stub
		ax = x;
		ay = y;
		updateXY();
	}
	public float getAnchorX() {
		return anchorX;
	}
	public void setAnchorX(float anchorX) {
		this.anchorX = anchorX;
		updateXY();
	}
	public float getAnchorY() {
		return anchorY;
	}
	public void setAnchorY(float anchorY) {
		this.anchorY = anchorY;
		updateXY();
	}
	public void setAnchor(float anchorX, float anchorY){
		this.anchorX	= anchorX;
		this.anchorY	= anchorY;
		updateXY();
	}
	protected void updateXY(){
		float x = ax - getWidth()*getAnchorX();
		float y = ay - getHeight() * getAnchorY();
		super.setPosition(x, y);
	}
	@Override
	public Actor hit (float x, float y, boolean touchable) {
		if (touchable && getTouchable() == Touchable.disabled) return null;
		Array<Actor> children = this.getChildren();
		for (int i = children.size - 1; i >= 0; i--) {
			Actor child = children.get(i);
			if (!child.isVisible()) continue;
			child.parentToLocalCoordinates(v2.set(x, y));
			Actor hit = child.hit(v2.x, v2.y, touchable);
			if (hit != null) return hit;
		}
		return super.hit(x, y, touchable);
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		float or, og, ob, oa;
		or = batch.getColor().r;
		og = batch.getColor().g;
		ob = batch.getColor().b;
		oa = batch.getColor().a;
		batch.setColor(getColor());
		batch.draw(region, getX(), getY());
		super.draw(batch, parentAlpha*getColor().a);
		batch.setColor(or, og, ob, oa);
	}

}
