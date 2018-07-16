package com.esotericsoftware.spine;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState.TrackEntry;

public class MyAnimationHook {
    Map<String, Array<MyUevent>> maps = new HashMap<String, Array<MyUevent>>(4);

    protected static int b_min(Array<MyUevent> data, float key) {
        int len = data.size;
        if (data.get(len - 1).percent <= key)
            return -1;
        int l = 0, r = len - 1;
        while (l <= r) {
            int m = (r + l) / 2;
            if (data.get(m).percent <= key) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return l;
    }
    public void trigger(String animation, TrackEntry current, int i){
        Array<MyUevent> a = maps.get(animation);

        if(a!=null && a.size>0){
            float last = current.lastTime / current.endTime;
            float time = current.time / current.endTime;
            if(last>0){
                last -= Math.floor(last);
            }
            time -= Math.floor(time);
            float key = last > time ? -1f : last;
            int ii = b_min(a, key);
            if(ii<0) return;
            MyUevent e;
            while(ii<a.size){
                e = a.get(ii);
                if(e.percent > time)
                    break;
                if(e.invoke(i, e.userData)){
                    a.removeIndex(ii);
                }else{
                    ii++;
                }
            }
        }
    }

    public void register(MyUevent e) {
        if (e==null || e.animation==null)
            throw new NullPointerException("e and animation must not be null");
        Array<MyUevent> a = maps.get(e.animation);
        if (a == null) a = new Array<MyUevent>();
        a.add(e);
        a.sort();
        maps.put(e.animation, a);
    }
    public void clear() {
        maps.clear();
    }

    void trigger_first(Array<MyUevent> array, TrackEntry current, float time) {
        for (int i = 0; i < array.size; ) {
            MyUevent e = array.get(i);
            if (e.percent > time) break;
            if (e.invoke(0, e.userData)) {
                array.removeIndex(i);
            } else {
                i++;
            }
        }
    }

    void trigger2(TrackEntry current, float time) {
        String animation = current.animation.getName();
        Array<MyUevent> arr = maps.get(animation);
        if (arr == null || arr.size == 0) return;

        final float lastTime = current.lastTime;
        final float endTime = current.endTime;

        if (lastTime <= 0) {
            trigger_first(arr, current, time);
            return;
        }

        final float last = lastTime % endTime;
        final float end = time % endTime;
        final float last_percent = last / endTime;
        final float end_percent = end / endTime;
        final int count = (int)(time/endTime);
        if (current.loop) {
            if (last > end) {
                // end + start
                int k = b_min(arr, last_percent);
                if (k>=0)
                    while (k<arr.size) {
                        MyUevent e = arr.get(k);
                        if (e.invoke(count-1, e.userData)) {
                            arr.removeIndex(k);
                        }else {
                            k++;
                        }
                    }
                k=0;
                while (k<arr.size) {
                    MyUevent e = arr.get(k);
                    if (e.percent>end_percent) break;
                    if (e.invoke(count, e.userData)) {
                        arr.removeIndex(k);
                    }else {
                        k++;
                    }
                }
            } else {
                // last ~ time
                int ii = b_min(arr, last_percent);
                if (ii<0) return;
                MyUevent e;
                while (ii < arr.size) {
                    e = arr.get(ii);
                    if (e.percent > end_percent)
                        break;
                    if (e.invoke(count, e.userData)) {
                        arr.removeIndex(ii);
                    } else {
                        ii++;
                    }
                }
            }
        } else {
            // last ~ min(time, end), no loop
            int ii = b_min(arr, last_percent);
            if (ii<0) return;
            MyUevent e;
            while (ii<arr.size) {
                e = arr.get(ii);
                if (e.percent>end_percent) break;
                if (e.invoke(count, e.userData)) {
                    arr.removeIndex(ii);
                }else {
                    ii++;
                }
            }
        }
    }
}
