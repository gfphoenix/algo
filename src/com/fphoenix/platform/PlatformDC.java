package com.fphoenix.platform;

import android.fphoenix.face.DoodleGame;
import android.os.SystemClock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.AttachmentLoader;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.esotericsoftware.spine.attachments.SkinnedMeshAttachment;
import com.fphoenix.common.Bundle;
import com.fphoenix.common.Hub;
import com.fphoenix.common.MessageChannels;
import com.fphoenix.common.TextureString;
import com.fphoenix.common.Utils;
import com.fphoenix.components.SpineData;
import com.fphoenix.entry.AdPolicy;
import com.fphoenix.entry.AdPolicyImpl;
import com.fphoenix.entry.Assets;
import com.fphoenix.entry.SoundPlayer;
import com.fphoenix.gdx.mice.EnemyData;
import com.fphoenix.gdx.mice.HoleLayoutData;
import com.fphoenix.gdx.mice.LevelData;
import com.fphoenix.xutils.Helper;
import com.unity3d.ads.UnityAds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlatformDC {
    public boolean hasClickDaily = false;
    public volatile boolean camera_releasing = false;
    boolean firstOpen;
    boolean video_ready;
    Settings settings = null;
    SoundPlayer player;
	AdPolicy ad_;
	String ad_text_;
    Bundle bundle = new Bundle();
    Platform.PlatformBundle pBundle = new Platform.PlatformBundle();
    int main_count=0;
    long elapsedClockTime;
    long startServerTime = -1;
    int last_played_glv_ = -1;
    int last_played_endless_index_ = -1;
	PolygonSpriteBatch psb = null;
    private List<LevelData> levelDataList;
    private Map<String, HoleLayoutData> holeLayoutDataMap = new HashMap<String, HoleLayoutData>();
    private Map<String, EnemyData> enemyDataMap = new HashMap<String, EnemyData>();
	private Map<String, SpineData> spineDataMap = new HashMap<String, SpineData>();
    private final Map<String, SkeletonData> skeletonDataMap = new HashMap<String, SkeletonData>();
	private Map<String, TextureString> strstr_ = new ConcurrentHashMap<String, TextureString>(40);

	
	public PolygonSpriteBatch getPolygonSpriteBatch(){
		if(this.psb == null){
			this.psb = new PolygonSpriteBatch();
		}
		return this.psb;
	}
    public boolean isVideoReady() {
	    return UnityAds.isReady();
    }

    public int n_level() {
	    return levelDataList.size();
    }

    public HoleLayoutData getHoleLayout(String name) {
        return holeLayoutDataMap.get(name);
    }
    public LevelData getLevelData(int idx) {
	    return levelDataList.get(idx);
    }
    public EnemyData getEnemyData(String name) {
	    return enemyDataMap.get(name);
    }
    public SpineData getSpineData(String key) {
        return spineDataMap.get(key);
    }
    void loadSpineDataMap() {
        List<SpineData> list = Helper.loadCSV(SpineData.class, "csv/spine_data.csv");
        for (SpineData d : list) {
            d.init();
            spineDataMap.put(d.key(), d);
        }
    }
    public SkeletonData loadSkeletonData(String file, AttachmentLoader loader) {
        if (file.endsWith(".json")) {
            SkeletonJson json = new SkeletonJson(loader);
            return json.readSkeletonData(Gdx.files.internal(file));
        }else if (file.endsWith(".skel")) {
            SkeletonBinary bin = new SkeletonBinary(loader);
            return bin.readSkeletonData(Gdx.files.internal(file));
        }
        throw new IllegalArgumentException("spine file has an invalid ext, i.e. neither '.json' nor '.skel'");
    }

    public SkeletonData getSkeletonData(SpineData spineData, boolean update_texture) {
        String file = spineData.spineFile();
        SkeletonData result = skeletonDataMap.get(file);
        TextureAtlas ta = Utils.load_get(spineData.spineAtlas());
        if (result == null) {
            result = loadSkeletonData(file, new AtlasAttachmentLoader(ta));
            skeletonDataMap.put(file, result);
        }else if (update_texture)
            resetTextureResourceToSpine(result, ta);
        return result;
    }

    public void cacheSkeletonData(SpineData spineData, AttachmentLoader loader) {
        String key = spineData.spineFile();
        SkeletonData skeletonData = skeletonDataMap.get(key);
        if (skeletonData == null) {
            skeletonData = loadSkeletonData(key, loader);
            if (!skeletonDataMap.containsKey(key))
                skeletonDataMap.put(key, skeletonData);
        }
    }
    public void resetTextureResources(boolean load_if_not, SpineData ...spineDatas) {
        for (SpineData spineData : spineDatas) {
            TextureAtlas ta = Utils.load_get(spineData.spineAtlas());
            String file = spineData.spineFile();
            SkeletonData skeletonData = skeletonDataMap.get(file);
            if (skeletonData!=null) {
                resetTextureResourceToSpine(skeletonData, ta);
            }else if (load_if_not) {
                skeletonData = loadSkeletonData(file, new AtlasAttachmentLoader(ta));
                skeletonDataMap.put(file, skeletonData);
            }
        }
    }
    static SkeletonData resetTextureResourceToSpine(SkeletonData skeletonData, TextureAtlas ta) {
        Array<Skin> skins = skeletonData.getSkins();
        for (Skin skin : skins) {
            restoreSkin(skin, ta);
        }
        return skeletonData;
    }
    static void restoreSkin(Skin skin, TextureAtlas ta) {
        Skin.SkinEntry se = skin.iterator();
        while (se.hasNext()) {
            se.next();
            Attachment attachment = se.attachment();
            restoreAttachment(attachment, ta);
        }
    }
    static void restoreAttachment(Attachment attachment, TextureAtlas ta) {
        if (attachment instanceof RegionAttachment) {
            RegionAttachment ra = (RegionAttachment)attachment;
            ra.setRegion(ta.findRegion(ra.getPath()));
            ra.updateOffset();
        }else if (attachment instanceof MeshAttachment) {
            MeshAttachment ma = (MeshAttachment)attachment;
            ma.setRegion(ta.findRegion(ma.getPath()));
            ma.updateUVs();
        }else if (attachment instanceof SkinnedMeshAttachment) {
            SkinnedMeshAttachment sma = (SkinnedMeshAttachment)attachment;
            sma.setRegion(ta.findRegion(sma.getPath()));
            sma.updateUVs();
        }
    }

    public SkeletonData tryGetSkeletonData(String json) {
        return skeletonDataMap.get(json);
    }
    public SoundPlayer getPlayer() {
        return this.player;
    }
    public long getCurrentServerTime() {
        if (startServerTime<0) return -1;
        long d = SystemClock.elapsedRealtime() - this.elapsedClockTime;
        return startServerTime + d;
    }
    public long getStartServerTime() {
        return startServerTime;
    }
    public void setStartServerTime(long startServerTime) {
        this.startServerTime = startServerTime;
        this.elapsedClockTime = SystemClock.elapsedRealtime();
    }
    public boolean isNetOpen() {
        return this.startServerTime > 0;
    }

    public TextureString getTextureString(String tag) {
        return strstr_.get(tag);
    }
    public void addTextureString(String tag, TextureString ts) {
        strstr_.put(tag, ts);
    }

//	public TextureAtlas getDebugTa() {return debug_ta;}
//    public TextureRegion getDebugMask1() {
//	    return debug_mask1;
//    }
	public BitmapFont tryGetBMF() {
		return tryGetBMF(Assets.font1);
	}
    public BitmapFont tryGetBMF(String tag) {
        AssetManager assetManager = Utils.getAssetManager();
        boolean ok = assetManager.isLoaded(tag, BitmapFont.class);
        return ok ? assetManager.get(tag, BitmapFont.class) : null;
    }
    public int getMainCount() {
        return main_count;
    }
    public int incMainCount() {
        return ++main_count;
    }
    public boolean isFirstOpen() {
        return firstOpen;
    }
	public AdPolicy getAdPolicy() {
		return ad_;
	}
	// "level_ad, health_ad, revive_ad, success_ad,
	public String currentAD() {
	    return ad_text_;
    }
    public PlatformDC setCurrentVideoAD(String ad_text) {
	    this.ad_text_ = ad_text;
	    return this;
    }
	public Settings getSettings() {
        return this.settings;
    }
    public Bundle getBundle() {
        return bundle.clear();
    }
    private void load_csv() {
        loadSpineDataMap();
        {
            List<EnemyData> list = Helper.loadCSV(EnemyData.class, "csv/enemy.csv");
            for (EnemyData d : list) {
                d.init();
                enemyDataMap.put(d.key(), d);
            }
        }
        String level_name = DoodleGame.get().isSupportTF() ? "csv/level.csv" : "csv/level_low.csv";
        levelDataList = Helper.loadCSV(LevelData.class, level_name);
        for (int i=0,n=levelDataList.size(); i<n; i++) {
            levelDataList.get(i).init();
        }
        {
            List<HoleLayoutData> list = Helper.loadCSV(HoleLayoutData.class, "csv/hole_layout.csv");
            for (HoleLayoutData d : list) {
                holeLayoutDataMap.put(d.name(), d.check());
            }
        }
    }
    private void load_spine() {

    }

    private void load_particles() {
    }
    private void load_resource() {
//	    debug_ta = Utils.load_get("debug.atlas");
//	    TextureRegion region = debug_ta.findRegion("mask3");
//	    debug_mask1 = new TextureRegion(region, 1,1,1,1);
        load_csv();
        load_spine();
        load_particles();
    }

    private Hub<MessageChannels.Message> hub;
	private MessageChannels.Message message = new MessageChannels.Message();

    public Hub<MessageChannels.Message> getHub() {
        return hub;
    }

    public MessageChannels.Message getMessage() {
        return message;
    }

    public void setHub(Hub<MessageChannels.Message> hub) {
        this.hub = hub;
    }

    public int last_played_glv() {return last_played_glv_;}
    public int last_played_endless_index() {return last_played_endless_index_;}
    public void last_played_glv(int lv){last_played_glv_=lv;}
    public void last_played_endless_index(int index){last_played_endless_index_=index;}

    private static PlatformDC self_;
	private Platform platform_;
    private Preferences db_;
//    private TextureAtlas debug_ta;
//    private TextureRegion debug_mask1;
	
	public Platform platform(){
		return platform_;
	}
    public Platform.PlatformBundle getPlatformBundle() {
        return this.pBundle;
    }
    private static final String DB_TAG = "UGLY_FACE";
	public void init2() {
        Preferences pref = Gdx.app.getPreferences(DB_TAG);
        this.db_ = pref;
        settings = new Settings();
        settings.init(pref);
        if (settings.isAdFree()) {
            ad_ = new AdPolicy.AdFree();
        }else {
            ad_ = new AdPolicyImpl();
        }
        this.player = new SoundPlayer();
        // load settings
        load_resource();
	}
    public PlatformDC setDB(Preferences db) {
        this.db_ = db;
        return this;
    }
    public Preferences getDB() {
        return this.db_;
    }
	public static void init(Platform platform){
		self_ = new PlatformDC();
		self_.platform_ = platform;
	}
	public static void destroy() {
        self_.player.dispose();
		self_ = null;
	}
	public static PlatformDC get() {
		return self_;
	}
}
