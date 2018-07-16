package com.fphoenix.platform;

import android.os.SystemClock;

import com.badlogic.gdx.Preferences;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by wuhao on 9/9/16.
 */
public class Settings {
    public static final String S_SOUND = "Sound";
    public static final String S_MUSIC = "Music";
    public static final String S_NOTIFICATION = "Notify";
    public static final String HAS_RATE = "Rate";
    public static final String AD_FREE = "Adf";
    private static final String COIN_NUMBER = "cn";
    private static final String HEALTH = "hl";
    private static final String PROP = "pp";
    private static final String STAR = "st_";
    private static final String SCORE = "sc_";
    private static final String CURRENT_LV = "clv";
    private static final String HP_RECOVER_TIME_ELAPSED = "hre";
    private static final String HP_RECOVER_TIME_SYS = "hrs";

    public static final String DAILY_BONUS_DAY = "dbay";
    public static final String DAILY_BONUS_TIME = "dbT";
    public static final String SALE_START_TIME = "lst"; // -1 default , -2 timeout & not committed, -3 committed
    public static final String LIMITED_VIDEO = "ltdV";
    public static final String TUT_LV1 = "tut1";
    public static final String TUT_LV2 = "tut2";
    public static final String TUT_LV3 = "tut3";
    public static final String TUT_PROP = "tutProp";
    public static final String LIMIT_SALE_DAY = "lsDay";
    public static final String LIMIE_SALE_CNT = "lsCnt";
    public static final String LIMIT_SALE_SHOW_SHOP = "lsShop";
    public static final String LIMIT_SALE_FAIL_CNT = "lsFail";
    public static final String LIMIT_SALE_WIN_CNT = "lsWin";
    private static final String HAS_CLICK_MORE = "cMore";


    public static final int PROP_BLOOD = 0;
    public static final int PROP_X_SCORE = 1;
    public static final int PROP_ALL_KILL = 2;
    public static final int PROP_SHIELD = 3;
    public static final int PROP_ICE = 4;
    public static final int PROP_NUM = 5;

    public static final int PROP_FAKE_COIN = 6;

    public static final int N_LV = 60;
    public static final int MAX_HP = 5;
    // assume 2 hour
    public static final int HP_RECOVER_TIME = 25 * 60 * 1000;

    public Settings init(Preferences db) {
        this.db = db;
        this.music_on = db.getBoolean(S_MUSIC, true);
        this.sound_on = db.getBoolean(S_SOUND, true);
        this.adFree = db.getBoolean(AD_FREE, false);
        this.notification_on = db.getBoolean(S_NOTIFICATION, true);
        this.has_rate = db.getBoolean(HAS_RATE, false);
        this.has_click_more = db.getBoolean(HAS_CLICK_MORE, false);
        this.daily_bonus_day = db.getInteger(DAILY_BONUS_DAY, 0);
        this.daily_bonus_time= db.getLong(DAILY_BONUS_TIME, -1);
        this.sale_start_time = db.getLong(SALE_START_TIME, -1);
        this.last_limited_video = db.getLong(LIMITED_VIDEO, -1);
        this.coinValue = db.getLong(COIN_NUMBER, 100);
        this.prop_nums = new int[PROP_NUM];
        for (int i = 0; i < PROP_NUM; i++) {
            this.prop_nums[i] = db.getInteger(PROP + i, 0);
        }
        stars = new byte[N_LV];
        scores = new int[N_LV];
        for (int i = 0, n = stars.length; i < n; i++) {
            stars[i] = ((byte) db.getInteger(STAR + i, 0));
            scores[i] = db.getInteger(SCORE + i, 0);
        }
        init_tutorial();
        clv = db.getInteger(CURRENT_LV, 59);
        init_hp();
        initLimitSale();
        post_check();

        return this;
    }

    void post_check() {
        long c = System.currentTimeMillis();
        if (last_limited_video>=c) {
            last_limited_video = c;
        }
    }
    void init_tutorial() {
        boolean has;
        has = db.getBoolean(TUT_LV1, true);
        if (has) bool_set.add(TUT_LV1);
        has = db.getBoolean(TUT_LV2, true);
        if (has) bool_set.add(TUT_LV2);
        has = db.getBoolean(TUT_LV3, true);
        if (has) bool_set.add(TUT_LV3);
        has = db.getBoolean(TUT_PROP, true);
        if (has) bool_set.add(TUT_PROP);
    }
    public boolean hasTutorial(String name) {
        return bool_set.contains(name);
    }

    public void setTutorialDone(String name) {
        if (bool_set.contains(name)) {
            bool_set.remove(name);
            db.putBoolean(name, false);
            db.flush();
        }
    }

    void initLimitSale() {
        if (sale_start_time<-1) {
            sale_cnt = 10; // no ls to show
            return; // timeout
        }
        // init sale day and count
        sale_day = db.getInteger(LIMIT_SALE_DAY, -1);
        sale_cnt = db.getInteger(LIMIE_SALE_CNT, 0);
        sale_fail_cnt = db.getInteger(LIMIT_SALE_FAIL_CNT, 0);
        sale_win_cnt = db.getInteger(LIMIT_SALE_WIN_CNT, 0);
        sale_show_in_shop = db.getBoolean(LIMIT_SALE_SHOW_SHOP, false);
        if (sale_day==-1) {
            sale_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }else {
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (sale_day != day) {
                sale_day = day;
                sale_cnt = 0;
                sale_fail_cnt = 0;
                sale_show_in_shop = false;
            }
        }
    }

    public int getSaleDay() {
        return sale_day;
    }

    public int getSaleCnt() {
        return sale_cnt;
    }

    public boolean qSaleWhenSuccess() {
        db.putInteger(LIMIT_SALE_WIN_CNT, ++sale_win_cnt);
        if (canShowSale()) {
            if (sale_win_cnt==3 || sale_win_cnt%7==0) {
                incSaleCnt();
                return true;
            }
        }
        return false;
    }
    public boolean qSaleWhenFail() {
        if (canShowSale()) {
            db.putInteger(LIMIT_SALE_FAIL_CNT, ++sale_fail_cnt);
            if (sale_fail_cnt==2) {
                incSaleCnt();
                return true;
            }
        }
        return false;
    }

    public boolean qSaleWhenShop() {
        if (canShowSale()) {
            if (sale_show_in_shop) return false;
            sale_show_in_shop = true;
            incSaleCnt();
            return true;
        }
        return false;
    }
    private boolean canShowSale() {
        return sale_cnt<2;
    }
    public void setShowSale(boolean sale_show_in_shop) {
        boolean real = this.sale_show_in_shop || sale_show_in_shop;
        if (real != this.sale_show_in_shop) {
            db.putBoolean(LIMIT_SALE_SHOW_SHOP, true);
        }
    }
    private void incSaleCnt() {
        db.putInteger(LIMIE_SALE_CNT, ++sale_cnt);
    }
    void init_hp() {
        health = db.getInteger(HEALTH, MAX_HP);
        hp_recovering_start_time_elapsed = db.getLong(HP_RECOVER_TIME_ELAPSED, -1);
        if (hp_recovering_start_time_elapsed==-1) hp_recovering_start_time_elapsed = SystemClock.elapsedRealtime();
        long hp_recovering_start_time_sys = db.getLong(HP_RECOVER_TIME_SYS, -1);
        if (hp_recovering_start_time_sys==-1) hp_recovering_start_time_sys = System.currentTimeMillis();
        // try to check
        if (health>=MAX_HP) return; // when health reaches max, the time is meaningless.
        long time_elapsed = SystemClock.elapsedRealtime();
        if (hp_recovering_start_time_elapsed>time_elapsed) {
            // machine is reboot, we use sys time,
            long time_sys = System.currentTimeMillis();
            if (hp_recovering_start_time_sys>time_sys) {
                // sys time is changed also, wtf, reset to now
                hp_recovering_start_time_elapsed = time_elapsed;
            }else {
                // maybe time_sys is ok, use the diff
                long diff = time_sys - hp_recovering_start_time_sys;
                hp_recovering_start_time_elapsed = time_elapsed - diff;
            }
        }
        updateHP();
    }
    Settings commitHP() {
        db.putInteger(HEALTH, health);
        if (health < MAX_HP) {
            db.putLong(HP_RECOVER_TIME_ELAPSED, hp_recovering_start_time_elapsed);
            long hp_recovering_start_time_sys = System.currentTimeMillis() - (SystemClock.elapsedRealtime()-hp_recovering_start_time_elapsed);
            db.putLong(HP_RECOVER_TIME_SYS, hp_recovering_start_time_sys);
        }
        return this;
    }

    public void flush() {
        db.flush();
    }

    public void setBuyLimitSaleOK() {
        sale_start_time = -3;
        db.putLong(SALE_START_TIME, -3);
    }
    private long DAY = 24 * 3600 * 1000;
    private long TT = 48 *3600 * 1000;
    public long updateLS() {
        if (sale_start_time < -1) return -1; // no valid, timeout
        if (sale_start_time==-1) {
            // start to count
            sale_start_time = System.currentTimeMillis();
            db.putLong(SALE_START_TIME, sale_start_time);
        }
        long diff = sale_start_time + TT - System.currentTimeMillis();
        if (diff<=0) {
            sale_start_time = -2;
            db.putLong(SALE_START_TIME, -3);
            db.flush();
        }
        return diff;
    }
    // return -1 if full, otherwise the left time to recover a hp
    public long updateHP() {
        long elapsed = SystemClock.elapsedRealtime();
        do {
            if (health >= MAX_HP) return -1;
            if (elapsed - hp_recovering_start_time_elapsed < HP_RECOVER_TIME)
                break;
            health++;
            hp_recovering_start_time_elapsed += HP_RECOVER_TIME;
            commitHP();
        }while (true);
        return hp_recovering_start_time_elapsed + HP_RECOVER_TIME - elapsed;
    }

    public boolean useHP() {
        if (health<1) return false;
        if (health-- == MAX_HP) {
            hp_recovering_start_time_elapsed = SystemClock.elapsedRealtime();
        }
        commitHP();
        return true;
    }

    public void pauseFlush() {
        updateHP();
        updateLS();
        flush();
    }

    public boolean isSoundOn() {
        return sound_on;
    }

    public boolean isMusicOn() {
        return music_on;
    }

    public boolean isNotificationOn() {
        return notification_on;
    }

    public boolean isAdFree() {
        return this.adFree;
    }

    public boolean hasRate() {
        return this.has_rate;
    }
    public boolean hasClickMore() {
        return has_click_more;
    }

    public boolean onClickMore() {
        if (has_click_more) return false;
        has_click_more = true;
        db.putBoolean(HAS_CLICK_MORE, true);
        return true;
    }

    public void setSound(boolean on) {
        if (sound_on != on) {
            sound_on = on;
            db.putBoolean(S_SOUND, on);
        }
    }

    public void setMusic(boolean on) {
        if (music_on != on) {
            music_on = on;
            db.putBoolean(S_MUSIC, on);
        }
    }

    public boolean toggleMusic() {
        music_on = !music_on;
        db.putBoolean(S_MUSIC, music_on);
        return music_on;
    }

    public boolean toggleSound() {
        sound_on = !sound_on;
        db.putBoolean(S_SOUND, sound_on);
        return sound_on;
    }

    public void setNotification(boolean on) {
        if (notification_on != on) {
            notification_on = on;
            db.putBoolean(S_NOTIFICATION, on);
        }
    }

    public boolean toggleNotification() {
        notification_on = !notification_on;
        db.putBoolean(S_NOTIFICATION, notification_on);
        return notification_on;
    }

    public void adFree() {
        if (!adFree) {
            adFree = true;
            db.putBoolean(AD_FREE, true);
            db.flush();
        }
    }

    public void setRate() {
        if (!has_rate) {
            has_rate = true;
            db.putBoolean(HAS_RATE, true);
            db.flush();
        }
    }

    public boolean isLimitedVideoAvailable() {
        return last_limited_video/DAY != System.currentTimeMillis()/DAY;
    }

    public Settings consumeLimitedVideo() {
        last_limited_video = System.currentTimeMillis();
        db.putLong(LIMITED_VIDEO, last_limited_video);
        return this;
    }

    public Preferences getPreferences() {
        return this.db;
    }

    public int getPropNumber(int id) {
        return prop_nums[id];
    }

    // must
    public boolean decProp(int id, int v) {
        if (prop_nums[id] < v) return false;

        prop_nums[id] -= v;
        db.putInteger(PROP + id, prop_nums[id]);
        return true;
    }

    public boolean decProp(int id) {
        return decProp(id, 1);
    }

    public Settings putPropNumber(int id, int num) {
        if (prop_nums[id] != num) {
            prop_nums[id] = num;
            db.putInteger(PROP + id, num);
        }
        return this;
    }
    public long getDailyBonusTime() {
        return daily_bonus_time;
    }
    public int getBonusDay() {
        return daily_bonus_day;
    }
    public void claimDailyBonus() {
        this.daily_bonus_day++;
        this.daily_bonus_time = PlatformDC.get().getCurrentServerTime();
        db.putInteger(DAILY_BONUS_DAY, daily_bonus_day);
        db.putLong(DAILY_BONUS_TIME, daily_bonus_time);
    }
    public int getHealth() {
        return health;
    }
    public Settings incHealth() {
        if (health<MAX_HP) {
            health++;
            commitHP();
        }
        return this;
    }

    public boolean fillHP() {
        if (health<MAX_HP) {
            health = MAX_HP;
            commitHP();
        }
        return true;
    }
    public long addCoins(int n) {
        coinValue += n;
        db.putLong(COIN_NUMBER, coinValue);
        return coinValue;
    }
    public long getGoldNumber() {
        return coinValue;
    }

    public Settings putGoldNumber(long num) {
        if (coinValue != num) {
            coinValue = num;
            db.putLong(COIN_NUMBER, num);
        }
        return this;
    }
    public boolean tryUseGold(int num) {
        if (coinValue<num) return false;
        coinValue -= num;
        db.putLong(COIN_NUMBER, coinValue);
        flush();
        return true;
    }
    // all lv values in Settings are zero-based
    public int getStar(int lv0) {
        return stars[lv0];
    }

    // return true if a new record
    public boolean tryUpdateStar(int lv0, int val) {
        if (stars[lv0] < val) {
            stars[lv0] = (byte) val;
            db.putInteger(STAR + lv0, val);
            return true;
        }
        return false;
    }
    public int getScore(int lv0) {
        return scores[lv0];
    }
    public boolean tryUpdateScore(int lv0, int new_val) {
        if (scores[lv0]<new_val) {
            scores[lv0] = new_val;
            db.putInteger(SCORE+lv0, new_val);
            return true;
        }
        return false;
    }
    public int getClv() {
        return clv;
    }
    public boolean tryUpdateLv(int new_lv0) {
        if (new_lv0 < clv) return false;
        clv = new_lv0 + 1;
        db.putInteger(CURRENT_LV, clv);
        return true;
    }

    /**
     * return the prop id by its name, or fake_coin, or -1 illegal name
     * @param name
     * @return
     */
    public static int prop_name_to_id(String name) {
        if (name==null) return -1;
        if (name.equals("xblood")) return PROP_BLOOD;
        if (name.equals("xkill")) return PROP_ALL_KILL;
        if (name.equals("ice")) return PROP_ICE;
        if (name.equals("shield")) return PROP_SHIELD;
        if (name.equals("xscore")) return PROP_X_SCORE;
        if (name.equals("coin")) return PROP_FAKE_COIN;
        return -1;
    }
    public static String prop_id_to_name(int prop_id) {
        switch (prop_id) {
            case PROP_BLOOD:
                return "xblood";
            case PROP_X_SCORE:
                return "xscore";
            case PROP_ALL_KILL:
                return "xkill";
            case PROP_SHIELD:
                return "shield";
            case PROP_ICE:
                return "ice";
        }
        throw new IllegalArgumentException("bad prop id=" + prop_id);
    }

    byte[] stars;
    int[] scores;
    Preferences db;
    HashSet<String> bool_set = new HashSet<>();
    long coinValue;
    long last_limited_video; // the last time when the user invoked reviving by ad video or buy health
    int health;
    int clv;
    int[] prop_nums;
    long hp_recovering_start_time_elapsed;  // SystemClock.elapsedRealtime();
    private long sale_start_time;
    private long daily_bonus_time;
    private int daily_bonus_day;
    //    long hp_recovering_start_time_sys;      // System.currentTimeMillis()
    private int sale_cnt;
    private int sale_day;
    private int sale_fail_cnt;
    private int sale_win_cnt;
    private boolean sale_show_in_shop;
    boolean sound_on;
    boolean music_on;
    boolean notification_on;
    boolean adFree;
    boolean has_rate;
    boolean has_click_more;
}
