package com.fphoenix.common;

/**
 * Created by wuhao on 10/8/16.
 */
public class MessageChannels {
    private MessageChannels(){}
    public static class Message {
        public int i;
        public int i2;
        public long l;
        public long l2;
        public float f;
        public float f2;
        public String s;
        public Object object;
        public Message reset(){
            i = i2 = 0;
            f = f2 = 0;
            s = null;
            object = null;
            return this;
        }

        @Override
        public String toString() {
            return "int["+i+", "+i2+"], float["+f+", "+f2+"]";
        }
    }

    // Note that: when hp<=0, should immediately set state to FAILING, to prevent gaming logic,
    // and after several seconds, show failed animation
    public static final int PLAYER_HP_0  = 1; // when the player's hp down to 0
    public static final int PLAYER_HP_DEC = 2; // when the player is attacked, decreasing hp

    public static final int UPDATE_HP_UI = 11;
    public static final int UPDATE_FACE = 12; // update face hint
    public static final int UPDATE_SCORE = 13; // long old, new
    public static final int UPDATE_GOAL = 14; // goal, name:i
    public static final int UPDATE_PROP_KILL_ALL = 15;
    public static final int UPDATE_PROP_ICE = 16;
    public static final int UPDATE_PROP_SHIELD = 17;

    public static final int UPDATE_PROP_NUMS = 18;

//    public static final int PROP_hp_protect = 31;
//    public static final int PROP_start_decrease_speed = 32;
//    public static final int PROP_end_decrease_speed = 33;
//    public static final int PROP_bomb_emoji = 34;

    public static final int PROP_KILL_ALL = 31;
    public static final int PROP_ICE = 32;
    public static final int PROP_SHIELD = 33;

    public static final int UI_PAUSE = 50;
    public static final int UI_RESUME = 51;
    public static final int UNITY_AD_READY = 61;
    public static final int UNITY_AD_FINISHED = 62;


    public static final int UPDATE_UI_PROP = 100;
    public static final int UPDATE_UI_COIN = 101;

    public static final int REVIVED_BY_GOLD = 110;
    public static final int REVIVED_BY_AD = 111;
}
