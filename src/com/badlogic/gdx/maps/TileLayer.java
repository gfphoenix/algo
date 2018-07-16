package com.badlogic.gdx.maps;

/**
 * Created by alan on 18-4-2.
 */

public class TileLayer extends MapLayer {
    private int width;
    private int height;

    private float tileWidth;
    private float tileHeight;
    int []ids;

    /**
     * @return layer's witdth in tiles
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return layer's height in tiles
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return tiles' width in pixels
     */
    public float getTileWidth() {
        return tileWidth;
    }

    /**
     * @return tiles' height in pixels
     */
    public float getTileHeight() {
        return tileHeight;
    }

    /**
     * Creates TiledMap layer
     *
     * @param width layer width in tiles
     * @param height layer height in tiles
     * @param tileWidth tile width in pixels
     * @param tileHeight tile height in pixels
     */
    public TileLayer(int width, int height, int tileWidth, int tileHeight) {
        super();
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }
    /**
     * @param x
     * @param y
     * @return cell at (x, y)
     */
    public int getCell(int x, int y) {
        if(x < 0 || x >= width) return 0;
        if(y < 0 || y >= height) return 0;
        return ids[x*width+y];
    }

    public void setCell(int x, int y, int gid) {
        if(x < 0 || x >= width) return;
        if(y < 0 || y >= height) return;
        ids[x*width+y] = gid;
    }
    public int [] getRawID() {
        return ids;
    }
}
