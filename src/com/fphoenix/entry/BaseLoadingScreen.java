package com.fphoenix.entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL11;
import com.fphoenix.common.BaseGame;
import com.fphoenix.common.BaseScreen;

public abstract class BaseLoadingScreen extends BaseScreen {
	AssetManager am;
	boolean ok = false;
	public BaseLoadingScreen(BaseGame game) {
		super(game);
		this.am = game.getAssetManager();
		init();
	}
	public void init(){}
	public boolean isLoadingOK(){return ok;}
	public abstract boolean isReady();
	public abstract void switchScreen();
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);
		stage.draw();
		if(!ok)
			ok = am.update();
		stage.act();
		if(isLoadingOK() && isReady()) {
            switchScreen();
        }
	}
}