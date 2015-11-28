package controller;

import java.util.LinkedList;
import java.util.List;

import model.Model;
import model.ModelInterface;
import view.BasicGUI;
import view.ViewInterface;

/**
 * The main component of the 2048 Game, this communicates user actions to the model then updates the view based on any changes/information from the model.
 * 
 * @author Robert Stuart
 */
public class Controller implements ControllerInterface {
    private ViewInterface view;
    private ModelInterface model;
    private List<Tile> moves;
    private boolean alreadyWon = false;

    /**
     * The method by which the game of 2048 is started.
     * 
     * @param args
     *            Input arguments (not used).
     */
    public static void main(String[] args) {
	new Controller();
    }

    /**
     * Instantiates the model and view.
     */
    public Controller() {
	moves = new LinkedList<Tile>();
	model = new Model();
	moves = model.startGame();
	view = new BasicGUI(model.getWidth(), model.getHeight(), this);
	view.addTiles(moves);
	view.showScore(model.getScore(), model.getHighScore());
    }

    @Override
    public void restartGame(int newWidth, int newHeight) {
	alreadyWon = false;
	moves.clear();
	moves = model.restartGame(newWidth, newHeight);
	view.resetBoard(model.getWidth(), model.getHeight());
	view.addTiles(moves);
	view.showScore(model.getScore(), model.getHighScore());
    }

    @Override
    public void makeMove(Direction d) {
	moves.clear();
	moves = model.makeMove(d);
	if (moves.isEmpty()) {
	    if (model.isGameOver()) view.showLoss();
	} else {
	    view.moveTiles(moves);
	    view.showScore(model.getScore(), model.getHighScore());
	    if (model.isGameWon() && !alreadyWon) {
		alreadyWon = true;
		view.showWin();
	    } else if (model.isGameOver()) view.showLoss();
	}
    }

}