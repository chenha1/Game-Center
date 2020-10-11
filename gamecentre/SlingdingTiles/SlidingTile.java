package group_0522.csc207.gamecentre.SlingdingTiles;

import android.support.annotation.NonNull;

import java.lang.reflect.Field;

import group_0522.csc207.gamecentre.Common.Tile;
import group_0522.csc207.gamecentre.R;

/**
 * A SlidingTile in a sliding tiles puzzle.
 */
public class SlidingTile extends Tile implements Comparable<SlidingTile> {

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
    public SlidingTile(int id, int background) {
        this.id = id;
        this.background = background;
    }

    /**
     * A tile with a background id; Dynamically lookup and set the background
     *
     * @param id the tile id
     */
    public SlidingTile(int id) {
        this.id = id;
        String backgroundName = String.format("tile_%s",id);
       try{
           Class res = R.drawable.class;
           Field field= res.getField(backgroundName);
           background = field.getInt(field);
       }
       catch(Exception e){
           e.printStackTrace();
        }
    }

    @Override
    public int compareTo(@NonNull SlidingTile o) {
        if (this.id == 0) {
            return -1;
        } else if (o.getId() == 0) {
            return 1;
        } else {
            return o.getId() - this.id;
        }
    }

}
