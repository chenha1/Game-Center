package group_0522.csc207.gamecentre.MineSweeper;


import android.graphics.Color;

import group_0522.csc207.gamecentre.Common.Tile;
import group_0522.csc207.gamecentre.R;

/**
 * A MineSweeperTile in a MineSweeper Game.
 */
public class MineSweeperTile extends Tile {
    /**
     * The front size for each tile.
     */
    public final static int TEXT_SIZE = 40;
    /**
     * The background id to find the tile image.
     */
    private int background;

    /**
     * The unique id.
     */
    private int id;
    /**
     * whether the tile is covered
     */
    private boolean isCovered;
    /**
     * whether the tile has a mine underneath
     */
    private boolean hasMine;
    /**
     * whether the block is marked as flagged
     */
    private boolean hasFlag;
    /**
     * the number of mine in nearby tiles
     */
    private int numberOfMinesNearBy;
    /**
     * the text that correspond button should display
     */
    private String text;
    /**
     * the color for the text
     */
    private int color;

    /**
     * A MinSweeperTile with id and other default value.
     *
     * @param id the id
     */
    public MineSweeperTile(int id) {
        this.id = id;
        isCovered = true;
        hasMine = false;
        hasFlag = false;
        numberOfMinesNearBy = 0;
        text = "";
        color = Color.BLACK;
        this.background = R.drawable.block;
    }

    /**
     * return the text for the correspond button
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Return the background id.
     *
     * @return the background id
     */
    public int getBackground() {
        return background;
    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }


    /**
     * set the numberOfMinesNearBy
     *
     * @param numberOfMinesInSurrounding
     */
    public void setNumberOfSurroundingMines(int numberOfMinesInSurrounding) {
        this.numberOfMinesNearBy = numberOfMinesInSurrounding;
    }


    /**
     * set mine icon for the tiles
     */
    private void setMineIcon() {
        this.background = R.drawable.mine;
    }

    /**
     * Precondition: the tiles is covered.
     * set flag icon for the tiles if it is not flagged otherwise undo it
     */
    public void switchFlag() {
        if (!this.hasFlag) {
            this.hasFlag = true;
            this.background = R.drawable.flag;
        } else {
            this.hasFlag = false;
            this.background = R.drawable.block;
        }
    }


    /**
     * open this tile
     */
    public void openTile() {
        this.background = R.drawable.tapedblock;
        isCovered = false;

        // if it has mine, we should set mine icon. Moreover if it has flag, we should mark the Mine wile red X
        if (hasMine()) {
            setMineIcon();
            if (hasFlag) {
                this.text = "X";
                this.color = Color.RED;
            }
        }
        // if it doesn't has mine we should update the text with the nearby mine count
        else {
            if (numberOfMinesNearBy != 0) {
                this.text = String.format("%s", numberOfMinesNearBy);
            }
        }
    }

    /**
     * return the color
     *
     * @return color
     */
    public int getColor() {
        return this.color;
    }

    /**
     * plant a mine under the tile
     */
    public void plantMine() {
        hasMine = true;
    }

    /**
     * whether the tile is covered
     *
     * @return isCovered
     */
    public boolean isCovered() {
        return isCovered;
    }


    /**
     * whether the tile has mine
     *
     * @return hasMine
     */
    public boolean hasMine() {
        return hasMine;
    }

    /**
     * return the number of mines nearby
     *
     * @return number of mines nearby
     */
    public int getNumberOfMinesNearBy() {
        return numberOfMinesNearBy;
    }

    /**
     * whether the tile has benn marked flag
     *
     * @return
     */
    public boolean HasFlag() {
        return hasFlag;
    }


}

