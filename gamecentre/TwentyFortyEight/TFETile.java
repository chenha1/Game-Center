package group_0522.csc207.gamecentre.TwentyFortyEight;

import java.lang.reflect.Field;

import group_0522.csc207.gamecentre.Common.Tile;
import group_0522.csc207.gamecentre.R;

public class TFETile extends Tile {
    /**
     * The background id to find the tile image.
     */
    private int background;

    /**
     * The unique id.
     */
    private int id;

    /**
     * Return the background id.
     *
     * @return the background id
     */
    @Override
    public int getBackground() {
        return background;
    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * A SlidingTile with id and background. The background may not have a corresponding image.
     *
     * @param id         the id
     * @param background the background
     */
    public TFETile(int id, int background) {
        this.id = id;
        this.background = background;
    }

    /**
     * A tile with a background id; look up and set the id.
     *
     * @param id
     */
    public TFETile(int id) { // id is the number on the tile (ie. size)
        this.id = id;
        // This looks so ugly. add: More ugly T_T
        String backgroundName = String.format("tile%s",id);
        try{
            Class res = R.drawable.class;
            Field field= res.getField(backgroundName);
            background = field.getInt(field);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
