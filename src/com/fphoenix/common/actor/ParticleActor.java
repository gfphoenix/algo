package com.fphoenix.common.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class ParticleActor extends AnchorActor {
	ParticleEffect effect;
//	Array<ParticleEmitter> emitter;

	public ParticleActor(String config, TextureAtlas ta) {
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal(config), ta);

		effect.start();
	}
	public ParticleActor(ParticleEffect effect, TextureAtlas ta){
		this.effect = new ParticleEffect(effect);
		this.effect.loadEmitterImages(ta);
		this.effect.start();
	}
	@Override
	public void setX(float x) {
		super.setX(x);
		effect.setPosition(x, getY());
	}
	@Override
	public void setY(float y) {
		super.setY(y);
		effect.setPosition(getX(), y);
	}
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		effect.setPosition(x, y);
	}
	
	public void reset(){
		effect.reset();
		setColor(Color.WHITE);
	}
	public void start(){
		effect.start();
	}
	public void allowCompletion(){
		effect.allowCompletion();
	}
	public boolean isComplete(){
		return effect.isComplete();
	}
	public float getDuration(){
		Array<ParticleEmitter> arr = effect.getEmitters();
		float dt=0;
		for (ParticleEmitter e : arr){
			if(dt<e.duration)
				dt = e.duration;
		}
		return dt/1000;
	}
	@Override
	public void drawMe(SpriteBatch batch, float parentAlpha) {
		effect.draw(batch, Gdx.graphics.getDeltaTime());
	}
}
