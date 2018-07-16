package com.fphoenix.common.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fphoenix.common.TextureString;

public class TexStringActor extends AnchorActor {
	protected TextureString map;
	protected CharSequence str;
	public TexStringActor(TextureString strMap, CharSequence initStr){
		this.map	= strMap;
		this.str	= initStr;
	}
	public TexStringActor(TextureString strMap){
		this(strMap, null);
	}
	public String getString(){
		return this.str.toString();
	}
	public void setString(CharSequence str){
		this.str = str;
	}
    public TextureString getMap() {
        return map;
    }
    public TextureString setMap(TextureString ts) {
        TextureString old = this.map;
        this.map = ts;
        return old;
    }

	@Override
	public float getWidth() {
		if(str==null || str.length()==0 || map==null)
			return 0;
		return map.getWidth(str);
	}
	@Override
	public float getHeight() {
		if(str==null || str.length()==0 || map==null)
			return 0;
		return map.getHeight();
	}
	public void drawMe(SpriteBatch batch, float parentAlpha){
		if(str==null || str.length()==0)
			return;
		final float w = getWidth();
		final float h = getHeight();
		final float sx = getScaleX();
		final float sy = getScaleY();
		final float ox = w * getAnchorX();
		final float oy = h * getAnchorY();
		final float x0 = getX() - w * getAnchorX();
		final float y = getY() - h * getAnchorY();
		final int len = str.length();
		float x = x0;
//        batch.setColor(getColor());
		for(int i=0; i<len; i++){
			char ch = str.charAt(i);
			if(ch == ' '){
				x += map.getSpaceWidth();
				continue;
			}
			TextureRegion v = map.getTex(ch);
			if(v==null){
				Gdx.app.log("BUG", "invalid char in TexStringActor::drawC = "+ch);
				continue;
			}
			batch.draw(v, x, y, x0-x+ox, oy, v.getRegionWidth(), v.getRegionHeight(), sx, sy, getRotation());
			x += v.getRegionWidth();
		}
	}
	public void drawMe2(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		if(str==null || str.length()==0)
			return;
		final float w = getWidth();
		final float h = getHeight();
		final float sx = getScaleX();
		final float sy = getScaleY();
		final int len = str.length();
		float x = getX()-w*sx*getAnchorX();
		final float y = getY()-h*sy*getAnchorY();
		final float hh = h * sy;
		for(int idx=0; idx<len; idx++){
			char ch = str.charAt(idx);
			if(ch==' '){
				x += map.getSpaceWidth();
				continue;
			}
			TextureRegion v = map.getTex(ch);
			if(v==null){
				Gdx.app.log("BUG", "invalid char in TexStringActor::drawC = "+ch);
				continue;
			}
			final float ww = v.getRegionWidth()*sx;
			batch.draw(v, x, y, ww, hh);
			x += ww;
		}
	}
}
