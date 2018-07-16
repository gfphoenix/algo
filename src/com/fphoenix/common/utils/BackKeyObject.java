package com.fphoenix.common.utils;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.fphoenix.common.BaseScreen;
import com.fphoenix.common.Utils;
import com.fphoenix.common.actor.LayerGroup;

public class BackKeyObject {
	public interface BackKeyOp{
	void onBack(BaseScreen bs);
	}
	public static void push(BaseScreen bs, BackKeyOp op){
		bs.backOp.add(op);
	}
	public static void pop(BaseScreen bs, BackKeyOp op){
		if(bs.backOp.size>0 && bs.backOp.peek()==op)
			bs.backOp.pop();
	}
	public static class BackKeyLayer extends LayerGroup implements BackKeyOp {
		public BackKeyLayer(){
			addAction(Actions.run(new Runnable() {
				@Override
				public void run() {
					onEnter();
				}
			}));
		}
		public void onEnter(){
			BaseScreen bs = Utils.getBaseGame().getBaseScreen();
			push(bs, this);
		}
		@Override
		public void onBack(BaseScreen bs) {
			pop(bs, this);
		}
	}
}
