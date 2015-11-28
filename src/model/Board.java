package model;

import java.awt.Point;

/**
 * Stores a representation of the state of the game. All incoming and outgoing cell locations assume the same convention used in the Tile class in the Controller package. Specifically: The point information is
 * to be in (x,y) coordinates where (0,0) refers to the top-left cell, while (1,2) would be one cell down and two cells to the right. However this need not be representative for how the information is stored
 * internally.
 * 
 * @author Robert Stuart
 */
class Board {
    private int[][] grid;
    private int width, height;
    private int score, highScore;
    private boolean isGameWon, isGameOver;

    /**
     * Returns the value of the given cell location if the cell is on the board, otherwise 0;
     * 
     * @param cell
     *            The location where the value is being queried.
     * @return The value at the given location.
     */
    protected int valueAt(Point cell) {
	if (isContained(cell)) return grid[cell.x][cell.y];
	return 0;
    }

    /**
     * Returns the value of the given cell location if the cell is on the board, otherwise 0;
     * 
     * @param x
     *            x coordinate of the location where the value is being queried.
     * @param y
     *            y coordinate of the location where the value is being queried.
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
     * @param cell
     *            The cell location in question.
     * @return True if the cell is a valid location on this board, false otherwise.
     */
    protected boolean isContained(Point cell) {
	if (cell.x < 0 || cell.x >= width || cell.y < 0 || cell.y >= height) return false;
	return true;
    }

    /**
     * Sets the value of the given cell, if contained on this board, to the value given.
     * 
     * @param cell
     *            The location where the value will be changed.
     * @param val
     *            The value the given cell will be changed to.
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

    protected boolean stringToBoard(String str) {
	if (str == null) return false;
	if (!str.contains(";")) return false;
	String[] parts = str.split(";");
	if (parts.length != 2) return false;
	String[] state = parts[0].split(",");
	// parse width, height, score, highscore, isGameWon, isGameOver information
	String[] lookFor = { "Width:\\d+", "Height:\\d+", "Score:\\d+", "HighScore:\\d+", "isGameWon:(false|true)", "isGameOver:(false|true)" };
	if (state.length != lookFor.length) return false;
	if (!state[0].matches(lookFor[0])) return false;
	int tempWidth = Integer.parseInt(state[0].substring(state[0].indexOf(":") + 1));
	if (tempWidth < Rules.DFLT_WIDTH) return false;
	if (!state[1].matches(lookFor[1])) return false;
	int tempHeight = Integer.parseInt(state[1].substring(state[1].indexOf(":") + 1));
	if (tempHeight < Rules.DFLT_HEIGHT) return false;
	if (!state[2].matches(lookFor[2])) return false;
	int tempScore = Integer.parseInt(state[2].substring(state[2].indexOf(":") + 1));
	if (tempScore < 0) return false;
	if (!state[3].matches(lookFor[3])) return false;
	int tempHighScore = Integer.parseInt(state[3].substring(state[3].indexOf(":") + 1));
	if (tempHighScore < tempScore) return false;
	if (!state[4].matches(lookFor[4])) return false;
	boolean tempIsGameWon = Boolean.parseBoolean(state[4].substring(state[4].indexOf(":") + 1));
	if (!state[5].matches(lookFor[5])) return false;
	boolean tempIsGameOver = Boolean.parseBoolean(state[5].substring(state[5].indexOf(":") + 1, state[5].length() - 2));
	// parse the game board
	String[] rows = parts[1].trim().split("\n");
	if (rows.length != tempHeight) return false;
	int[][] tempBoard = new int[tempWidth][tempHeight];
	String[] values = null;
	for (int y = 0; y < rows.length; y++) {
	    values = rows[y].split("\t");
	    if (values.length != tempWidth) return false;
	    for (int x = 0; x < values.length; x++) {
		if (!values[x].matches("\\d+")) return false;
		tempBoard[x][y] = Integer.parseInt(values[x]);
		if (tempBoard[x][y] >= Rules.DFLT_WINVALUE && tempIsGameWon == false) return false;
	    }
	}
	grid = tempBoard;
	width = tempWidth;
	height = tempHeight;
	score = tempScore;
	highScore = tempHighScore;
	isGameWon = tempIsGameWon;
	isGameOver = tempIsGameOver;
	return true;
    }

}