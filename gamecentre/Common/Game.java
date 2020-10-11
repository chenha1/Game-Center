package group_0522.csc207.gamecentre.Common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A Game
 */
public abstract class Game implements Serializable {
    /**
     * The userName
     */
    private final String userName;
    /**
     * The difficulty of the game
     */
    private final Integer difficulty;
    /**
     * The unique id associated with the game
     */
    /**
     * The unique id for this game
     */
    private int id;
    /**
     * Created date
     */
    private Date date;

    /**
     * Create a new game
     *
     * @param userName the user's name
     * @param difficulty the difficulty of the game
     * @param id the assigned id
     * @param date the created date
     */
    protected Game(String userName, Integer difficulty, int id, Date date) {
        this.userName = userName;
        this.difficulty = difficulty;
        this.id = id;
        this.date = date;
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
     * Return the difficulty
     *
     * @return difficulty
     */
    public Integer getDifficulty() {
        return this.difficulty;
    }

    /**
     * Return the id
     *
     * @return id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Return the current score
     *
     * @return score
     */
    public abstract Integer getScore();

    /**
     * Return the gameName
     *
     * @return gameName
     */
    public abstract String getGameName();
    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        return String.format("ID: %s \n %s", this.id, strDate);
    }


}
