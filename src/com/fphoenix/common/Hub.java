package com.fphoenix.common;

import com.badlogic.gdx.utils.IntMap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhao on 10/8/16.
 * de-couple the dependency between the event sender and receiver
 */
public class Hub<M> {
    // channel => list of listeners
    private final IntMap<List<WeakReference<Listener<M>>>> listeners = new IntMap<List<WeakReference<Listener<M>>>>();
    public interface Listener<M> {
        // true: processed, should stop any further processing
        // false: not processed, or wanna further processing
        boolean receiveMessage(int channel, M message);
    }
    public void subscribe(Listener<M> listener, int channel) {
        if (listener==null) throw new NullPointerException("listener must not be null");
        List<WeakReference<Listener<M>>> list = listeners.get(channel);
        if (list == null) {
            list = new ArrayList();
            listeners.put(channel, list);
        }else {
            for (WeakReference<Listener<M>> wl : list) {
                if (wl.get() == listener) {
                    throw new RuntimeException("duplicate listener for the same channel="+channel);
                }
            }
        }
        list.add(new WeakReference<Listener<M>>(listener));
    }
    public void subscribe(Listener<M> listener, int ...channels) {
        for (int ch : channels)
            subscribe(listener, ch);
    }
    public void unsubscribe_channel(int channel) {
        listeners.remove(channel);
    }
    public void unsubscribe(int channel, Listener<M> listener) {
        if (listener==null) return;
        List<WeakReference<Listener<M>>> list = listeners.get(channel);
        if (list==null) return;
        for (int i=list.size(); i-->0; ) {
            WeakReference<Listener<M>> w = list.get(i);
            Listener<M> l = w.get();
            if (l==null){
                list.remove(i);
            }else if (l==listener) {
                list.remove(i);
                break;
            }
        }
        if (list.size()==0)
            listeners.remove(channel);
    }
    public void clearAll() {
        listeners.clear();
    }
//    return number of receive-message for all channels
    public int sendMessage(M message, int ...channel){
        int cc = 0;
        for (int ch : channel)
            cc += sendMessage(message, ch);
        return cc;
    }
    // return number of receive-message
    public int sendMessage(M message, int channel) {
        List<WeakReference<Listener<M>>> list = listeners.get(channel);
        int cc = 0;
        if (list!=null) {
            for (int i = list.size(); i-- > 0; ) {
                WeakReference<Listener<M>> w = list.get(i);
                Listener<M> l = w.get();
                if (l == null) {
                    list.remove(i);
                } else {
                    boolean stop = l.receiveMessage(channel, message);
                    cc++;
                    if (stop) break;
                }
            }
            if (list.size() == 0)
                listeners.remove(channel);
        }
        return cc;
    }
    // if receiver is null, it equals the above twos
    public int sendMessage(M message, Listener<M> receiver, int ...channels) {
        if (receiver==null) return sendMessage(message, channels);
        for (int ch : channels)
            receiver.receiveMessage(ch, message);
        return channels.length;
    }
    public int sendMessage(M message, Listener<M> receiver, int channel) {
        if (receiver==null) return sendMessage(message, channel);
        receiver.receiveMessage(channel, message);
        return 1;
    }
}
