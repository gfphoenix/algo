package com.fphoenix.entry;

import com.badlogic.gdx.utils.Array;

public class Assets {
    public static final String font1 = "font.fnt";
    public static final String loading_ui = "loading2.atlas";
    public static final String main_atlas = "main.atlas";
    public static final String MUSIC_PREFIX = "music/";
    public static final String SOUND_PREFIX = "sound/";
    public static final String[] music_files = {
            "bgm_menu.ogg",
            "bgm_play.ogg",
    };
    public static final short BGM_MENU = 0;
    public static final short BGM_PLAY = 1;

    public static final String [] sound_files = {
            "add_blood.ogg",
            "add_coin.ogg",
            "add_score.ogg",
            "attack.ogg",
            "booster_cover.ogg",
            "booster_freeze.ogg",
            "booster_hammer.ogg",
            "booster_use.ogg",
            "click.ogg",
            "count.ogg",
            "defense.ogg",
            "emoji_attack.ogg",
            "freeze.ogg",
            "heart.ogg",
            "hp_trans_score.ogg",
            "injured.ogg",
            "lose.ogg",
            "match.ogg",
            "ready_go.ogg",
            "reborn_or_not.ogg",
            "reward.ogg",
            "score.ogg",
            "star.ogg",
            "uncover.ogg",
            "unfreeze.ogg",
            "win.ogg",
            "win_tips.ogg",
    };
    public static final short S_ADD_BLOOD = 0;
    public static final short S_ADD_COIN = 1;
    public static final short S_ADD_SCORE = 2;
    public static final short S_ATTACK = 3;
    public static final short S_BOOSTER_COVER = 4;
    public static final short S_BOOSTER_FREEZE = 5;
    public static final short S_BOOSTER_HAMMER = 6;
    public static final short S_BOOSTER_USE = 7;
    public static final short S_CLICK = 8;
    public static final short S_COUNT = 9;
    public static final short S_DEFENSE = 10;
    public static final short S_EMOJI_ATTACK = 11;
    public static final short S_FREEZE = 12;
    public static final short S_HEART = 13;
    public static final short S_HP_TRANS_SCORE = 14;
    public static final short S_INJURED = 15;
    public static final short S_LOSE = 16;
    public static final short S_MATCH = 17;
    public static final short S_READY_GO = 18;
    public static final short S_REBORN_OR_NOT = 19;
    public static final short S_REWARD = 20;
    public static final short S_SCORE = 21;
    public static final short S_STAR = 22;
    public static final short S_UNCOVER = 23;
    public static final short S_UNFREEZE = 24;
    public static final short S_WIN = 25;
    public static final short S_WIN_TIPS = 26;
    public static Array<String> getLoadAtlasNames() {
        Array<String> ss = new Array<String>(2);

        return ss;
    }

    public static Array<String> getLoadBMNames() {
        Array<String> ss = new Array<String>(2);
        ss.add("font/font2.fnt");
        return ss;
    }

    public static Array<String> getUnloadResourceNames() {
        Array<String> ss = new Array<String>(8);

        return ss;
    }

}
