package net.graph;
/**
 * Created by wuhao on 8/15/16.
 */
// like a board
public class GridGraph {
    public static class Cell {

    }
    Cell [][]cells;
    public int rows(){
        return cells.length;
    }
    public int cols() {
        return cells[0].length;
    }
    public Cell at(int row, int col) {
        return cells[row][col];
    }
}
