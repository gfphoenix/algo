package com.badlogic.gdx.maps;

/**
 * Created by alan on 18-4-2.
 */

public class TileSet {

    private String name;
    int firstGID;
    int tileWidth;
    int tileHeight;
    int tileCount;
    private String resource;
    private MapProperties properties;

    /**
     * @return tileset's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name new name for the tileset
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return tileset's properties set
     */
    public MapProperties getProperties() {
        return properties;
    }
}