package com.fphoenix.entry;

import com.fphoenix.entry.Audio.AudioInterface;

public class AudioIntf implements AudioInterface{
	public static final short MENU	= 0;
	public static final short GAMEPLAY	= 1;
	
	public static final short CHECKOUT_LINE       =  0;
	public static final short CUT1                =  1;
	public static final short CUT2                =  2;
	public static final short DIE1                =  3;
	public static final short DIE2                =  4;
	public static final short DIE3                =  5;
	public static final short HIT1                =  6;
	public static final short HIT2                =  7;
	public static final short HIT3                =  8;
	public static final short IDLE1               =  9;
	public static final short LAUNCH1             = 10;
	public static final short NEW_RECORD          = 11;
	public static final short SLASH1              = 12;
	public static final short SLASH2              = 13;
	public static final short STOP2               = 14;
	public static final short STOP                = 15;
	public static final short UI_BUTTON           = 16;
	public static final short UI_CHANGE1          = 17;
	public static final short UI_CHANGE2          = 18;
	public static final short UI_CHANGEWEAPONS2   = 19;
	public static final short UI_CHANGEWEAPONS    = 20;
	public static final short UI_COUNTING         = 21;
	public static final short UI_GETMONEY         = 22;
	public static final short UI_LOSE             = 23;
	public static final short UI_SUCCESS1         = 24;
	public static final short UI_SUCCESS2         = 25;
	public static final short UI_TIMEOUT1         = 26;

	private String []soundFiles = {
		"checkout_line.ogg",
		"cut1.ogg",
		"cut2.ogg",
		"die1.ogg",
		"die2.ogg",
		"die3.ogg",
		"hit1.ogg",
		"hit2.ogg",
		"hit3.ogg",
		"idle1.ogg",
		"launch1.ogg",
		"New_record.ogg",
		"slash1.ogg",
		"slash2.ogg",
		"stop2.ogg",
		"stop.ogg",
		"UI_button.ogg",
		"UI_change1.ogg",
		"UI_change2.ogg",
		"UI_changeweapons2.ogg",
		"UI_changeweapons.ogg",
		"UI_counting.ogg",
		"UI_getmoney.ogg",
		"UI_lose.ogg",
		"UI_success1.ogg",
		"UI_success2.ogg",
		"UI_timeout1.ogg",
	};


	private String []musicFiles = {
		"bgm_main.ogg",
		"bgm_gameplay.ogg",
	};
	public String [] getSoundFiles(){
		return soundFiles;
	}
	public String getSoundPrefix(){
		return "sfx/";
	}
	public String [] getMusicFiles(){
		return musicFiles;
	}
	public String getMusicPrefix(){
		return "music/";
	}
	public AudioIntf(){
		
	}
	@Override
	public int getSoundFileNumber() {
		return soundFiles.length;
	}
	@Override
	public int getMusicFileNumber() {
		return musicFiles.length;
	}
	@Override
	public String getSoundFile(int idx) {
		return soundFiles[idx];
	}
	@Override
	public String getMusicFile(int idx) {
		return musicFiles[idx];
	}
}
