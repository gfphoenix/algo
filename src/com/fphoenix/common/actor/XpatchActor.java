package com.fphoenix.common.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.fphoenix.common.Xpatch;

public class XpatchActor extends Actor {
	private Xpatch patch;
	public XpatchActor(Xpatch patch){
		this.patch = patch;
	}
	public Xpatch getXpatch(){
		return patch;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
//        float w = getWidth() * getScaleX();
//        float h = getHeight() * getScaleY();
//		patch.drawC(batch, getX(), getY(), w, getRotation(), h);
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a*parentAlpha);
        patch.draw(batch, getX(), getY(), getWidth(), getScaleX(), getScaleY(), getRotation());
	}

	public void setAnchorX(float ax){
		patch.setAnchorX(ax);
	}
	public void setAnchorY(float ay) {
		patch.setAnchorY(ay);
	}
}
