package group_0522.csc207.gamecentre.TwentyFortyEight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import group_0522.csc207.gamecentre.Common.FileConverter;
import group_0522.csc207.gamecentre.R;
import group_0522.csc207.gamecentre.main.Account;
import group_0522.csc207.gamecentre.main.GameCentre;

public class TFEPage extends AppCompatActivity {
    /**
     * the current user
     */
    Account user;
    /**
     * a fileConverter
     */
    FileConverter converter;
    /**
     * the text to display userName
     */
    TextView userText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page);
        addLoadGameButtonListener();
        addNewGameButtonListener();
        addScoreBoardButtonListener();
        addLogOutButtonListener();
        converter = new FileConverter(this);
        try {
            user = (Account) converter.loadFromFile(GameCentre.CURRENT_USER);
        } catch (IOException e) {
            Log.e("User Page activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("User Page activity", "File contained unexpected data type: " + e.toString());
        }
        userText = findViewById(R.id.displayUser);
        userText.setText(user.getUserName());
        TextView gameText = findViewById(R.id.gameName);
        gameText.setText(TFEGame.GAME_NAME);
    }


    /**
     * add a NewGameButton, jump to difficulty Activity once click
     */
    private void addNewGameButtonListener() {
        Button newGame = findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGameDifficulty();
            }
        });
    }

    /**
     * add a LoadGameButton, jump to loading activity once click
     */
    private void addLoadGameButtonListener() {
        Button loadGame = findViewById(R.id.loadGame);
        loadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLoadingPage();
            }
        });
    }

    /**
     * add a ScoreBoardButton, jump to ScoreBoard once click
     */
    private void addScoreBoardButtonListener() {
        Button scoreBoard = findViewById(R.id.scoreBoard);
        scoreBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToScoreBoard();
            }
        });
    }

    /**
     * add a LogOut button, jump to logIn page once click
     */
    private void addLogOutButtonListener() {
        Button logOut = findViewById(R.id.LogOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToUserPage();
            }
        });
    }

    /**
     * switch to gameDifficulty page
     */
    private void switchToGameDifficulty() {
        Intent tmp = new Intent(this, TFESetUpPage.class);
        startActivity(tmp);
    }

    /**
     * switch to loading slidingTilesGame page
     */
    private void switchToLoadingPage() {
        Intent tmp = new Intent(this, TFELoadingPage.class);
        startActivity(tmp);
    }

    /**
     * switch to ScoreBoard page
     */
    private void switchToScoreBoard() {
        Intent tmp = new Intent(this, TFEActivity.class);
        startActivity(tmp);
    }

    /**
     * switch to logIn page
     */
    private void switchToUserPage() {
        Intent tmp = new Intent(this, group_0522.csc207.gamecentre.main.UserPage.class);
        startActivity(tmp);
    }

}
