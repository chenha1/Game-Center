package group_0522.csc207.gamecentre.TwentyFortyEight;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import group_0522.csc207.gamecentre.Common.Game;

public class TFEGame extends Game {
    public static final String GAME_NAME= "2048";

    /**
     * The SlidingBoard
     */
    private TFEBoard tfeBoard;

    public static final int DEFAULT_UNDO_STEPS = 3;

    /**
     * Created date
     */
    private Date date;
    /**
     * the userName
     */
    private String userName;
    /**
     * the id
     */
    private int id;
    /**
     * list to store steps
     */
    private ArrayList<TFEBoard> steps;
    /**
     * the difficulty
     */
    private int difficulty;
    /**
     * the undoSteps left
     */
    private int undoSteps = 0;
    /**
     * the max undo step;
     */
    private int maxSteps;

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    /**
     * Return the current slidingBoard.
     */
    public TFEBoard getSlidingBoard() {
        return tfeBoard;
    }

    public TFEGame(List<TFETile> tiles, int[] complexity, int id, String userName, int maxSteps){
        super(userName,complexity[0],id, new Date());
        this.tfeBoard = new TFEBoard(complexity[0], complexity[1], tiles);
        this.id = id;
        this.userName = userName;
        this.date = new Date();
        this.steps = new ArrayList<>();
        this.difficulty = complexity[0];
        if (maxSteps == -1) {
            this.maxSteps = Integer.MAX_VALUE;
        } else {
            this.maxSteps = maxSteps;
        }
    }
    /**
     * Manage a new shuffled slidingBoard.
     */
    public TFEGame(int[] complexity, int id, String userName, int maxSteps) {
        super(userName,complexity[0],id, new Date());
        this.tfeBoard = new TFEBoard(complexity[0], complexity[1]);
        this.id = id;
        this.userName = userName;
        this.date = new Date();
        this.steps = new ArrayList<>();
        this.difficulty = complexity[0];
        if (maxSteps == -1) {
            this.maxSteps = Integer.MAX_VALUE;
        } else {
            this.maxSteps = maxSteps;
        }
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    public boolean puzzleSolved() {
        boolean solved = false;
        Iterator<TFETile> btr = this.tfeBoard.iterator();
        TFETile current = btr.next();
        while (btr.hasNext()) {
            if (current.getId() == 2048) {
                solved = true;
            }
        }
        return solved;
    }


    /**
     * Return whether the tile at (row, col) has the same id as the input.
     *
     * @return whether the tile at (row,col) has the same id as the input
     */
    private boolean equalId(int row, int col, int id) {
        return (this.tfeBoard.getTile(row, col).getId() == id);
    }


    /**
     * Process a undo, undo one step. Throw IndexOutOfBoundsException if the steps exceed the maximum.
     *
     * @throws IndexOutOfBoundsException
     */
    public void undo() throws IndexOutOfBoundsException {
        if (undoSteps > 0) {
            undoSteps -= 1;
            tfeBoard = steps.remove(steps.size() - 1);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Process a touch at position in the slidingBoard, swapping tiles as appropriate.
     *
     *
     */
    public void swipe(int direction) { // need to fix the undo part
        if (this.undoSteps < maxSteps) {
            undoSteps += 1;
        }
        switch (direction){
            case 1: // swipe up
                tfeBoard.tilesUp();
                //tfeBoard.addRandTile();
                break;
            case 2: // swipe down
                tfeBoard.tilesDown();
                //tfeBoard.addRandTile();
                break;
            case 3: // swipe left
                tfeBoard.tilesLeft();
                //tfeBoard.addRandTile();
                break;
            case 4: // swipe right
                tfeBoard.tilesRight();
                //tfeBoard.addRandTile();
                break;
        }

    }


    /**
     * return the current steps
     *
     * @return steps
     */
    public Integer getScore() {
        int sum = 0;
        for (int row = 0; row < tfeBoard.getNumRows(); row++){
            for (int col = 0; col < tfeBoard.getNumCols(); col++){
                sum += tfeBoard.getTile(row, col).getId();
            }
        }
        return sum;
    }

    /**
     * return the unique Id associated with the slidingTilesGame
     *
     * @return Id
     */
    public int getId() {
        return this.id;
    }




    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        return String.format("ID: %s \n %s", this.id, strDate);
    }

    /**
     * getter for undoSteps
     *
     * @return undoSteps
     */
    public int getUndoSteps() {
        return this.undoSteps;
    }

    public String getGameName() {
        return GAME_NAME;
    }

}
