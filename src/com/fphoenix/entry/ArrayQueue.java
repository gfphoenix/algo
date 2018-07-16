package com.fphoenix.entry;

import java.util.ArrayList;

/**
 * Created by wuhao on 12/16/16.
 */
// all apis are not thread safe
public class ArrayQueue<T> {
    ArrayList<T> out = new ArrayList<T>();
    ArrayList<T> in = new ArrayList<T>();
    int readIndex=0;

    public int size() {
        return out.size()-readIndex + in.size();
    }
    // make sure that idx is legal
    public T get(int idx) {
        if (readIndex >= out.size() && !in.isEmpty()) {
            ArrayList<T> tmp = out;
            out = in;
            in = tmp;
            in.clear();
            readIndex = 0;
        }
        if (readIndex>=out.size()) throw new RuntimeException("get in an empty queue");
        int n = out.size() - readIndex;
        if (idx<n) return out.get(readIndex+idx);
        idx -= n;
        if (idx>=in.size()) throw new RuntimeException("get in an empty queue");
        return in.get(idx);
    }
    public boolean empty() {
        return readIndex>=out.size() && in.isEmpty();
    }
    public void push(T x) {
        in.add(x);
    }
    public T pop() {
        if (readIndex>=out.size() && !in.isEmpty()) {
            final ArrayList<T> tmp = out;
            out = in;
            in = tmp;
            tmp.clear();
            readIndex = 0;
        }
        if (readIndex>=out.size()) throw new RuntimeException("pop in an empty queue");
        return out.get(readIndex++);
    }
    public void clear() {
        readIndex=0;
        out.clear();
        in.clear();
    }
    public ArrayList<T> getAll(ArrayList<T> list) {
        for (int i=readIndex,n=out.size(); i<n; i++) {
            list.add(out.get(i));
        }
        for (T x : in)
            list.add(x);
        return list;
    }
}
