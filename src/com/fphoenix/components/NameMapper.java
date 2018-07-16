package com.fphoenix.components;

/**
 * Created by alan on 18-4-11.
 */

public interface NameMapper {
    String s(String name);
    public static final NameMapper I = new NameMapper() {
        @Override
        public String s(String name) {
            return name;
        }
    };
}
