package com.fphoenix.common;


public abstract class ScreenSwitcher extends BaseScreen {

	private int frameCount=0;
	public ScreenSwitcher(BaseGame game) {
		super(game);
		init();
	}
	@Override
	public void render(float delta) {
		super.render(delta);
		if(frameCount == 0)
			System.gc();
		if(frameCount == 1)
			Switch();
		frameCount ++;
	}
	public abstract void Switch();
	public void init(){}
}
