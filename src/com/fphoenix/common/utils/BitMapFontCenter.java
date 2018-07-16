package com.fphoenix.common.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.fphoenix.common.Utils;
import com.fphoenix.entry.Assets;
import com.fphoenix.platform.PlatformDC;

public class BitMapFontCenter {
	public static BitmapFont tryGet(){
		BitmapFont tmp = PlatformDC.get().tryGetBMF();
		if(tmp!=null) return tmp;
		tmp = Utils.getBaseGame().getAssetManager().get(Assets.font1);
		return tmp;
	}
	public static BitmapFont getNormalFont(){
		
		BitmapFont bf = PlatformDC.get().tryGetBMF();
		if(bf==null){
			AssetManager am = Utils.getBaseGame().getAssetManager();
			am.load(Assets.font1, BitmapFont.class);
			am.finishLoading();
			bf = am.get(Assets.font1);
			bf.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
//			PlatformDC.get().setBMF(bf);
		}
		return bf;
	}
}
