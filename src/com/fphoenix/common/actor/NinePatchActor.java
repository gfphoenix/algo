package com.fphoenix.common.actor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.fphoenix.common.MyNinePatch;
import com.fphoenix.common.action.SettableProtocol;

public class NinePatchActor extends Actor implements SettableProtocol{
	protected MyNinePatch patch;
	public NinePatchActor(MyNinePatch patch){
		this.patch = patch;
		setSize(patch.getWidth(), patch.getHeight());
	}
	public MyNinePatch getMyNinePatch(){
		return patch;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(getColor());
		batch.mulAlpha(parentAlpha);
		patch.draw(batch, getX(), getY(), (int)getWidth(), (int)getHeight());
	}
	@Override
	public void setTextureRegion(TextureRegion region) {
		// TODO Auto-generated method stub
		
	}
}
