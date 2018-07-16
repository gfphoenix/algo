package com.fphoenix.entry;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Audio {
	public interface AudioSwitch {
		boolean shouldPlaySound();
		boolean shouldPlayMusic();
		void toggleSound();
		void toggleMusic();
	}

	public interface AudioInterface {
		public int getSoundFileNumber();
		public int getMusicFileNumber();
		public String getSoundFile(int idx);
		public String getMusicFile(int idx);
		public String getSoundPrefix();
		public String getMusicPrefix();
	}
	public static class SoundData implements Poolable{
		public Sound sound;
		public long id;
		public boolean loop;
		@Override
		public void reset() {
			sound = null;
			id = -1;
			loop = false;
		}
	}
	Pool<SoundData> pool = new Pool<SoundData>(){
		protected SoundData newObject() {
			return new SoundData();
		};
	};
	protected AudioSwitch as = null;
	private AudioInterface audio = null;
	protected Sound[] sounds;
	protected Music[] musics;
	private AssetManager am;
	protected Array<SoundData> sq = new Array<Audio.SoundData>(8);
//	protected Array<Integer> soundQueue = new Array<Integer>(16);
	protected Array<SoundData> sqLoop = new Array<SoundData>(4);
	protected Array<Music> musicPlayingStack = new Array<Music>();
	private static Audio self = null;
	protected Timer timer = new Timer();
	
	public static AudioSwitch alwaysOn = new AudioSwitch() {
		@Override
		public boolean shouldPlaySound() {
			return true;
		}
		@Override
		public boolean shouldPlayMusic() {
			return true;
		}
		@Override
		public void toggleSound() {
		}
		@Override
		public void toggleMusic() {
		}
		
	};

	private Audio(AudioInterface au, AudioSwitch as) {
		this.audio = au;
		this.as = as;
	}

	public static Audio InitAudio(AudioInterface au, AudioSwitch as) {
		if (as == null)
			as = alwaysOn;
		self = new Audio(au, as);

		return self;
	}
//	public void onSwitchSound(boolean on){
////		soundQueue.clear();
//		sq.clear();
//		as.setSoundOn(on);
//	}
//	public void onSwitchMusic(boolean on){
//		try{
//			synchronized(this){
//			as.setMusicOn(on);
//			if(musicPlayingStack.size<=0)
//				return;
//			Music m = musicPlayingStack.peek();
//			if(on){
//				m.play();
//			}else{
//				m.pause();
//			}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	public static Audio getInstance() {
		return self;
	}
	public void end(){
		synchronized(this){
		sq.clear();
		}
		self = null;
	}
//	private void play_sound_by_thread() {
//		if (soundQueue.size <= 0)
//			return;
//		try {
//			int id;
//			synchronized (self) {
//				id = soundQueue.removeIndex(0);
//			}
//			if (shouldPlaySound())
//				sounds[id].play();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	public SoundData playSound(int soundId, boolean loop, boolean nocopy){
		if(!as.shouldPlaySound()){
			return null;
		}
		SoundData sd = pool.obtain();
		try{
		sd.sound = sounds[soundId];
		sd.loop = loop;
		boolean ex = false;
		if(nocopy){
			for(SoundData s : sq){
				if(s.sound == sd.sound){
					ex = true;
					break;
				}
			}
		}
		if(!ex){
			sq.add(sd);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sd;
	}
	public SoundData playSound(int soundId, boolean loop){
		return playSound(soundId, loop, true);
	}
	private void play_sound_in_thread__(){
		if(sq.size<=0)
			return;
		try{
			SoundData sd ;
			synchronized(self){
				if(sq.size<=0) return;
				sd = sq.removeIndex(0);
			}
			if(sd==null || sd.sound==null)
				return;
			if(sd.loop){
				sd.id = sd.sound.loop();
				sqLoop.add(sd);
			}else{
				sd.id = sd.sound.play();
				freeSoundData(sd);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void freeSoundData(SoundData sd){
		if(sd!=null)
		pool.free(sd);
	}
	public void stopSound(SoundData sd){
		try{
			sd.sound.stop(sd.id);
			if(!sd.loop)
				return;
			for(int i=sqLoop.size; --i>=0; ){
				SoundData s = sqLoop.get(i);
				if(s==sd){
					s.sound.stop(s.id);
					sqLoop.removeIndex(i);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void stopSound(int id){
		try{
			Sound s = sounds[id];
			synchronized(self){
				for(int i=sq.size; --i>=0;){
					SoundData sd = sq.get(i);
					if(sd.sound == s){
						sq.removeIndex(i);
					}
				}
				for(int i=sqLoop.size; --i>=0; ){
					SoundData sd = sqLoop.get(i);
					if(sd.sound == s){
						sqLoop.removeIndex(i);
					}
				}
			}
			s.stop();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void clearLoopSound(){
		synchronized(self){
			try{
			for(int i=sq.size; --i>=0; ){
				SoundData sd = sq.get(i);
				if(sd.loop){
					sd.sound.stop();
				}
			}
			for(int i=sqLoop.size; --i>=0; ){
				sqLoop.get(i).sound.stop();
			}
			sq.clear();
			sqLoop.clear();
			
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				sqLoop.clear();
			}
		}
	}
	public SoundData playSound(int soundId){
		return playSound(soundId, false);
	}

	public void playMusic(int id) {
		try {
			synchronized(this){
			for(Music x : musicPlayingStack){
				if(x!=musics[id])
					x.stop();
			}
			musicPlayingStack.clear();
			Music m = musics[id];
			musicPlayingStack.add(m);
			if (shouldPlayMusic() && !m.isPlaying())
				m.play();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void stopMusic(){
		try{
			synchronized(this){
			musicPlayingStack.clear();
			for(Music m : musics){
				m.stop();
			}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void pushMusic(int id) {
		synchronized(this){
		try {
			if(musicPlayingStack.size>0){
				Music old = musicPlayingStack.peek();
				old.stop();
			}
			Music x = musics[id];
			musicPlayingStack.add(x);
			if (shouldPlayMusic())
				x.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	}

	public void popMusic() {
		synchronized(this){
		try {
			Music m = musicPlayingStack.pop();
			m.stop();
			if (shouldPlayMusic() && musicPlayingStack.size>0)
				musicPlayingStack.peek().play();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	}

	public void pauseMusic() {
		synchronized(this){
		try {
			if(musicPlayingStack.size>0){
				Music m = musicPlayingStack.peek();
				if(m!=null)
					m.pause();
			}
			for(SoundData sd : sqLoop){
				sd.sound.pause(sd.id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	}

	public void resumeMusic() {
		synchronized(this){
		try {
			for(SoundData sd : sqLoop){
				if(sd!=null && sd.sound!=null)
				sd.sound.resume(sd.id);
			}
			if(musicPlayingStack.size==0 || !shouldPlayMusic())
				return;
			Music m = musicPlayingStack.peek();
			m.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	}

	public boolean shouldPlaySound() {
		return this.as.shouldPlaySound();
	}

	public boolean shouldPlayMusic() {
		return this.as.shouldPlayMusic();
	}
	public void waitFinish(){
		try {
			am.finishLoading();
			String sPrefix = audio.getSoundPrefix();
			String mPrefix = audio.getMusicPrefix();
			for (int idx = 0; idx < sounds.length; idx++) {
				String s = audio.getSoundFile(idx);
				sounds[idx] = am.get(sPrefix + s);
			}
			for (int idx = 0; idx < musics.length; idx++) {
				String m = audio.getMusicFile(idx);
				musics[idx] = am.get(mPrefix + m);
				musics[idx].setLooping(true);
			}
			// timer

			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					play_sound_in_thread__();
//					play_sound_by_thread();
				}
			},500,150);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void load(AssetManager am) {
		try {
			this.am = am;
			String sPrefix = audio.getSoundPrefix();
			String mPrefix = audio.getMusicPrefix();
			int len = audio.getSoundFileNumber();
			for (int idx = 0; idx < len; idx++) {
				String s = audio.getSoundFile(idx);
				am.load(sPrefix + s, Sound.class);
			}
			sounds = new Sound[len];
			
			len = audio.getMusicFileNumber();
			for (int idx = 0; idx < len; idx++) {
				String m = audio.getMusicFile(idx);
				am.load(mPrefix + m, Music.class);
			}
			musics = new Music[len];

		} catch (Exception e) {
			e.printStackTrace();
			Gdx.app.log("Arthur", "load sound file failed");
		}
	}

	public void dispose() {
		try {
			timer.cancel();
			
			String prefix = audio.getSoundPrefix();
			int len = audio.getSoundFileNumber();
			for (int idx = 0; idx < len; idx++) {
				String s = audio.getSoundFile(idx);
				String file = prefix + s;
				if(am.isLoaded(file, Sound.class))
					am.unload(file);
			}
			prefix = audio.getMusicPrefix();
			len = audio.getMusicFileNumber();
			for (int idx = 0; idx < len; idx++) {
				String m = audio.getMusicFile(idx);
				String file = prefix + m;
				if(am.isLoaded(file, Music.class))
					am.unload(prefix + m);
			}
			Sound[] ss = this.sounds;
			for (Sound s : ss) {
				if (s != null)
					s.dispose();
			}
			Music[] mm = this.musics;
			for (Music m : mm) {
				if (m != null)
					m.dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			self = null;
		}
	}
}
