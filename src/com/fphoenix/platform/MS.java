package com.fphoenix.platform;

/**
 * Created by alan on 18-5-30.
 */

public class MS {
    public static void play(int id) {
        PlatformDC.get().getPlayer().play(id);
    }
    public static void bgm(int id) {
        PlatformDC.get().getPlayer().playBGM(id);
    }
}
