package com.fphoenix.common;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.fphoenix.common.utils.BackKeyObject.BackKeyOp;
import com.fphoenix.common.utils.BitMapFontCenter;

public class BaseScreen implements Screen {
	protected Stage stage;
	protected BaseGame game;
	protected Color clearColor = new Color(0f, 0f, 0f, 0f);
	protected Array<Disposable> disposeArray;
	Hub<MessageChannels.Message> hub = new Hub<MessageChannels.Message>();
	public Array<BackKeyOp> backOp = new Array<BackKeyOp>();
	public BaseScreen(BaseGame game) {
		this.game = game;
		this.stage = new Stage(Config.width, Config.height, false,
				game.spriteBatch){
			@Override
			public boolean keyUp(int keyCode) {
				if( keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE)
					InputUtils.setBack();
				return super.keyUp(keyCode);
			}
		};
		this.disposeArray = new Array<Disposable>();
		this.disposeArray.add(stage);
		
		stage.addAction(new Action() {
			@Override
			public boolean act(float delta) {
				onEnter();
				return true;
			}
		});
	}
	public void onEnter(){
	}

	public Hub<MessageChannels.Message> getHub() {
		return hub;
	}

	@Override
	public void render(float delta) {
		stage.act();
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}
	protected void debugDraw0(SpriteBatch batch){
		BitmapFont fnt = BitMapFontCenter.tryGet();
		if(fnt == null){
			return ;
		}
		String f = String.format(Locale.US, "fps=%d, rendercalls=%d",
						Gdx.graphics.getFramesPerSecond(),
						batch.renderCalls);
		float y = fnt.getCapHeight()+4;
		
		batch.begin();
		fnt.draw(batch, f, 10, y);
		batch.end();
	}
	public void debugDraw(){
		BitmapFont fnt = BitMapFontCenter.tryGet();
		if(fnt == null){
			return ;
		}
		float sx = fnt.getScaleX();
		float sy = fnt.getScaleY();
		fnt.setScale(.5f);
		SpriteBatch batch = game.spriteBatch;
		
		debugDraw0(batch);
		
		fnt.setScale(sx, sy);
	}
	public OrthographicCamera getOrthoCamera(){
		Camera c = stage.getCamera();
		if(!(c instanceof OrthographicCamera))
			return null;
		return (OrthographicCamera)c;
	}
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide() {
	}

	@Override
	public final void pause() {
	}

	@Override
	public final void resume() {
	}
	public void addDisposable(Disposable dp){
		if(dp!=null)
			disposeArray.add(dp);
	}
	@Override
	public void dispose() {
		for (Disposable dp :disposeArray){
			dp.dispose();
		}
	}

	public BaseGame getGame() {
		return game;
	}
	public Stage getStage(){
		return stage;
	}
}
