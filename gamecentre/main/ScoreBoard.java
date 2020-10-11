package group_0522.csc207.gamecentre.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import group_0522.csc207.gamecentre.Common.Game;
import group_0522.csc207.gamecentre.Common.Triplet;
import group_0522.csc207.gamecentre.Common.Tuple;

/**
 * The scoreBoard
 */
public class ScoreBoard implements Observer, Serializable {
    /**
     * ScorePerUser map, the keys are triplet contain userName, gameName and difficulty, the value are ArrayList of score item
     * which contains score, gameName,userName and difficulty
     */
    private Map<Triplet<String, String, String>, List<ScoreItem>> scorePerUser;
    /**
     * ScorePerGame map, the keys are tuple contain gameName and difficulty, the value are ArrayList of score pair which contains
     * score, gameName and difficulty
     */
    private Map<Tuple<String, String>, List<ScoreItem>> scorePerGame;
    /**
     * The file directory to save the scoreBoard object
     */
    public static final String FILE_NAME = "ScoreBoard.ser";

    /**
     * Create a new ScoreBoard
     */
    public ScoreBoard() {
        this.scorePerUser = new HashMap<>();
        this.scorePerGame = new HashMap<>();
    }


    @Override
    public void update(Observable obs, Object obj) {
        if (obj instanceof Game) {
            Game finishedGame = (Game) obj;
            String user = finishedGame.getUserName();
            Integer difficult = finishedGame.getDifficulty();
            String game = finishedGame.getGameName();
            String difficultyStr = String.valueOf(difficult);

            //set up the keys and item
            ScoreItem newItem = new ScoreItem(finishedGame);
            Triplet<String, String, String> tripletKey = new Triplet<>(game, difficultyStr, user);
            Tuple<String, String> tupleKey = new Tuple<>(game, difficultyStr);

            //update the map
            updateScore(scorePerGame, tupleKey, newItem);
            updateScore(scorePerUser, tripletKey, newItem);

        }


    }


    /**
     * Update the Map according to the input key and item, the value of the map should be a list of item
     * @param map the map we need update
     * @param key the key for the item
     * @param item the item we need put into the innerList
     * @param <T> the class for the key
     * @param <E> the class for the item should implement comparable
     */
    private <T, E extends Comparable<E>> void updateScore(Map<T, List<E>> map, T key, E item) {
        ArrayList<E> newList = new ArrayList<>();
        List<E> innerList = map.getOrDefault(key, newList);
        if (!(innerList.contains(item))) {
            innerList.add(item);
            Collections.sort(innerList);
            map.put(key, innerList);
        }
    }

    /**
     * Return the List associate with the given difficulty and game in ScorePerGame
     *
     * @param difficulty the difficulty of game
     * @param gameName the name of game
     * @return list of scoreItem for given difficulty and gameName
     */
    public List<ScoreItem> getScorePerGames(String gameName, Integer difficulty) {
        String difficultyStr = String.valueOf(difficulty);
        Tuple<String, String> key = new Tuple<>(gameName, difficultyStr);
        return scorePerGame.getOrDefault(key, new ArrayList<ScoreItem>());
    }

    /**
     * Return the List associate with the given difficulty, userName, and difficulty in ScorePerUser
     *
     * @param userName the name of user who play this game
     * @param difficulty the difficulty of the game
     * @param gameName the name of the game
     * @return list of scoreItem for given difficulty, gameName and UserName
     */
    public List<ScoreItem> getScorePerUser(String gameName, String userName, Integer difficulty) {
        String difficultyStr = String.valueOf(difficulty);
        Triplet<String, String, String> key = new Triplet<>(gameName, difficultyStr, userName);
        return scorePerGame.getOrDefault(key, new ArrayList<ScoreItem>());
    }

}
