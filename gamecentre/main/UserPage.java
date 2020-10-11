package group_0522.csc207.gamecentre.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import group_0522.csc207.gamecentre.Common.FileConverter;
import group_0522.csc207.gamecentre.MineSweeper.MineSweeperGame;
import group_0522.csc207.gamecentre.R;
import group_0522.csc207.gamecentre.SlingdingTiles.SlidingTilesGame;
import group_0522.csc207.gamecentre.TwentyFortyEight.TFEGame;

/**
 * The userPage
 */
public class UserPage extends AppCompatActivity {
    /**
     * Current user
     */
    private Account user;
    private FileConverter converter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        /*
         * a fileConverter
         */
        converter = new FileConverter(this);
        try {
            user = (Account) converter.loadFromFile(GameCentre.CURRENT_USER);
        } catch (IOException e) {
            Log.e("User Page activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("User Page activity", "File contained unexpected data type: " + e.toString());
        }
        TextView welcome = findViewById(R.id.welcomeText);
        welcome.setText(String.format("Welcome \n %s", user.getUserName()));
        Button slidingTilesGameButton = findViewById(R.id.game1);
        Button mineSweeperGameButton = findViewById(R.id.game2);
        Button TFEGameButton = findViewById(R.id.game3);
        addGameButtonListener(slidingTilesGameButton, SlidingTilesGame.GAME_NAME);
        addGameButtonListener(mineSweeperGameButton, MineSweeperGame.GAME_NAME);
        addGameButtonListener(TFEGameButton, TFEGame.GAME_NAME);
        addLogOutButtonListener();

    }

    /**
     * Add GameButtonListener for the given Button gameButton, once click it will switch to the ith gameButton page.
     * It will also set the gameName to the button
     * @param gameButton the button we need to modified
     * @param gameName the name of the game that should display on the button
     */
    private void addGameButtonListener(Button gameButton, final String gameName) {
        gameButton.setText(gameName);
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setGameName(gameName);
                saveCurrentUser();
                switchToGamePage();
            }
        });

    }


    /**
     * Add the LogOutButton listener, once click, it fill jump to log in page
     */
    private void addLogOutButtonListener() {
        Button logOut = findViewById(R.id.logOut);
        logOut.setText("LOG OUT");
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLogInPage();
            }
        });
    }

    /**
     * Switch to the gamePage
     */
    private void switchToGamePage() {
            Intent tmp = new Intent(this, GamePage.class);
            startActivity(tmp);
    }


    /**
     * Switch to the login page
     */
    private void switchToLogInPage() {
        Intent tmp = new Intent(this, LogInActivity.class);
        startActivity(tmp);
    }
    private void saveCurrentUser(){
        try {
            converter.saveToFile(GameCentre.CURRENT_USER, user);
            converter.saveToFile(user.getFileName(), user);
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("User Page activity", "Can not save file: " + e.toString());
        }
    }
}
