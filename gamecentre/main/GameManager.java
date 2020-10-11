package group_0522.csc207.gamecentre.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import group_0522.csc207.gamecentre.Common.Game;

/**
 * The GameManager to manage Games
 */
public class GameManager extends Observable implements Serializable {
    /**
     * Games that are being managed, the key is gameName and
     * the value is the list stored all the games with the same gameName
     */
    private Map<String, List<Game>> games;
    /**
     * the id to assign to each games
     */
    private int id;

    /**
     * Create a new GameManager
     */
    public GameManager() {
        this.games = new HashMap<>();
        this.id = 0;
    }

    /**
     * add the s Game and also update the id,
     * if the size of the list for the input Game exceed the maximum, throw IndexOutOfBoundsException
     *
     * @param game the game being add to the GameManager
     * @throws IndexOutOfBoundsException
     */
    public void addGame(Game game) throws IndexOutOfBoundsException {
        String gameName = game.getGameName();
        ArrayList<Game> gameArrayList = new ArrayList<>();
        List<Game> gameList = games.getOrDefault(gameName, gameArrayList);
        if (gameList.size() >= GameCentre.MAX_DISPLAY) {
            throw new IndexOutOfBoundsException();
        } else {
            gameList.add(game);
            this.games.put(gameName, gameList);
            this.id += 1;
        }
    }

    /**
     * Return the current id
     *
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * Precondition: the Game with the id and gameName exist
     * Remove the Game with the given id and given gameName
     *
     * @param gameName the game's GameName
     * @param id       the game's id
     */
    public void selectRemove(String gameName, int id) {
        for (Game g : games.get(gameName)) {
            if (g.getId() == id) {
                games.get(gameName).remove(g);
                break;
            }
        }
    }


    /**
     * Return the List of games with the given gameName that being managed
     *
     * @param gameName the game's name
     * @return list of games with the given gameName
     */
    public List<Game> getGames(String gameName) {
        return this.games.getOrDefault(gameName, new ArrayList<Game>());
    }

    /**
     * when user finish a Game, remove it from the list and notify the scoreBoard
     *
     * @param gameName the finished game's name
     * @param id       the finished game's id
     */
    public void finishGame(String gameName, int id) {
        for (Game g : games.get(gameName)) {
            if (g.getId() == id) {
                games.get(gameName).remove(g);
                setChanged();
                notifyObservers(g);
                break;
            }
        }
    }

    /**
     * Update the GameManager in order to save the input game
     *
     * @param game the game need to be saved
     */
    public void saveGame(Game game) {
        int id = game.getId();
        String gameName = game.getGameName();
        List<Game> gameArrayList = games.getOrDefault(gameName, new ArrayList<Game>());
        int i = 0;
        while (i < gameArrayList.size()) {
            Game g = gameArrayList.get(i);
            if (g.getId() == id) {
                gameArrayList.set(i, game);
                games.put(gameName, gameArrayList);
                break;
            }
            i++;
        }
    }


}
