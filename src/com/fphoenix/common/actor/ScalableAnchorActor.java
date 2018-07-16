package com.fphoenix.common.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.fphoenix.common.action.SettableProtocol;


public class ScalableAnchorActor extends AnchorActor implements SettableProtocol {
	protected TextureRegion region;
	protected boolean flipX;
	protected boolean flipY;
	public ScalableAnchorActor(TextureRegion region){
		setTextureRegion(region);
		setTouchable(Touchable.disabled);
	}
	public TextureRegion getTextureRegion(){
		return region;
	}
	public void setFlip(boolean flipX, boolean flipY){
		this.flipX = flipX;
		this.flipY = flipY;
	}
	public void setFlipX(boolean flip){
		this.flipX = flip;
	}
	public void setFlipY(boolean flip){
		this.flipY = flip;
	}
	public boolean isFlipX(){return this.flipX;}
	public boolean isFlipY(){return this.flipY;}
	@Override
	protected void sizeChanged() {
		float ox = getAnchorX() * getWidth();
		float oy = getAnchorY() * getHeight();
		setOrigin(ox, oy);
	}
    public void setAlpha(float alpha) {
        Color c = getColor();
        setColor(c.r, c.g, c.b, alpha);
    }
    public void mulAlpha(float scaleAlpha) {
        Color c = getColor();
        setColor(c.r, c.g, c.b, c.a*scaleAlpha);
    }
	@Override
	public void drawMe(SpriteBatch batch, float parentAlpha) {
		if(region==null)
			return ;
		float w = getWidth();
		float h = getHeight();
		float x = getX() - w * getAnchorX();
		float y = getY() - h * getAnchorY();
		boolean fx = region.isFlipX();
		boolean fy = region.isFlipY();
		region.flip(flipX ^ fx, flipY ^ fy);
		batch.draw(region, x, y, getOriginX(), getOriginY(), 
				w, h, getScaleX(), getScaleY(), getRotation());
		region.flip(flipX ^ fx, flipY ^ fy);
	}
    protected void drawHelper(SpriteBatch batch, TextureRegion region, float offX, float offY) {
        if(region==null)
            return ;
        float w = region.getRegionWidth();
        float h = region.getRegionHeight();
        float ox = w * getAnchorX() + offX;
        float oy = h * getAnchorY() + offY;
        float x = getX() - ox;
        float y = getY() - oy;
        batch.draw(region, x, y, ox, oy,
                w, h, getScaleX(), getScaleY(), getRotation());
    }
	public void setTextureRegion(TextureRegion region, boolean reSize){
		this.region = region;
		if(region==null || !reSize)
			return;
		super.setSize(region.getRegionWidth(), region.getRegionHeight());
	}
	@Override
	public void setTextureRegion(TextureRegion region) {
		setTextureRegion(region, true);
	}
}
