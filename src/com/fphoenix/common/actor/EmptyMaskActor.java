package com.fphoenix.common.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.fphoenix.common.Config;

public class EmptyMaskActor extends AnchorActor {
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		return this;
	}
	public EmptyMaskActor(){
		addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				event.setBubbles(false);
				return true;
			}
		});
	}
	TextureRegion mask=null;
	public void setMaskRegion(TextureRegion region){
		mask = region;
	}
	@Override
	public void drawMe(SpriteBatch batch, float parentAlpha) {
		if(mask==null)
			return;
		float w = Math.max(Config.width, Gdx.graphics.getWidth());
		float h = Math.max(Config.height,Gdx.graphics.getHeight());
		batch.draw(mask, 0, 0, w, h);
	}
}
