package com.fphoenix.common;

/**
 * Created by alan on 18-4-26.
 */

public interface DataSource {
    int get(String key, int default_int);
    String get(String key, String default_string);
}
