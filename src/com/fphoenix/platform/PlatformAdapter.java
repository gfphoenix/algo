package com.fphoenix.platform;


import com.badlogic.gdx.Gdx;

import java.util.Locale;
import java.util.TimeZone;

// intends to be platform independent,
// ignore ad on pc
public class PlatformAdapter implements Platform {
    private boolean show_full_ad;
    @Override
    public void callPlatform(int code, PlatformBundle bundle) {
        switch (code) {
            case Platform.CODE_BUY_GOODS:
                buyGoods(bundle.i);
                break;
            case Platform.CODE_FLURRY_EVENT:
                debug_flurry((FlurryMessage) bundle.o);
                break;
            case Platform.CODE_SHOW_EXIT_FULL_AD:
                show_full_ad = true;
                break;
            case Platform.CODE_CLOSE_FULL_AD:
                show_full_ad = false;
                break;
            case Platform.CODE_IS_SHOWING_FULL_AD:
                bundle.b = show_full_ad;
                break;
            case Platform.CODE_REQUEST_UPDATE_TIME: {
                long local_time = (System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) / 1000;
                PlatformDC dc = PlatformDC.get();
                if (dc!=null) dc.setStartServerTime(local_time);
                break;
            }
            case Platform.CODE_IS_FOCUSED:
                bundle.b = true;
                break;
        }
    }

    protected void buyGoods(int idx) {
        System.out.println("buy good " + idx);
//        ShopScreen.onSuccess(idx);
    }

    private void debug_flurry(FlurryMessage message) {
        Gdx.app.log("FLURRY", String.format(Locale.US, "[%s - %s : %s", message.event, message.key, message.val));
    }
}
