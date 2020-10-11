package group_0522.csc207.gamecentre.SlingdingTiles;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import group_0522.csc207.gamecentre.Common.Board;

/**
 * The sliding tiles board.
 */
public class SlidingBoard extends Board<SlidingTile> {

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
    private SlidingTile[][] tiles;

    /**
     * A new board of tiles in row-major order.
     *
     * @param rows
     * @param cols
     * @param tiles
     */
    SlidingBoard(int rows, int cols, List<SlidingTile> tiles) {
        Iterator<SlidingTile> iterator = tiles.iterator();
        this.numRows = rows;
        this.numCols = cols;
        this.tiles = new SlidingTile[rows][cols];
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
    public SlidingTile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    public void swapTiles(int row1, int col1, int row2, int col2) {
        SlidingTile onHold = this.tiles[row1][col1];
        this.tiles[row1][col1] = this.tiles[row2][col2];
        this.tiles[row2][col2] = onHold;

        setChanged();
        notifyObservers();
    }

    /**
     * Returns an iterator over elements of type {@code SlidingTile}.
     *
     * @return an Iterator.
     */
    @NonNull
    @Override
    public Iterator<SlidingTile> iterator() {
        return new SlidingBoardIterator();
    }

    @Override
    public String toString() {
        return "SlidingBoard{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    /**
     * Return the number of rows
     *
     * @return numRows
     */
    @Override
    public int getNumRows() {
        return this.numRows;
    }

    /**
     * Return the number of cols
     *
     * @return numCols
     */
    @Override
    public int getNumCols() {
        return this.numCols;
    }


    /**
     * An iterator over elements of SlidingTile.
     */
    private class SlidingBoardIterator implements Iterator<SlidingTile> {
        /**
         * The current iterating position.
         */
        private int position = 0;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {

                return (position < SlidingBoard.this.numTiles());
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public SlidingTile next() {
            if (!(this.hasNext())) {
                throw new NoSuchElementException();

            }
            int row = position / numRows;
            int col = position % numRows;
            this.position += 1;
            return SlidingBoard.this.getTile(row, col);
        }
    }
}
