package group_0522.csc207.gamecentre.main;

import android.support.annotation.NonNull;

import java.io.Serializable;

import group_0522.csc207.gamecentre.Common.Game;

/**
 * The scoreItem. In order to store score for each finished Game, we only need to know the gameName, userName, difficulty and score
 */
public class ScoreItem implements Comparable<ScoreItem>, Serializable {
    /**
     * The gameName
     */
    private String gameName;
    /**
     * The userName
     */
    private String userName;
    /**
     * The difficulty
     */
    private Integer difficulty;
    /**
     * The score
     */
    private Integer score;

    /**
     * Create a new scoreItem for the finished game
     *
     * @param game the finish game
     */
    public ScoreItem(Game game) {
        this.gameName = game.getGameName();
        this.userName = game.getUserName();
        this.score = game.getScore();
        this.difficulty = game.getDifficulty();
    }

    /**
     * Return the gameName
     *
     * @return gameName
     */
    public String getGameName() {
        return this.gameName;
    }

    /**
     * Return the userName
     *
     * @return userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Return the score
     *
     * @return score
     */
    public Integer getScore() {
        return this.score;
    }

    /**
     * Return the difficulty
     *
     * @return difficulty
     */
    public Integer getDifficulty() {
        return this.difficulty;
    }

    @Override
    public int compareTo(@NonNull ScoreItem other) {
        return (this.score - other.getScore());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ScoreItem)) {
            return false;
        }
        ScoreItem item = (ScoreItem) obj;
        return (this.gameName.equals(item.getGameName()) && this.userName.equals(item.getUserName()) && this.score.equals(item.getScore())
                && this.difficulty.equals(item.getDifficulty()));
    }
}
