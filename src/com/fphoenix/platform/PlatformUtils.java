package com.fphoenix.platform;

/**
 * Created by wuhao on 12/6/16.
 */
public class PlatformUtils {
    private PlatformUtils() {
    }
    public static Platform.PlatformBundle query(int code) {
        PlatformDC dc = PlatformDC.get();
        Platform.PlatformBundle bundle = dc.getPlatformBundle();
        dc.platform().callPlatform(code, bundle);
        return bundle;
    }
    public static boolean isShowingFullAd() {
        Platform.PlatformBundle bundle = query(Platform.CODE_IS_SHOWING_FULL_AD);
        return bundle.b;
    }
    public static void call0(int code) {
        PlatformDC d=PlatformDC.get();
        if(d!=null) d.platform().callPlatform(code, null);
    }

    public static void buyGoods(int idx) {
        PlatformDC dc = PlatformDC.get();
        Platform.PlatformBundle bundle = dc.getPlatformBundle();
        bundle.i = idx;
        dc.platform().callPlatform(Platform.CODE_BUY_GOODS, bundle);
    }

    private static void ad(int code) {
        PlatformDC dc = PlatformDC.get();
        Settings settings = dc.getSettings();
        if (!settings.isAdFree())
            dc.platform().callPlatform(code, null);
    }

    public static void showFeatureView() {
        ad(Platform.CODE_SHOW_FEATURE_VIEW);
    }

    public static void closeFeatureView() {
        ad(Platform.CODE_CLOSE_FEATURE_VIEW);
    }

    public static void showFullAd() {
        ad(Platform.CODE_SHOW_FULL_AD);
    }

    public static void showFullExitAd() {
        ad(Platform.CODE_SHOW_EXIT_FULL_AD);
    }

    public static void closeFullAd() {
        ad(Platform.CODE_CLOSE_FULL_AD);
    }

    public static void forceCloseAd() {
        PlatformDC dc = PlatformDC.get();
        dc.platform().callPlatform(Platform.CODE_CLOSE_FULL_AD, null);
        dc.platform().callPlatform(Platform.CODE_CLOSE_FEATURE_VIEW, null);
    }

    public static void flurry(FlurryMessage message) {
        PlatformDC dc = PlatformDC.get();
        Platform.PlatformBundle bundle = dc.getPlatformBundle();
        bundle.o = message;
        dc.platform().callPlatform(Platform.CODE_FLURRY_EVENT, bundle);
    }

    public static boolean isFocused(boolean initValue) {
        PlatformDC dc = PlatformDC.get();
        Platform.PlatformBundle bundle = dc.getPlatformBundle();
        bundle.b = true;
        dc.platform().callPlatform(Platform.CODE_IS_FOCUSED, bundle);
        return bundle.b;
    }
}
