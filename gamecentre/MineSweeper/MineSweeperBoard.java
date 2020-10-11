package group_0522.csc207.gamecentre.MineSweeper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import group_0522.csc207.gamecentre.Common.Board;

public class MineSweeperBoard extends Board<MineSweeperTile> {

    /**
     * The number of rows.
     */
    private int numRows;

    /**
     * The number of rows.
     */
    private int numCols;

    /**
     * The tiles on the board in row-major order.
     */
    private MineSweeperTile[][] tiles;

    /**
     * A new board of tiles in row-major order.
     *
     * @param rows
     * @param cols
     * @param tiles
     */
    MineSweeperBoard(int rows, int cols, List<MineSweeperTile> tiles) {
        Iterator<MineSweeperTile> iterator = tiles.iterator();
        this.numRows = rows;
        this.numCols = cols;
        this.tiles = new MineSweeperTile[rows][cols];
        for (int row = 0; row != rows; row++) {
            for (int col = 0; col != cols; col++) {
                this.tiles[row][col] = iterator.next();
            }
        }
    }


    /**
     * Return the number of tiles on the board.
     *
     * @return the number of tiles on the board
     */
    public int numTiles() {
        return (this.numRows * this.numCols);
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    @Override
    public MineSweeperTile getTile(int row, int col) {
        return tiles[row][col];
    }


    @Override
    public String toString() {
        return "MineSweeperBoard{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    /**
     * get the number of rows
     *
     * @return numRows
     */
    @Override
    public int getNumRows() {
        return this.numRows;
    }

    /**
     * get the number of cols
     *
     * @return numCols
     */
    @Override
    public int getNumCols() {
        return this.numCols;
    }

    /**
     * Precondition: tile at [row][col] is covered
     * set flag for tile at [row][col]
     *
     * @param row
     * @param col
     */
    public void setFlag(int row, int col) {
        tiles[row][col].switchFlag();
        setChanged();
        notifyObservers();
    }

    /**
     * ripple open tile at [row][col]
     *
     * @param clickedRow
     * @param clickedCol
     */
    public void openTile(int clickedRow, int clickedCol) {
        // don't open flagged or mined.
        if (tiles[clickedRow][clickedCol].hasMine() ||
                tiles[clickedRow][clickedCol].HasFlag()) {
            return;
        }

        tiles[clickedRow][clickedCol].openTile();
        int numOfMine = tiles[clickedRow][clickedCol].getNumberOfMinesNearBy();
        setChanged();
        notifyObservers();

        // if clicked block have nearby mines then don't open further
        if (numOfMine != 0) {
            return;
        }

        // open next 3 rows and 3 columns recursively
        for (int row = -1; row < 2; row++) {
            for (int column = -1; column < 2; column++) {
                // check all the above checked conditions
                // if met then open subsequent blocks
                if ((clickedRow + row >= 0) && (clickedCol + column >= 0)
                        && (clickedRow + row < this.numRows)
                        && (clickedCol + column < this.numCols) &&
                        tiles[clickedRow + row][clickedCol + column].isCovered()) {
                    openTile(clickedRow + row, clickedCol + column);
                }
            }
        }
    }

    /**
     * counting the number of mines nearby for tile at [row][col]
     *
     * @param row
     * @param col
     * @return number of mines nearby
     */
    private int nearbyMineCount(int row, int col) {
        int count = 0;
        MineSweeperTile current;
        MineSweeperTile tile = tiles[row][col];
        if (tile.hasMine()) {
            return 0;
        } else {
            if (row > 0) {
                current = tiles[row - 1][col];
                count = current.hasMine() ? (count + 1) : count;
                if (col > 0) {
                    current = tiles[row - 1][col - 1];
                    count = current.hasMine() ? (count + 1) : count;
                }
                if (col + 1 < numCols) {
                    current = tiles[row - 1][col + 1];
                    count = current.hasMine() ? (count + 1) : count;
                }
            }
            if (row + 1 < numRows) {
                current = tiles[row + 1][col];
                count = current.hasMine() ? (count + 1) : count;
                if (col > 0) {
                    current = tiles[row + 1][col - 1];
                    count = current.hasMine() ? (count + 1) : count;
                }
                if (col + 1 < numCols) {
                    current = tiles[row + 1][col + 1];
                    count = current.hasMine() ? (count + 1) : count;
                }
            }
            if (col > 0) {
                current = tiles[row][col - 1];
                count = current.hasMine() ? (count + 1) : count;
            }
            if (col + 1 < numRows) {
                current = tiles[row][col + 1];
                count = current.hasMine() ? (count + 1) : count;
            }
        }
        return count;
    }

    /**
     * set the number of mines for every tiles on the board
     */
    public void setMineCount() {
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                int nearbyMine = this.nearbyMineCount(row, col);
                this.tiles[row][col].setNumberOfSurroundingMines(nearbyMine);
            }
        }

    }

    /**
     * setting the mines for the game, and the tile with [row][col] will not have mine
     *
     * @param row
     * @param col
     * @param numberOfMine
     */
    public void setMines(int row, int col, int numberOfMine) {
        // set mines excluding the location where user clicked
        Random rand = new Random();
        int mineRow, mineColumn;

        for (int numMine = 0; numMine < numberOfMine; numMine++) {
            mineRow = rand.nextInt(numRows);
            mineColumn = rand.nextInt(numCols);
            if ((mineRow != row) || (mineColumn != col)) {
                if (tiles[mineRow][mineColumn].hasMine()) {
                    numMine--; // mine is already there, don't repeat for same block
                }
                // plant mine at this location
                tiles[mineRow][mineColumn].plantMine();
            }
            // exclude the user clicked location
            else {
                numMine--;
            }
        }
        setMineCount();
    }

    /**
     * whether the board satisfied winning conditions
     *
     * @return isWin
     */
    public boolean isWin() {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numCols; column++) {
                if (!tiles[row][column].hasMine() && tiles[row][column].isCovered()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * uncover all the tiles.
     */
    public void showAll() {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numCols; column++) {

                if (tiles[row][column].isCovered()) {
                    tiles[row][column].openTile();
                }
            }
            setChanged();
            notifyObservers();
        }


    }
}
