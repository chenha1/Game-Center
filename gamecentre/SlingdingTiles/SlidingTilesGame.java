package group_0522.csc207.gamecentre.SlingdingTiles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import group_0522.csc207.gamecentre.Common.Game;
import group_0522.csc207.gamecentre.Common.Tile;

/**
 * A new SlidingTilesGame
 */
public class SlidingTilesGame extends Game {
    /**
     * The name of this game
     */
    public static final String GAME_NAME = "Sliding Tiles";
    /**
     * The default undo steps
     */
    public static final int DEFAULT_UNDO_STEPS = 3;
    /**
     * The SlidingBoard class for this game
     */
    private SlidingBoard slidingBoard;
    /**
     * A List to store steps
     */
    private ArrayList<Integer> steps;
    /**
     * The undoSteps left
     */
    private int undoSteps;
    /**
     * The max undo step;
     */
    private int maxSteps;

    /**
     * Return the current slidingBoard.
     */
    public SlidingBoard getSlidingBoard() {
        return slidingBoard;
    }


    /**
     * Create a new game based on the given tiles, complexity, id , userName and maxSteps
     *
     * @param tiles      the list of tiles in specific position
     * @param complexity the difficulty of the game
     * @param id         the unique id
     * @param userName   the user who create the game
     * @param maxSteps   the maximum undo steps
     */
    public SlidingTilesGame(List<SlidingTile> tiles, int[] complexity, int id, String userName, int maxSteps) {
        super(userName, complexity[0], id, new Date());
        this.slidingBoard = new SlidingBoard(complexity[0], complexity[1], tiles);
        this.steps = new ArrayList<>();
        if (maxSteps == -1) {
            this.maxSteps = Integer.MAX_VALUE;
        } else {
            this.maxSteps = maxSteps;
        }
    }

    /**
     * Create a solvable new game based on the given complexity, id, userName and maxSteps
     *
     * @param complexity the difficulty of the game
     * @param id         the unique id
     * @param userName   the user who create the game
     * @param maxSteps   the maximum undo steps
     */
    public SlidingTilesGame(int[] complexity, int id, String userName, int maxSteps) {
        super(userName, complexity[0], id,new Date());
        List<SlidingTile> tiles = new ArrayList<>();
        final int numTiles = complexity[0] * complexity[1];
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new SlidingTile(tileNum));
        }

        Collections.shuffle(tiles);
        //Make a solvable game
        SlidingTileGameChecker checker = new SlidingTileGameChecker(tiles, complexity[0]);
        tiles = checker.makeSolvable();
        this.slidingBoard = new SlidingBoard(complexity[0], complexity[1], tiles);
        this.steps = new ArrayList<>();
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
        boolean solved = true;
        Iterator<SlidingTile> btr = this.slidingBoard.iterator();
        if (btr.hasNext()) {
            SlidingTile current = btr.next();

            while (btr.hasNext()) {
                SlidingTile temp = btr.next();
                if (current.compareTo(temp) < 0) {
                    solved = false;
                }
                current = temp;
            }
        }
        return solved;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    public boolean isValidTap(int position) {

        int row = position / slidingBoard.getNumRows();
        int col = position % slidingBoard.getNumRows();
        int blankId = 0;
        // Are any of the 4 the tile_0 tile?
        Tile above = row == 0 ? null : slidingBoard.getTile(row - 1, col);
        Tile below = row == slidingBoard.getNumRows() - 1 ? null : slidingBoard.getTile(row + 1, col);
        Tile left = col == 0 ? null : slidingBoard.getTile(row, col - 1);
        Tile right = col == slidingBoard.getNumRows() - 1 ? null : slidingBoard.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Return whether the tile at (row, col) has the same id as the input.
     *
     * @return whether the tile at (row,col) has the same id as the input
     */
    private boolean equalId(int row, int col, int id) {
        return (this.slidingBoard.getTile(row, col).getId() == id);
    }


    /**
     * Process a undo, undo one step. Throw IndexOutOfBoundsException if the steps exceed the maximum.
     *
     * @throws IndexOutOfBoundsException
     */
    public void undo() throws IndexOutOfBoundsException {
        if (undoSteps > 0) {
            undoSteps -= 1;
            int position = steps.remove(steps.size() - 1);
            touchMove(position,false);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }


    /**
     * Process a touch at position in the slidingBoard, swapping tiles as appropriate. Add the step to steps if and only if
     * should add is true
     *
     * @param position the position
     * @param shouldAdd whether to record this move
     */
    public void touchMove(int position, boolean shouldAdd) {

        int row = position / slidingBoard.getNumRows();
        int col = position % slidingBoard.getNumRows();
        int blankId = 0;
        if (this.undoSteps < maxSteps) {
            undoSteps += 1;
        }

        // figure out when to call slidingBoard.swapTiles. Specifically, if any of the neighbouring
        // tiles is the tile_0 tile, swap by calling SlidingBoard's swap method.
        if (row - 1 >= 0) {
            if (equalId(row - 1, col, blankId)) {
                if(shouldAdd) {
                    steps.add((row - 1) * slidingBoard.getNumRows() + col);
                }
                slidingBoard.swapTiles(row, col, row - 1, col);
            }
        }
        if (col - 1 >= 0) {
            if (equalId(row, col - 1, blankId)) {
                if(shouldAdd) {
                    steps.add((row) * slidingBoard.getNumRows() + col - 1);
                }
                slidingBoard.swapTiles(row, col, row, col - 1);
            }
        }
        if (row + 1 <= slidingBoard.getNumRows() - 1) {
            if (equalId(row + 1, col, blankId)) {
                if(shouldAdd) {
                    steps.add((row + 1) * slidingBoard.getNumRows() + col);
                }
                slidingBoard.swapTiles(row, col, row + 1, col);
            }
        }
        if (col + 1 <= slidingBoard.getNumCols() - 1) {
            if (equalId(row, col + 1, blankId)) {
                if(shouldAdd) {
                    steps.add((row) * slidingBoard.getNumRows() + col + 1);
                }
                slidingBoard.swapTiles(row, col, row, col + 1);
            }
        }

    }

    /**
     * Return the current steps
     *
     * @return current score
     */
    public Integer getScore() {
        return steps.size();
    }


    /**
     * Return the undoSteps remaining
     *
     * @return undoSteps remaining
     */
    public int getUndoSteps() {
        return this.undoSteps;
    }

    /**
     * Return the gameName
     * @return the name of the game
     */
    public String getGameName() {
        return GAME_NAME;
    }

}