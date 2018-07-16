package com.fphoenix.components;

import com.smilerlee.util.lcsv.Column;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wuhao on 8/18/16.
 */
public final class SpineData implements NameMapper {
    public static final String TAG = "spine-data";
    @Column(name="A")
    String 	key;
    @Column(name="B")
    String spine_file;
    @Column(name="C")
    String 	atlas;
    @Column(name="D")
    String 	skin;
    @Column(name="E")
    String animations;
    @Column(name = "F")
    String _misc;

    private Map<String, String> str_map;
    private Map<String, String> misc_map;

    @Override
    public String s(String str) {
        String val = str_map.get(str);
        return val==null?str:val;
    }
    public String misc(String key) {
        return misc_map!=null ? misc_map.get(key) : null;
    }
    public Set<String> getAnimationKeySet() {
        return str_map.keySet();
    }
    public void putString(String key, String value) {
        str_map.put(key, value);
    }
    public String key() {
        return key;
    }
    public String spineAtlas() {
        return atlas;
    }
    private String getPrefix() {
        return "spines/";
    }
    // default suffix, must be in ".json", "skel"
    private String getDefaultSuffix() {
        return ".json";
    }
    public String spineFile() {
        String pref = getPrefix();
        if (spine_file.endsWith(".skel") || spine_file.endsWith(".json"))
            return pref + spine_file;
        return pref + spine_file + getDefaultSuffix();
    }

    public String skinName() {
        return skin;
    }

    public static Map<String, String> parseMapString(String str, Map<String, String> out) {
        if (str==null || str.length()==0) return out;
        String []parts = str.split(":");
        if (out==null) out = new HashMap<String, String>(parts.length);
        for (String s : parts) {
            String []kv = s.split("=", 2);
            out.put(kv[0], kv[1]);
        }
        return out;
    }
    public final boolean init() {
        if (animations == null) return true;
        str_map = parseMapString(animations, null);
        misc_map = parseMapString(_misc, null);
        return true;
    }
}
