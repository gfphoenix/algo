package com.fphoenix.entry;

import com.fphoenix.entry.Audio.AudioSwitch;

public class AudioSwitchImpl implements AudioSwitch{
	boolean soundOn;
	boolean musicOn;
	public AudioSwitchImpl(){
//		soundOn = MyDoodleGame.getSettings().soundOn;
//		musicOn = MyDoodleGame.getSettings().musicOn;
	}
	@Override
	public boolean shouldPlaySound() {
		return soundOn;
	}
	@Override
	public boolean shouldPlayMusic() {
		return musicOn;
	}
	public void toggleMusic(){
		boolean on = !musicOn;
		musicOn = on;
//		MyDoodleGame.getSettings().musicOn = on;
//		MyDoodleGame.get().saveSettings();
	}
	public void toggleSound(){
		boolean on = !soundOn;
		soundOn = on;
//		MyDoodleGame.getSettings().soundOn = on;
//		MyDoodleGame.get().saveSettings();
	}
}
