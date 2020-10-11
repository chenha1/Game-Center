package group_0522.csc207.gamecentre.MineSweeper;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import group_0522.csc207.gamecentre.Common.Game;

/**
 * A MineSweeperGame
 */
public class MineSweeperGame extends Game {
    /**
     * Game name
     */
    public static final String GAME_NAME = "MineSweeper";
    /**
     * Default num of mine
     */
    public static final int DEFAULT_NUM_MINE = 3;
    /**
     * whether the game is still clickable.
     */
    private boolean isClickable;
    /**
     * The MineSweeperBoard
     */
    private MineSweeperBoard mineSweeperBoard;
    /**
     * whether the user performed the first click
     */
    private boolean isFirstClicked;
    /**
     * whether the mines are set for the game
     */
    private boolean isMineSet;
    /**
     * the current score measure in time
     */
    private int score;
    /**
     * Created date
     */
    private Date date;
    /**
     * the total number of mine
     */
    private int numberOfMine;
    /**
     * the current game state
     */
    private String gameState;


    /**
     * create a new Game
     *
     * @param complexity
     * @param id
     * @param userName
     * @param numberOfMine
     */
    public MineSweeperGame(int[] complexity, int id, String userName, int numberOfMine) {
        super(userName, numberOfMine, id, new Date());
        List<MineSweeperTile> tiles = new ArrayList<>();
        int numTiles = complexity[0] * complexity[1];
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new MineSweeperTile(tileNum));
        }

        this.mineSweeperBoard = new MineSweeperBoard(complexity[0], complexity[1], tiles);
        this.numberOfMine = numberOfMine;
        this.isFirstClicked = false;
        this.isMineSet = false;
        this.isClickable = true;
        this.score = 0;
        this.gameState = "";

        this.date = new Date();
    }

    /**
     * set the score
     *
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public Integer getScore() {
        return this.score;
    }

    /**
     * whether a touch move is valid at position
     *
     * @param position
     * @return whether the move is valid
     */
    public boolean isValidMove(int position) {
        int row = position / mineSweeperBoard.getNumRows();
        int col = position % mineSweeperBoard.getNumRows();
        return (mineSweeperBoard.getTile(row, col).isCovered() && !mineSweeperBoard.getTile(row, col).HasFlag());
    }

    /**
     * perform a touch move at position
     *
     * @param position
     */
    public void touchMove(int position) {
        int row = position / mineSweeperBoard.getNumRows();
        int col = position % mineSweeperBoard.getNumRows();
        this.isFirstClicked = true;
        mineSweeperBoard.openTile(row, col);
    }

    /**
     * pauseGame
     */
    public void pauseGame() {
        isFirstClicked = false;
    }

    /**
     * whether we should plant mine after the click
     *
     * @return should we plant mine
     */
    public boolean isSettingClick() {
        return (!this.isMineSet);
    }

    /**
     * whether the click is the first click after pausing
     *
     * @return isFirstClicked
     */
    public boolean isFirstClicked() {
        return this.isFirstClicked;
    }


    /**
     * planting Mine and perform a touch move at position
     *
     * @param position
     */
    public void firstSettingTouchMove(int position) {
        int row = position / mineSweeperBoard.getNumRows();
        int col = position % mineSweeperBoard.getNumRows();
        this.isFirstClicked = true;
        this.isMineSet = true;
        mineSweeperBoard.setMines(row, col, numberOfMine);
        mineSweeperBoard.openTile(row, col);
    }

    /**
     * whether the longPress is valid at position
     *
     * @param position
     * @return whether the longPress is valid
     */
    public boolean isValidLongPress(int position) {
        int row = position / mineSweeperBoard.getNumRows();
        int col = position % mineSweeperBoard.getNumRows();
        if (!isMineSet) {
            return false;
        }
        return (mineSweeperBoard.getTile(row, col).isCovered() || mineSweeperBoard.getTile(row, col).HasFlag());
    }

    /**
     * perform a longPress at position
     *
     * @param position
     */
    public void longPressMove(int position) {
        int row = position / mineSweeperBoard.getNumRows();
        int col = position % mineSweeperBoard.getNumRows();
        this.isFirstClicked = true;
        mineSweeperBoard.setFlag(row, col);
    }

    /**
     * whether the game is win
     *
     * @return isGameWin
     */
    public boolean isGameWin() {
        return mineSweeperBoard.isWin();
    }

    /**
     * whether the game is lost
     *
     * @param position
     * @return does the touch position have mine
     */
    public boolean isGameLost(int position) {
        int row = position / mineSweeperBoard.getNumRows();
        int col = position % mineSweeperBoard.getNumRows();
        return mineSweeperBoard.getTile(row, col).hasMine();

    }

    /**
     * set the game to be finished
     */
    public void gameFinished() {
        this.isClickable = false;
        mineSweeperBoard.showAll();

    }

    @Override
    public String getGameName() {
        return MineSweeperGame.GAME_NAME;
    }

    /**
     * return the MineSweeperBoard
     *
     * @return MineSweeperBoard
     */
    public MineSweeperBoard getMineSweeperBoard() {
        return this.mineSweeperBoard;
    }

    /**
     * whether the game is clickable
     *
     * @return isClickable
     */
    public boolean isClickable() {
        return this.isClickable;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        return String.format("ID: %s \n %s", this.getId(), strDate);
    }

    /**
     * return the gameState
     * @return gameState
     */
    public String getGameState(){
        return this.gameState;
    }

    /**
     * setting the game state
     * @param gameState
     */
    public void setGameState(String gameState){
        this.gameState = gameState;
    }

}
