package com.fphoenix.platform;

/**
 * Created by wuhao on 12/12/16.
 */
public class FlurryMessage {
    public static final String E_LEVEL = "LEVEL";
    public static final String E_BOOSTER = "BOOSTER";
    public static final String E_EMOJI = "EMOJI";
    public static final String E_STORE = "STORE";
    public static final String E_AD = "AD";
    public static final String E_MISC = "MISC";

    public static final String k_level_start = "level_start";
    public static final String k_level_pause = "level_pause";
    public static final String k_level_win = "level_win";
    public static final String k_level_fail = "level_fail";
    public static final String k_level_first_play = "level_first_play";
    public static final String k_level_first_win = "level_first_win";

    public static final String k_charge_click = "charge_click";
    public static final String k_charge_success = "charge_success";

    public static final String k_item_buy = "item_buy";
    public static final String k_item_use = "item_use";

    public static final String k_lto_show = "lto_show";
    public static final String k_lto_buy = "lto_buy_click";
    public static final String k_lto_buy_ok = "lto_buy_ok";
    public static final String k_lto_lv = "lto_lv";

    public static final String k_more = "show_more";
    public static final String k_daily_bonus = "daily_bonus";
    public static final String k_ad = "show_full_ad";

    private static final FlurryMessage msg = new FlurryMessage();
    public static FlurryMessage level(String k, int lv0) {
        return event(E_LEVEL, k, lv0);
    }

    public static FlurryMessage booster(String key, int lv0) {
        return event(E_BOOSTER, key, lv0);
    }
    public static FlurryMessage store(String key, String val) {
        return make(E_STORE, key, val);
    }
    public static FlurryMessage misc(String key, String val) {
        return make(E_MISC, key, val);
    }

    public static FlurryMessage emoji(String key, String val) {
        return make(E_EMOJI, key, val);
    }

    public static FlurryMessage ad(String key, String val) {
        return make(E_AD, key, val);
    }
    public static FlurryMessage event(String event, String key, int lv0) {
        msg.event = event;
        msg.key = key;
        msg.val = "lv_" + (lv0+1);
        return msg;
    }
    public static FlurryMessage make(String event, String key, String val) {
        msg.event = event;
        msg.key = key;
        msg.val = val;
        return msg;
    }

    public static FlurryMessage make2(String event, String key) {
        return make(event, key, "ok");
    }

    @Override
    public String toString() {
        return "["+event+"] '"+key+"'="+val;

    }
    public FlurryMessage copy() {
        FlurryMessage message = new FlurryMessage();
        message.event = event;
        message.key = key;
        message.val = val;
        return message;
    }

    public String event;
    public String key;
    public String val;
}
