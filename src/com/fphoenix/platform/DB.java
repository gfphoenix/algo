package com.fphoenix.platform;

/**
 * Created by wuhao on 12/2/16.
 */
public interface DB {
    int getInt(String key, int def_value);
    long getLong(String key, long def_value);
    boolean getBool(String key, boolean def_value);
    void setInt(String key, int value);
    void setLong(String key, long value);
    void setBool(String key, boolean value);
}
