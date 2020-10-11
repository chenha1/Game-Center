package group_0522.csc207.gamecentre.Common;

import java.io.Serializable;

/**
 * A tile on the board
 */
public abstract class Tile implements Serializable {
    /**
     * Return the background Id
     *
     * @return background Id
     */
    public abstract int getBackground();

    /**
     * Return the id
     *
     * @return id
     */
    public abstract int getId();
}
