package com.fphoenix.entry;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.fphoenix.platform.PlatformDC;
import com.fphoenix.platform.Settings;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wuhao on 12/16/16.
 */
// constraint:
// all tagged sound is loop
// none-loop sound can only be pause/resume/stop
// loop sound can be stopped by tag
public class SoundPlayer {
    Settings settings;
    private int tag = 1;
    boolean pause;
    boolean running;
    Music bgm;
    final ArrayQueue<Data> pendingQ = new ArrayQueue<Data>();
    final Array<Data> loopQ = new Array<Data>();
    final ArrayList<Data> list = new ArrayList<Data>(8);
    Music[] musics;
    Sound[] sounds;
    Timer timer;
//    Thread sound_thread;
    static class Data {
        long id;
        //        long play_time;
        Sound sound;
        int tag;
        boolean loop;
        boolean valid;
    }

    public SoundPlayer() {
        this.settings = PlatformDC.get().getSettings();
    }

    public boolean isPause() {
        return pause;
    }
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void waitFinish(AssetManager assetManager) {
        assetManager.finishLoading();
        String[] music_files = Assets.music_files;
        String[] sound_files = Assets.sound_files;
        Music[] musics = new Music[music_files.length];
        Sound[] sounds = new Sound[sound_files.length];
        try {
            String pref = Assets.MUSIC_PREFIX;
            for (int i = 0, n = music_files.length; i < n; i++) {
                musics[i] = assetManager.get(pref + music_files[i], Music.class);
                musics[i].setLooping(true);
            }
            pref = Assets.SOUND_PREFIX;
            for (int i = 0, n = sound_files.length; i < n; i++) {
                sounds[i] = assetManager.get(pref + sound_files[i], Sound.class);
            }
            this.musics = musics;
            this.sounds = sounds;
//            int[] tmp = {Assets.B_WIN, Assets.B_LOSE,};
            int [] tmp = {};
            for (int m : tmp) {
                Music music = getMusic(m);
                if (music != null) {
                    music.setLooping(false);
                    music.setOnCompletionListener(music_listener);
                }
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    play_in_thread_();
                }
            }, 500, 20);

//            sound_thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    mainLoop();
//                }
//            });
//            sound_thread.start();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    final Pool<Data> pool = new Pool<Data>() {
        @Override
        protected Data newObject() {
            return new Data();
        }
    };

    public Sound getSound(int soundID) {
        if (sounds == null || soundID < 0 || soundID >= sounds.length)
            return null;
        return sounds[soundID];
    }

    public Music getMusic(int musicID) {
        if (musics == null || musicID < 0 || musicID >= musics.length)
            return null;
        return musics[musicID];
    }
    public void play_in_thread_() {
        Data d = null;
        if (pause || !settings.isSoundOn()) return;
        synchronized (this) {
            while (!pendingQ.empty()) {
                d = pendingQ.pop();
                if (d.valid) break;
                pool.free(d);
                d = null;
            }
        }
        if (d == null) return;
        try {
            if (d.loop) {
                d.id = d.sound.loop();
                loopQ.add(d);
            } else {
                d.id = d.sound.play();
                pool.free(d);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void try_to_enqueue(Data d) {
        synchronized (this) {
            pendingQ.push(d);
//            if (d==sounds[Assets.S_ONESHOOT])
//            try_to_enqueue__(d, 1);
//            else
//                pendingQ.push(d);
        }
    }
    // sound d in Q is not exceed n
    private void try_to_enqueue__(Data d, int n) {
        ArrayQueue<Data> q = this.pendingQ;
        int len = q.size();
        int cc = 0;
        for (int i=0; i<len; i++) {
            Data x = q.get(i);
            if (x.sound == d.sound) {
                cc++;
            }
        }
        if (cc<n)
            q.push(d);
    }

    // play sound & music
    public void play(int soundID, int tag, boolean loop) {
        try {
            if (!settings.isSoundOn()) return;
            Sound sound = getSound(soundID);
            if (sound == null) return;
            Data d = pool.obtain();
            d.sound = sound;
            d.tag = tag;
            d.loop = loop;
            d.valid = true;
            try_to_enqueue(d);
        }catch (Throwable t) {
        }
    }

    public void play(int soundID, boolean loop) {
        play(soundID, -1, loop);
    }

    public void play(int soundID) {
        play(soundID, -1, false);
    }
    // return a tag
    public int playLoopSound(int soundID) {
        int t = tag++;
        play(soundID, t, true);
        return t;
    }

    public void playBGM(int musicID) {
        try {
            Music music = getMusic(musicID);
            if (music == null) {
                if (bgm!=null)
                    bgm.stop();
                return;
            }
            Music old = bgm;
            bgm = music;
            if (!settings.isMusicOn()) return;
            if (old == music) {
                if (!old.isPlaying())
                    music.play();
            }else {
                if (old!=null)
                    old.stop();
                music.play();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    Music.OnCompletionListener music_listener = new Music.OnCompletionListener() {
        @Override
        public void onCompletion(Music music) {
//            popMusic();
//            playBGM(Assets.B_MENU);
        }
    };

    public void toggleMusic(boolean on) {
        if (bgm != null)
            try {
                if (on && settings.isMusicOn())
                    bgm.play();
                else
                    bgm.pause();
            } catch (Throwable t) {
                t.printStackTrace();
            }
    }

    // stop sound
    // 1. stop by tag used for loop sound
    // 2. stop all this sound instance
    // 3. stop all looped sound, used to stop looped sound lastly
    // 4. stop all sound, used by turn off sound
    // stop music : used to turn off music
    //
    public void stopByTag(int tag) {
        synchronized (this) {
            try {
                for (int i = loopQ.size - 1; i >= 0; i--) {
                    Data d = loopQ.get(i);
                    if (d.tag == tag) {
                        d.sound.stop(d.id);
                        loopQ.removeIndex(i);
                        d.valid = false;
                        pool.free(d);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void stopSound(int soundID) {
        Sound sound = getSound(soundID);
        if (sound == null) return;
        synchronized (this) {
            try {
                sound.stop();
                for (int i = loopQ.size - 1; i >= 0; i--) {
                    Data d = loopQ.get(i);
                    if (d.sound == sound) {
                        loopQ.removeIndex(i);
//                    d.valid = false;
                        pool.free(d);
                    }
                }
                list.clear();
                pendingQ.getAll(list);
                for (Data d : list) {
                    if (d.sound == sound) {
                        d.valid = false;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private void stopAllSound_() {
        try {
            while (!pendingQ.empty()) {
                Data d = pendingQ.pop();
                pool.free(d);
            }
            for (Data d : loopQ) {
                d.sound.stop();
            }
            pool.freeAll(loopQ);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void stopAllLoopSound_() {
        try {
            for (Data d : loopQ)
                d.sound.stop();
            pool.freeAll(loopQ);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void stopAllSound() {
        synchronized (this) {
            stopAllSound_();
        }
    }

    public void stopAllLoopSound() {
        synchronized (this) {
            stopAllLoopSound_();
        }
    }


    public void stopMusic() {
        try {
            if (bgm != null) {
                bgm.stop();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void pauseAllSound_() {
        pause = true;
        for (Data d : loopQ) {
            d.sound.pause(d.id);
        }
    }

    private void resumeAllSound_() {
        if (!settings.isSoundOn()) return;
        pause = false;
        for (Data d : loopQ) {
            d.sound.resume(d.id);
        }
    }


    // pause  &  resume
    public void pauseAll() {
        synchronized (this) {
            try {
                if (settings.isSoundOn()) {
                    pauseAllSound_();
                }
                if (settings.isMusicOn() && bgm != null) {
                    bgm.stop();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            pauseBGM();
        }
    }

    public void resumeAll() {
        synchronized (this) {
            try {
                if (settings.isSoundOn()) {
                    resumeAllSound_();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            resumeBGM();
        }
    }
    public void pauseBGM() {
        try {
            if (bgm!=null)
                bgm.pause();
        }catch (Throwable t) {
        }
    }
    public void resumeBGM() {
        try {
            if (settings.isMusicOn() && bgm != null) {
                bgm.play();
            }
        }catch (Throwable t){
        }
    }

    public void dispose() {
        try {
            stopAllLoopSound();
            stopMusic();
            if (musics != null) {
                Music[] tmp = musics;
                musics = null;
                for (Music m : tmp)
                    m.dispose();
            }
            if (sounds != null) {
                Sound[] sounds = this.sounds;
                this.sounds = null;
                for (Sound s : sounds)
                    s.dispose();
            }
            if (timer != null) {
                timer.cancel();
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
