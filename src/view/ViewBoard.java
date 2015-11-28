package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

import javax.swing.JLayeredPane;

import controller.Tile;

/**
 * Controls the look of the board, where all tiles are placed, and how they are moved.
 * 
 * @author Robert Stuart
 */
class ViewBoard extends JLayeredPane {
    private static final long serialVersionUID = 7704761091317274700L;
    private static final double borderWidthRatio = 6.88; // b.c. LoLz
    private static final Integer GRIDLAYER = new Integer(1), TILELAYER = new Integer(2);
    private int numTilesX, numTilesY;
    private int tileBorderV, tileBorderH;
    private int boardWidth = 0, boardHeight = 0;

    /**
     * Constructs this board with the given dimensions.
     * 
     * @param numTilesX
     *            The number of horizontal tiles.
     * @param numTilesY
     *            The number of vertical tiles.
     * @param maxSize
     *            The maximum size that the board can be.
     */
    protected ViewBoard() {
	setBackground(new Color(187, 173, 160));
	setOpaque(true);
    }

    protected int setHorizontalConstraints(int numTilesX, int maxBoardWidth) {
	boardHeight = 0; // force user to call setVerticalConstraints after setHorizontalConstraints
	this.numTilesX = numTilesX;
	int prefTileBorderH = (int) (ViewTile.getPrefSize().width / borderWidthRatio);
	int prefWidth = (int) (numTilesX * ViewTile.getPrefSize().width + (numTilesX + 1) * prefTileBorderH);
	if (prefWidth > maxBoardWidth) {
	    tileBorderH = (int) (maxBoardWidth / ((borderWidthRatio + 1) * numTilesX + 1));
	    ViewTile.setActualWidth((int) (borderWidthRatio * tileBorderH));
	} else {
	    tileBorderH = prefTileBorderH;
	    ViewTile.setActualWidth(ViewTile.getPrefSize().width);
	}
	boardWidth = numTilesX * ViewTile.getActualWidth() + (numTilesX + 1) * tileBorderH;
	setPreferredSize(new Dimension(boardWidth, boardHeight));
	setMaximumSize(getPreferredSize());
	setMinimumSize(getPreferredSize());
	return boardWidth;
    }

    protected int setVerticalConstraints(int numTilesY, int maxBoardHeight) {
	if (boardWidth == 0) return 0; // force user to call setVerticalConstraints after setHorizontalConstraints
	this.numTilesY = numTilesY;
	int prefTileBorderV = (int) (ViewTile.getPrefSize().height / borderWidthRatio);
	int prefHeight = (int) (numTilesY * ViewTile.getPrefSize().height + (numTilesY + 1) * prefTileBorderV);
	if (prefHeight > maxBoardHeight) {
	    tileBorderV = (int) (maxBoardHeight / ((borderWidthRatio + 1) * numTilesY + 1));
	    ViewTile.setActualHeight((int) (borderWidthRatio * tileBorderV));
	} else {
	    tileBorderV = prefTileBorderV;
	    ViewTile.setActualHeight(ViewTile.getPrefSize().height);
	}
	boardHeight = numTilesY * ViewTile.getActualHeight() + (numTilesY + 1) * tileBorderV;
	setPreferredSize(new Dimension(boardWidth, boardHeight));
	setMaximumSize(getPreferredSize());
	setMinimumSize(getPreferredSize());
	return boardHeight;
    }

    protected boolean createGrid() {
	if (boardWidth == 0 || boardHeight == 0) return false;
	for (int x = 0; x < numTilesX; x++)
	    for (int y = 0; y < numTilesY; y++)
		add(new ViewTile(0, coord2Point(new Point(x, y))), GRIDLAYER);
	return true;
    }

    /**
     * Removes all tiles from this board, then re-makes it with the new dimensions.
     * 
     * @param numTilesX
     *            The number of horizontal tiles.
     * @param numTilesY
     *            The number of vertical tiles.
     */
    protected void resetBoard() {
	removeAll();
	boardHeight = boardWidth = 0;
    }

    /**
     * Moves the given list of tile actions; moving, adding, and removing them as dictated.
     * 
     * @param moves
     *            A list of tile actions for this board.
     */
    protected void moveTiles(List<Tile> moves) {
	for (Tile aMove : moves) {
	    if (aMove == null) continue;
	    if (aMove.isDeleted()) {
		if (aMove.getPrvLoc() == null) remove(getTileAt(aMove.getCurLoc())); // A tile that didn't move but was merged into
		else remove(getTileAt(aMove.getPrvLoc())); // A merged into tile
	    } else {
		if (aMove.getPrvLoc() == null) add(new ViewTile(aMove.getCurVal(), coord2Point(aMove.getCurLoc())), TILELAYER); // A newly added tile
		else { // A tile that has moved and potentially merged into another tile
		    ViewTile tile = getTileAt(aMove.getPrvLoc());
		    if (tile != null) {
			Point startPnt = coord2Point(aMove.getPrvLoc());
			Point endPnt = coord2Point(aMove.getCurLoc());
			tile.moveTileBy(endPnt.x - startPnt.x, endPnt.y - startPnt.y);
			tile.setTileVal(aMove.getCurVal());
		    }
		}
	    }
	    revalidate();
	}
    }

    private ViewTile getTileAt(Point pnt) {
	Component[] allComps = getComponentsInLayer(TILELAYER);
	Component potentialTile = getComponentAt(coord2Point(pnt));
	for (int i = 0; i < allComps.length; i++)
	    if (allComps[i].equals(potentialTile) && potentialTile instanceof ViewTile) return (ViewTile) potentialTile;
	return null;
    }

    /**
     * Adds the given list of tiles to this board.
     * 
     * @param newTiles
     *            A list of tiles to be added to the board.
     */
    protected void addTiles(List<Tile> newTiles) {
	for (Tile aTile : newTiles)
	    add(new ViewTile(aTile.getCurVal(), coord2Point(aTile.getCurLoc())), TILELAYER);
	repaint();
    }

    private Point coord2Point(Point coord) {
	int x = coord.x * (ViewTile.getActualWidth() + tileBorderH) + tileBorderH;
	int y = coord.y * (ViewTile.getActualHeight() + tileBorderV) + tileBorderV;
	return new Point(x, y);
    }

    /**
     * Repaints this component.
     * 
     * @param g
     *            The Graphics object to protect.
     */
    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	CustomPainter.paintComponentRounded(this, g, 6);
    }

}