package com.fphoenix.common;

import com.fphoenix.platform.Platform;
import com.fphoenix.platform.PlatformDC;

public class ShopScreen extends BaseScreen {
	private BaseScreen oldBS;
	public ShopScreen(BaseGame game) {
		super(game);
	}
	
	public void onBuy(int idx){
        PlatformDC dc = PlatformDC.get();
        Platform.PlatformBundle bundle = dc.getPlatformBundle();
        bundle.i = idx;
		dc.platform().callPlatform(Platform.CODE_BUY_GOODS, bundle);
	}
	public void updateStatus(){
	}
	
	public static void pushShop(ShopScreen ss){
		BaseGame game = ss.getGame();
		BaseScreen old = game.replaceScreen(ss);
		ss.setOldScreen(old);
	}
	public void setOldScreen(BaseScreen bs){
		this.oldBS = bs;
	}
	public BaseScreen getOldScreen(){
		return this.oldBS;
	}
}
