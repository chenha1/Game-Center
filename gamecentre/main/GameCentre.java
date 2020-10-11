package group_0522.csc207.gamecentre.main;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import group_0522.csc207.gamecentre.Common.UserNotFoundException;

/**
 * A GameCentre
 */
public class GameCentre implements Serializable {
    /**
     * The maximum Game a user can create
     */
    public static final int MAX_DISPLAY = 5;
    /**
     * The specially file name to save the GameCentre Object
     */
    public static final String FILE_NAME = "GameCentre.ser";
    /**
     * The file name to save the current user
     */
    public static final String CURRENT_USER = "CURRENT_USER.ser";
    /**
     * The file name to save current Game
     */
    public static final String TEMP_GAME = "TEMP_GAME.ser";
    /**
     * A Map witch the key is the userName and the value is corresponding file directory for the user
     */
    private Map<String, String> accounts;

    /**
     * Create a new GameCentre
     */
    public GameCentre() {
        this.accounts = new HashMap<>();

    }


    /**
     * Return the user file's directory associated with userName, if userName is not contained in the map
     * throws UserNotFoundException
     *
     * @param userName the userName
     * @return File's directory
     * @throws UserNotFoundException
     */
    public String getUserFile(String userName) throws UserNotFoundException {
        if (accounts.containsKey(userName)) {
            return accounts.get(userName);
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     * Precondition: the user exist.
     * Return true iff the password is correct
     *
     * @param password the input password
     * @param user the account we need to check
     * @return whether the password is correct
     */
    public boolean logIn(String password, Account user) {
        return user.getPassword().equals(password);

    }

    /**
     * Return true iff the userName and password are in the correct format:
     * all the userName and password should contain 5 to 10 characters.
     *
     * @param userName user input userName
     * @param password user input password
     * @return whether the userName and password are in the correct format
     */
    public boolean isValidFormat(String userName, String password) {
        return (userName.length() <= 10 && 5 <= userName.length() && password.length() <= 10 &&
                5 <= password.length());
    }

    /**
     * Return true iff there dose not exist a user with the same userName
     *
     * @param userName the input userName
     * @return whether the userName is unique
     */
    public boolean isValidUserName(String userName) {
        for (String user : accounts.keySet()) {
            if (user.equals(userName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Precondition: The userName is unique and both userName and password are in the correct format
     * Create the account with the userName and password
     *
     * @param userName the input userName
     * @param password the input password
     * @return Account being created
     */
    public Account createAccount(String userName, String password) {
        Account newAccount = new Account(userName, password);
        accounts.put(userName, newAccount.getFileName());
        return newAccount;

    }
}
