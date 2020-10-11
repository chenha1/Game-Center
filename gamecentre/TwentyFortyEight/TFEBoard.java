package group_0522.csc207.gamecentre.TwentyFortyEight;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import group_0522.csc207.gamecentre.Common.Board;

public class TFEBoard extends Board<TFETile> {
    private int numRows;
    private int numCols;
    private TFETile[][] tiles;
    private TFETile[] allTiles;

    TFEBoard (int rows, int cols) {
        this.numRows = rows;
        this.numCols = cols;
        this.tiles = new TFETile[rows][cols];
        Random randomNum = new Random();
        int newApTile1 = randomNum.nextInt(numCols * numRows);
        int newApTile2 = randomNum.nextInt(numCols * numRows);
        for (int row = 0; row != rows; row++) {
            for (int col = 0; col != cols; col++) {
                if (row * numCols + col == newApTile1) { // not the tile with num
                    this.tiles[row][col] = new TFETile(2);
                }
                else if (row * numCols + col == newApTile2) {
                    this.tiles[row][col] = new TFETile(4);
                }
                else {
                    this.tiles[row][col] = new TFETile(0);
                }
            }
        }
    }

    TFEBoard (int rows, int cols, List<TFETile> tiles) {
        Iterator<TFETile> iterator = tiles.iterator();
        this.numRows = rows;
        this.numCols = cols;
        this.tiles = new TFETile[rows][cols];
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

    private void initializeTile(TFETile[] tiles, int size){
        for (int i=0; i < size; i++){
            tiles[i] = new TFETile(0);
        }
    }

    public int getNumCols() {
        return this.numCols;
    }

    public int getNumRows() {
        return this.numRows;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    @Override
    public TFETile getTile(int row, int col) {
        return tiles[row][col];
    }

    public void addRandTile(){
        int numEmpty = 0;
        for (int row = 0; row < numRows; row ++){
            for (int col = 0; col < numCols; col ++){
                if (tiles[row][col].getId() == 0){
                    numEmpty ++;
                }
            }
        }

        Random randNum = new Random();
        int newTileIndex = randNum.nextInt(numEmpty);
        numEmpty = 0;
        for (int row = 0; row < numRows; row++){
            for (int col = 0; col < numCols; col ++){
                if (tiles[row][col].getId() == 0){
                    if (numEmpty == newTileIndex){
                        int dice = randNum.nextInt(2);
                        if (dice == 0) {
                            tiles[row][col] = new TFETile(2);
                        }else{
                            tiles[row][col] = new TFETile(4);
                        }
                    }
                }
            }
        }

        setChanged();
        notifyObservers();
    }

    private TFETile[] moveTiles(TFETile[] tiles, int size){
        TFETile[] tilesAfter = new TFETile[size];
        initializeTile(tilesAfter, size);
        int count = 0;
        for (int i = 0; i < size; i++){
            if (tiles[i].getId() != 0){
                if ((tilesAfter[0].getId() != 0) && (tiles[i].getId() == tilesAfter[count - 1].getId())){
                    tilesAfter[count - 1] = new TFETile(tilesAfter[count].getId() * 2);
                }else {
                    tilesAfter[count] = new TFETile(tiles[i].getId());
                    count += 1;
                }
            }
        }
        return tilesAfter;
    }

    public void tilesDown() {
        for (int col = 0; col != numCols; col++){
            TFETile[] tilesAfter = moveTiles(tiles[col], numRows);
            int count = 0;
            for (int row = numRows-1; row >= 0; row--){
                tiles[col][row] = tilesAfter[count];
                count += 1;
            }
        }
        setChanged();
        notifyObservers();
    }

    public void tilesUp() { // slide up
        for (int col = 0; col != numCols; col++) {
            TFETile[] tilesAfter = moveTiles(tiles[col], numRows);
            int count = 0;
            for (int row = 0; row < numRows; row++){
                tiles[col][row] = tilesAfter[count];
                count += 1;
            }
        }
        setChanged();
        notifyObservers();
    }

    public void tilesLeft() { // slide towards left
        for (int row = 0; row != numCols; row++) {
            TFETile[] tilesAfter = moveTiles(tiles[row], numRows);
            int count = 0;
            for (int col = 0; col < numCols; col++){
                tiles[col][row] = tilesAfter[count];
                count += 1;
            }
        }
        setChanged();
        notifyObservers();
    }

    public void tilesRight() { // slide towards left
        for (int row = 0; row != numCols; row++) {
            TFETile[] tilesAfter = moveTiles(tiles[row], numRows);
            int count = 0;
            for (int col = numCols-1; col >= 0; col--){
                tiles[col][row] = tilesAfter[count];
                count += 1;
            }
        }
        setChanged();
        notifyObservers();
    }

}
