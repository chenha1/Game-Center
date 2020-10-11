package group_0522.csc207.gamecentre.SlingdingTiles;

import java.util.List;
//The idea is from http://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
/**
 * A checker to make SlidingTileGame solvable
 */
public class SlidingTileGameChecker {
    /**
     * The list of tiles
     */
    private List<SlidingTile> tiles;
    /**
     * The complexity of the game
     */
    private int complexity;

    /**
     * Create a new game checker
     *
     * @param tiles the list of tiles that we need to check
     * @param complexity the complexity of the game
     */
    public SlidingTileGameChecker(List<SlidingTile> tiles, int complexity) {
        this.tiles = tiles;
        this.complexity = complexity;
    }

    /**
     * Finding the blank tile location
     *
     * @return the index of the blank tile
     */
    private int findBlank() {
        int index = 0;
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getId() == 0) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Counting the total number of inversions
     *
     * @return number of inversions
     */
    private int countInversions() {
        int inversion = 0;
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getId() != 0) {
                for (int j = i + 1; j < tiles.size(); j++) {
                    if (tiles.get(i).compareTo(tiles.get(j)) > 0) {
                        inversion += 1;
                    }
                }
            }
        }
        return inversion;
    }

    // (grid width odd) && (#inversions even) )  ||  ( (grid width even) && ((tile_0 on odd row from bottom) == (#inversions even)) )

    /**
     * Whether the game is solvable, the game is solvable if and only if
     * (grid width odd) && (#inversions even) )  ||  ( (grid width even) && ((tile_0 on odd row from bottom) == (#inversions even))
     *
     * @return isSolvable
     */
    private boolean isSolvable() {
        int inversion = this.countInversions();
        int totalNumber = this.tiles.size();
        int blankRow = complexity - this.findBlank() / complexity;
        if (totalNumber % 2 != 0) {
            return inversion % 2 == 0;
        } else if (blankRow % 2 == 0) {
            return inversion % 2 != 0;

        } else {
            return inversion % 2 == 0;
        }
    }

    /**
     * Return a solvable game by swapping
     *
     * @return a solvable list of SlidingTile
     */
    public List<SlidingTile> makeSolvable() {
        if (this.isSolvable()) {
            return this.tiles;
        }
        if (tiles.get(0).getId() != 0 && tiles.get(1).getId() != 0) {
            SlidingTile tem = tiles.remove(0);
            tiles.add(1, tem);
            return this.tiles;
        } else {
            SlidingTile tem = tiles.remove(tiles.size() - 1);
            tiles.add(tiles.size() - 1, tem);
            return this.tiles;
        }
    }
}

