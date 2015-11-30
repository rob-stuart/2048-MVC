package model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import controller.Direction;
import controller.Tile;

class Rules {

    protected final static int DFLT_WIDTH = 4, DFLT_HEIGHT = 4;
    protected final static int DFLT_STARTNUM = 2, DFLT_WINVALUE = 2048;

    protected static List<Tile> startGame(Board board, int width, int height) {
	if (width < DFLT_WIDTH || height < DFLT_HEIGHT) throw new IllegalArgumentException();
	board.setSize(width, height);
	board.setScore(0);
	board.setHighScore(0);
	board.setGameOver(false);
	board.setGameWon(false);
	return addStartTiles(board);
    }

    protected static List<Tile> restartGame(Board board, int width, int height) {
	if (width < DFLT_WIDTH || height < DFLT_HEIGHT) throw new IllegalArgumentException();
	board.setSize(width, height);
	board.setScore(0);
	board.setGameOver(false);
	board.setGameWon(false);
	return addStartTiles(board);
    }

    private static List<Tile> addStartTiles(Board board) {
	List<Tile> additions = new LinkedList<Tile>();
	for (int i = 0; i < DFLT_STARTNUM; i++) {
	    Tile tmp = addRandomTile(board);
	    if (tmp != null) additions.add(tmp);
	}
	return additions;
    }

    private static Tile addRandomTile(Board board) {
	Tile tile = null;
	if (cellsAvailable(board)) {
	    Point cell = randomAvailCell(board);
	    board.setCell(cell, Math.random() < 0.9 ? 2 : 4);
	    tile = new Tile(cell, board.valueAt(cell));
	}
	return tile;
    }

    private static Point randomAvailCell(Board board) {
	List<Point> cells = availableCells(board);
	if (!cells.isEmpty()) return cells.get((int) Math.floor(Math.random() * cells.size()));
	return null;
    }

    private static List<Point> availableCells(Board board) {
	List<Point> locs = new LinkedList<Point>();
	for (int x = 0; x < board.getWidth(); x++)
	    for (int y = 0; y < board.getHeight(); y++)
		if (board.valueAt(x, y) == 0) locs.add(new Point(x, y));
	return locs;
    }

    private static boolean cellsAvailable(Board board) {
	if (availableCells(board).isEmpty()) return false;
	return true;
    }

    protected static List<Tile> makeMove(Board board, Direction d) {
	LinkedList<Tile> moves = new LinkedList<Tile>();
	Point vector = getVector(d);
	int[] xTraversal = getXTraversal(board, vector);
	int[] yTraversal = getYTraversal(board, vector);
	for (int x : xTraversal) {
	    for (int y : yTraversal) {
		Point current = new Point(x, y);
		if (board.valueAt(current) != 0) {
		    Point farthest, next = current;
		    do {
			farthest = next;
			next = new Point(farthest.x + vector.x, farthest.y + vector.y);
		    } while (board.isContained(next) && board.valueAt(next) == 0);
		    if (board.isContained(next) && board.valueAt(current) == board.valueAt(next) && canMerge(board, moves, next)) {// merge current & next position
			Tile aMove = new Tile(current, board.valueAt(current));	// set initial position
			aMove.merge(next, board.valueAt(current) * 2);	// set final position and value
			deleteMergeInto(board, moves, next);	// mark Tile being merged into as being deleted
			moves.add(aMove);
			board.setCell(next, 2 * board.valueAt(current));
			board.setScore(board.getScore() + board.valueAt(next));
			board.setHighScore(board.getScore() > board.getHighScore() ? board.getScore() : board.getHighScore());
			board.setCell(current, 0);
			if (board.valueAt(next) == DFLT_WINVALUE) board.setGameWon(true);
		    } else if (current != farthest) {// no merge, just move current to the farthest position. Do nothing if this Tile cannot be moved.
			Tile aMove = new Tile(current, board.valueAt(current));
			aMove.move(farthest);
			moves.add(aMove);
			board.setCell(farthest, board.valueAt(current));
			board.setCell(current, 0);
		    }
		}
	    }
	}
	if (!moves.isEmpty()) {
	    moves.add(addRandomTile(board));
	    if (!movesAvailable(board)) board.setGameOver(true);
	}
	return moves;
    }

    private static boolean movesAvailable(Board board) {
	return cellsAvailable(board) || tileMatchesAvailable(board);
    }

    private static boolean tileMatchesAvailable(Board board) {
	for (int x = 0; x < board.getWidth(); x++)
	    for (int y = 0; y < board.getHeight(); y++) {
		Point cell = new Point(x, y);
		if (board.valueAt(cell) != 0) {
		    for (Direction d : Direction.values()) {
			Point vector = getVector(d);
			Point next = new Point(cell.x + vector.x, cell.y + vector.y);
			if (board.isContained(next) && board.valueAt(cell) == board.valueAt(next)) return true;
		    }
		}
	    }
	return false;
    }

    private static void deleteMergeInto(Board board, List<Tile> moves, Point next) {
	boolean found = false;
	for (Tile move : moves)
	    if (move.getCurLoc().equals(next)) {
		move.merged();
		move.delete();
		found = true;
		break;
	    }
	if (!found) {// tile being merged into wasn't moved earlier, so now add it in and mark as deleted
	    Tile move = new Tile(next, board.valueAt(next));
	    move.merged();
	    move.delete();
	    moves.add(move);
	}
    }

    private static boolean canMerge(Board board, List<Tile> moves, Point next) {
	for (Tile move : moves)
	    if (move.getCurLoc().equals(next) && move.isMerged()) return false;
	return true;
    }

    private static int[] getYTraversal(Board board, Point vector) {
	int height = board.getHeight();
	int[] yTraversal = new int[height];
	if (vector.y == 1) {
	    for (int i = 0; i < height; i++)
		yTraversal[i] = height - 1 - i;
	} else {
	    for (int i = 0; i < height; i++)
		yTraversal[i] = i;
	}
	return yTraversal;
    }

    private static int[] getXTraversal(Board board, Point vector) {
	int width = board.getWidth();
	int[] xTraversal = new int[width];
	if (vector.x == 1) {
	    for (int i = 0; i < width; i++)
		xTraversal[i] = width - 1 - i;
	} else {
	    for (int i = 0; i < width; i++)
		xTraversal[i] = i;
	}
	return xTraversal;
    }

    private static Point getVector(Direction d) {
	Point vector;
	switch (d) {
	case UP:
	    vector = new Point(0, -1);
	    break;
	case RIGHT:
	    vector = new Point(1, 0);
	    break;
	case DOWN:
	    vector = new Point(0, 1);
	    break;
	default:
	    vector = new Point(-1, 0);
	}
	return vector;
    }

}