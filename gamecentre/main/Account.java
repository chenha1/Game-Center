package group_0522.csc207.gamecentre.main;

import java.io.Serializable;

/**
 * An account for the GameCentre
 */
public class Account implements Serializable {
    /**
     * Current gameName
     */
    private String gameName;
    /**
     * Username
     */
    private final String userName;
    /**
     * Password
     */
    private final String password;
    /**
     * The associated GameManager
     */
    private final GameManager gameManager;


    /**
     * Create a new account with userName and password
     *
     * @param userName the input userName
     * @param password the input password
     */
    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.gameManager = new GameManager();
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
     * Return the Password
     *
     * @return password
     */
    public String getPassword() {
        return this.password;
    }


    /**
     * Return the fileName to store this user account
     *
     * @return fileName
     */
    public String getFileName() {
        return String.format("%s.ser", userName);
    }

    /**
     * Return the gameManager
     *
     * @return gameManager
     */
    public GameManager getGameManager() {
        return this.gameManager;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return this.gameName;
    }

}
