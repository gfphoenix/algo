package com.fphoenix.platform;


import java.util.HashMap;
import java.util.Map;

public interface Platform {

    void callPlatform(int code, PlatformBundle bundle);

    public static final int CODE_SHOW_FEATURE_VIEW = 1;
    public static final int CODE_CLOSE_FEATURE_VIEW = 2;
    public static final int CODE_SHOW_FULL_AD = 3;
    public static final int CODE_SHOW_EXIT_FULL_AD = 4;
    public static final int CODE_CLOSE_FULL_AD = 5;
    public static final int CODE_IS_SHOWING_FULL_AD = 6;
    public static final int CODE_RATE = 7;
    public static final int CODE_MORE = 8;
    public static final int CODE_BUY_GOODS = 9;
    public static final int CODE_FLURRY_EVENT = 10; // key : str, value other

    public static final int CODE_LAZY_INIT = 20;
    public static final int CODE_UPDATE_NOTIFICATION_BONUS = 21;

    public static final int CODE_REQUEST_UPDATE_TIME = 30;
    public static final int CODE_TOGGLE_NOTIFICATION = 31;

    public static final int CODE_IS_FOCUSED = 41;
    public static final int CODE_REQUEST_CAMERA_PERMISSION = 51;
//    public static final int MC_UNITY_ADS_IS_READY = 61;
//    public static final int MC_UNITY_ADS_FINISHED = 62;
    public static final int CODE_START_SHOW_UNITY_ADS = 63;
    // IN-OUT PARAMETER
    public static class PlatformBundle {
        public boolean b;
//        int code;
        public int i;
        public long l;
        public float f;
        public String str;
        public Object o;
        public final Map<String, Object> map = new HashMap<String, Object>();
        public PlatformBundle reset() {
            b=false;
            i=0;
            l=0;
            f=0f;
            str=null;
            o = null;
            map.clear();
            return this;
        }
    }
}
