package model;

import java.util.List;

import controller.Direction;
import controller.Tile;

/**
 * An interface describing tasks that the model shall be responsible for.
 * 
 * @author Robert Stuart
 */
public interface ModelInterface {

    public List<Tile> startGame();

    /**
     * Starts a new game with the given dimensions.
     * 
     * @param width
     *            The width of the board to be made.
     * @param height
     *            The height of the board to be made.
     * @return A list of tiles that were added to the board.
     * 
     */
    public List<Tile> startGame(int width, int height);

    /**
     * Ends the current game and starts a new one with the given dimensions.
     * 
     * @param width
     *            The width of the board to be made.
     * @param height
     *            The height of the board to be made.
     * @return A list of tiles that were added to the new board.
     */
    public List<Tile> restartGame(int width, int height);

    /**
     * Shifts all tiles on the board in the given direction.
     * 
     * @param d
     *            The direction to shift the tiles on the board.
     * @return The past and present positions and values of all tiles that were affected by the move as well as the tile that was added to the board (only added after a successful move).
     */
    public List<Tile> makeMove(Direction d);

    /**
     * Returns the score of this current game.
     * 
     * @return The score of this current game.
     */
    public int getScore();

    /**
     * Returns the highScore of this game.
     * 
     * @return The highScore of this game.
     */
    public int getHighScore();

    /**
     * Indicates whether this game has been won or not. Note that to win the game the '2048' tile must be created, however the user can continue to play after that point.
     * 
     * @return true If this game has been won, otherwise false.
     */
    public boolean isGameWon();

    /**
     * Indicates whether this game is over. For a game to be over, the board must be completely filled and no moves are available.
     * 
     * @return true If this game is over, otherwise false.
     */
    public boolean isGameOver();

    public int getWidth();

    public int getHeight();

    // public void loadGame(File file) throws Exception;
    // public void saveGame(File file) throws Exception;

}