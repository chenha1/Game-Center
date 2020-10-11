package group_0522.csc207.gamecentre.Common;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Observable;


/**
 * A board with Tile T
 *
 * @param <T> the particular tiles that are on the board
 */
public abstract class Board<T extends Tile> extends Observable implements Serializable, Iterable<T> {
    /**
     * Return the tiles at [row][col]
     *
     * @param row the chosen row
     * @param col the chosen col
     * @return tiles at [row][col]
     */
    public abstract T getTile(int row, int col);

    /**
     * Return the number of rows
     *
     * @return numRows
     */
    protected abstract int getNumRows();

    /**
     * Return the number of cols
     *
     * @return numCols
     */
    public abstract int getNumCols();

    /**
     * Return the total number of tiles
     *
     * @return totalNumTiles
     */
    public abstract int numTiles();

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return new BoardIterator();
    }


    /**
     * An iterator over elements of Tiles.
     */
    private class BoardIterator implements Iterator<T> {
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

            return (position < Board.this.numTiles());
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public T next() {
            if (!(this.hasNext())) {
                throw new NoSuchElementException();

            }
            int row = position / Board.this.getNumRows();
            int col = position % Board.this.getNumRows();
            this.position += 1;
            return Board.this.getTile(row, col);
        }
    }
}
