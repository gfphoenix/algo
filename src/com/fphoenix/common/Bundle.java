package com.fphoenix.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuhao on 11/1/16.
 */
public class Bundle implements DataSource {
    Map<String, Integer> integerMap;
    Map<String, String> stringStringMap;
    Map<String, Object> objectMap;

    public Bundle() {
        integerMap = new HashMap<String, Integer>();
        stringStringMap = new HashMap<String, String>();
    }

    public Bundle(int i_cap, int s_cap) {
        integerMap = new HashMap<String, Integer>(i_cap);
        stringStringMap = new HashMap<String, String>(s_cap);
    }

    public Bundle copy() {
        Bundle cp = new Bundle();
        cp.integerMap.putAll(integerMap);
        cp.stringStringMap.putAll(stringStringMap);
        if (objectMap != null) {
            cp.objectMap = new HashMap<String, Object>(objectMap.size());
            cp.objectMap.putAll(objectMap);
        }
        return cp;
    }

    public Bundle clear() {
        integerMap.clear();
        stringStringMap.clear();
        if (objectMap != null) objectMap.clear();
        return this;
    }

    public Bundle put(String key, int i_value) {
        integerMap.put(key, i_value);
        return this;
    }

    public Bundle put(String key, String s_value) {
        stringStringMap.put(key, s_value);
        return this;
    }

    public Bundle putObject(String key, Object o) {
        if (objectMap == null)
            objectMap = new HashMap<String, Object>(2);
        objectMap.put(key, o);
        return this;
    }

    public int get(String key, int default_int) {
        Integer i = integerMap.get(key);
        return i == null ? default_int : i;
    }

    public String get(String key, String default_string) {
        String s = stringStringMap.get(key);
        return s == null ? default_string : s;
    }

    public <T> T getObject(String key, Class<T> clazz) {
        Object o = objectMap == null ? null : objectMap.get(key);
        if (o == null) return null;
        if (clazz.isAssignableFrom(o.getClass())) {
            return (T) o;
        }
        throw new ClassCastException("class " + o.getClass() + " cast to " + clazz + " failed");
    }
}
