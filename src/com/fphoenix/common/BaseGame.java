package com.fphoenix.common;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fphoenix.entry.Audio;
import com.fphoenix.platform.Platform;
import com.fphoenix.platform.PlatformDC;

public class BaseGame extends Game {

	protected AssetManager assetManager;
	protected SpriteBatch  spriteBatch;

//	protected OrthographicCamera cameraOrtho;
	protected BaseScreen currentScreen;
	protected BaseScreen nextScreen=null;
//	protected BaseScreen replaceScreen=null;
	protected boolean disposeOld = true;
	public AssetManager getAssetManager()
	{
		return assetManager;
	}
//	public OrthographicCamera getCameraOrtho()
//	{
//		return cameraOrtho;
//	}
	
	public SpriteBatch getSpriteBatch()
	{
		return spriteBatch;
	}


	@Override
	public void create() {
		assetManager = new AssetManager();
		spriteBatch	= new SpriteBatch();

//		cameraOrtho = new OrthographicCamera();
//		cameraOrtho.setToOrtho(false, Config.width, Config.height);
		
		currentScreen = null;
	}
	
	public void dispose(){
		spriteBatch.dispose();
		assetManager.dispose();
		super.dispose();
	}
	
	@Override
	public final void pause() {
		super.pause();
	}
	
	@Override
	public final void resume() {
		super.resume();
	}
	protected void switchScreen(){
		BaseScreen old = currentScreen;
		currentScreen = nextScreen;
		nextScreen = null;
		super.setScreen(currentScreen);
		
		if(old!=null && disposeOld)
			old.dispose();
	}
	@Override
	public void render() {
		if(nextScreen!=null)
			switchScreen();
		Screen screen = getScreen();
		if(screen == null)
			return;
		screen.render(Gdx.graphics.getDeltaTime());
		if(nextScreen!=null)
			switchScreen();
	}
	public BaseScreen getBaseScreen(){
		return currentScreen;
	}
	
	@Override
	public void setScreen(Screen s){
		BaseScreen screen;
		assert s instanceof BaseScreen;
		
		screen = (BaseScreen)s;
		nextScreen = screen;
		disposeOld = true;
	}
	// set screen but not dispose the old screen
	public BaseScreen replaceScreen(BaseScreen bs){
		nextScreen	= bs;
		disposeOld	= false;
		return currentScreen;
	}
}
