package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import controller.Tile;

/**
 * Stores a representation of the state of the game. All incoming and outgoing cell locations assume the same convention used in the Tile class in the Controller
 * package. Specifically: The point information is to be in (x,y) coordinates where (0,0) refers to the top-left cell, while (1,2) would be one cell down and two
 * cells to the right. However this need not be representative for how the information is stored internally.
 * 
 * @author Robert Stuart
 */
class Board implements Serializable {
    private static final long serialVersionUID = 7603293598183278839L;
    private int[][] grid;
    private int width, height;
    private int score, highScore;
    private boolean isGameWon, isGameOver;

    /**
     * Returns the value of the given cell location if the cell is on the board, otherwise 0;
     * 
     * @param cell The location where the value is being queried.
     * @return The value at the given location.
     */
    protected int valueAt(Point cell) {
	if (isContained(cell)) return grid[cell.x][cell.y];
	return 0;
    }

    /**
     * Returns the value of the given cell location if the cell is on the board, otherwise 0;
     * 
     * @param x x coordinate of the location where the value is being queried.
     * @param y y coordinate of the location where the value is being queried.
     * @return The value at the given location.
     */
    protected int valueAt(int x, int y) {
	if (isContained(x, y)) return grid[x][y];
	return 0;
    }

    private boolean isContained(int x, int y) {
	if (x < 0 || x >= width || y < 0 || y >= height) return false;
	return true;
    }

    /**
     * A check to see if the given cell is a valid location on this board.
     * 
     * @param cell The cell location in question.
     * @return True if the cell is a valid location on this board, false otherwise.
     */
    protected boolean isContained(Point cell) {
	if (cell.x < 0 || cell.x >= width || cell.y < 0 || cell.y >= height) return false;
	return true;
    }

    /**
     * Sets the value of the given cell, if contained on this board, to the value given.
     * 
     * @param cell The location where the value will be changed.
     * @param val The value the given cell will be changed to.
     */
    protected void setCell(Point cell, int val) {
	if (isContained(cell)) grid[cell.x][cell.y] = val;
    }

    protected int getWidth() {
	return width;
    }

    protected int getHeight() {
	return height;
    }

    protected void setSize(int width, int height) {
	this.width = width;
	this.height = height;
	grid = new int[width][height];
    }

    protected int getScore() {
	return score;
    }

    protected void setScore(int score) {
	this.score = score;
    }

    protected int getHighScore() {
	return highScore;
    }

    protected void setHighScore(int highScore) {
	this.highScore = highScore;
    }

    protected boolean isGameWon() {
	return isGameWon;
    }

    protected void setGameWon(boolean isGameWon) {
	this.isGameWon = isGameWon;
    }

    protected boolean isGameOver() {
	return isGameOver;
    }

    protected void setGameOver(boolean isGameOver) {
	this.isGameOver = isGameOver;
    }

    protected List<Tile> getAllTiles() {
	LinkedList<Tile> list = new LinkedList<Tile>();
	for (int x = 0; x < width; x++)
	    for (int y = 0; y < height; y++) {
		if (grid[x][y] == 0) continue;
		list.add(new Tile(new Point(x, y), grid[x][y]));
	    }
	return list;
    }

    @Override
    public String toString() {
	StringBuilder str = new StringBuilder();
	str.append("Width:" + width);
	str.append(",Height:" + height);
	str.append(",Score:" + score);
	str.append(",HighScore:" + highScore);
	str.append(",isGameWon:" + isGameWon);
	str.append(",isGameOver:" + isGameOver);
	str.append(";\n");
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		str.append(grid[x][y] + "\t");
	    }
	    str.append("\n");
	}
	return str.toString();
    }

}