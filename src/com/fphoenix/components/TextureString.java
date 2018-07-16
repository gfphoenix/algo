package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// all TextureRegions must have the same height, width may not equal
public class TextureString {
	public  float spaceWidth=0;
	
	private Map<Character, TextureRegion> map= new HashMap<Character, TextureRegion>();
	private int height=-1;
	public float getSpaceWidth(){
		return spaceWidth;
	}
	public boolean add(char ch, TextureRegion tex){
		assert height == -1 || height == tex.getRegionHeight();
		if(tex ==null)
			return false;
		height = Math.max(height, tex.getRegionHeight());
		spaceWidth = (spaceWidth*map.size()+tex.getRegionWidth())/(map.size()+1);
		map.put(ch, tex);
        return true;
	}
	public boolean isValid(String str){
		Set<Character> s = map.keySet();
		for(int i=0; i<str.length(); i++){
			char x = str.charAt(i);
			if(x!=' ' && !s.contains(x))
				return false;
		}
		return true;
	}
	public boolean isValidChar(char ch){
		return ch==' ' || map.keySet().contains(ch);
	}
	public boolean isValid(final char [] str){
		Set<Character> s = map.keySet();
		for(int i=0; i<str.length; i++){
			char x = str[i];
			if(x!=' ' && !s.contains(x))
				return false;
		}
		return true;
	}
	public int getWidth(CharSequence str){
		int w = 0;
		for(int i=0; i<str.length(); i++){
			char ch = str.charAt(i);
			if(ch==' '){
				w += spaceWidth;
				continue;
			}
			TextureRegion tex = map.get(ch);
			if(tex!=null)
				w += tex.getRegionWidth();
		}
		return w;
	}
	public int getHeight(){
		return height;
	}
	public TextureRegion getTex(char ch){
		return map.get(ch);
	}

    public TextureString addDigits(TextureAtlas ta, String prefix) {
        for (int i=0; i<=9; i++) {
            char ch = (char)('0'+i);
            TextureRegion region = ta.findRegion(prefix+ch);
            if (region!=null) add(ch, region);
        }
        return this;
    }
    public TextureString add(TextureAtlas ta, String prefix, char ...chs) {
        for (char ch : chs) {
            TextureRegion region = ta.findRegion(prefix+ch);
            if (region!=null) {
                add(ch, region);
            }
        }
        return this;
    }
}
