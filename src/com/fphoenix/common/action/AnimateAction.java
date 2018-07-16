package com.fphoenix.common.action;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Array;

public class AnimateAction extends TemporalAction {
	
	private Array<TextureRegion> list;
	SettableProtocol set_;
	TextureRegion old;
	boolean flipX=false;
	@Override
	protected void update(float percent) {
		if(set_ == null)
			return;
		int idx = (int)(percent*list.size);
		if(idx>=list.size){
			idx = list.size-1;
		}
		TextureRegion r = list.get(idx);
		set_.setTextureRegion(r);
	}
	public AnimateAction(float duration){
		super(duration);
		list = new Array<TextureRegion>();
	}
	public AnimateAction(float duration, final Array<TextureRegion> arr){
		super(duration);
		list = arr;
		for(TextureRegion r : list){
			r.flip(flipX ^ r.isFlipX(), false);
		}
	}
	public void setFlipX(boolean flipX){
		this.flipX=flipX;
		if(list!=null)
		for(TextureRegion r : list){
			r.flip(flipX ^ r.isFlipX(), false);
		}
	}
	@Override
	protected void begin() {
		super.begin();
		Actor a = getActor();
		if(a instanceof SettableProtocol){
			set_ = (SettableProtocol)a;
		}
	}
	public void addFrame(TextureRegion region){
		region.flip(flipX^region.isFlipX(), false);
		list.add(region);
	}
	public void addFrames(final TextureRegion []arr){
		for(final TextureRegion r : arr){
			r.flip(flipX^r.isFlipX(), false);
			list.add(r);
		}
	}
	public void addFrames(final Array<TextureRegion> arr){
		for(final TextureRegion r : arr){
			if(r!=null){
				r.flip(flipX^r.isFlipX(), false);
				list.add(r);
			}
		}
	}
	public void setFrames(Array<TextureRegion> arr){
		list = arr;
		for(TextureRegion r : list){
			r.flip(flipX ^ r.isFlipX(), false);
		}
	}
	public AnimateAction copy(){
		AnimateAction aa = new AnimateAction(getDuration(), list);
		return aa;
	}
}
