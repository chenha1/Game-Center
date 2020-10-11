package group_0522.csc207.gamecentre.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import group_0522.csc207.gamecentre.MineSweeper.MineSweeperGame;
import group_0522.csc207.gamecentre.MineSweeper.MineSweeperLoading;
import group_0522.csc207.gamecentre.MineSweeper.MineSweeperSetUpPage;
import group_0522.csc207.gamecentre.SlingdingTiles.SlidingLoadingPageActivity;
import group_0522.csc207.gamecentre.SlingdingTiles.SlidingSetUpActivity;
import group_0522.csc207.gamecentre.SlingdingTiles.SlidingTileScoreBoardActivity;
import group_0522.csc207.gamecentre.SlingdingTiles.SlidingTilesGame;
import group_0522.csc207.gamecentre.TwentyFortyEight.TFEGame;
import group_0522.csc207.gamecentre.TwentyFortyEight.TFELoadingPage;
import group_0522.csc207.gamecentre.TwentyFortyEight.TFESetUpPage;
import group_0522.csc207.gamecentre.Common.FileConverter;
import group_0522.csc207.gamecentre.R;

/**
 * The gamePage
 */
public class GamePage extends AppCompatActivity {
    /**
     * The current user
     */
    private Account user;

    private String currentPlaying;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page);
        addLoadGameButtonListener();
        addNewGameButtonListener();
        addScoreBoardButtonListener();
        addBackToUserPageButtonListener();
        FileConverter converter = new FileConverter(this);
        try {
            user = (Account) converter.loadFromFile(GameCentre.CURRENT_USER);
        } catch (IOException e) {
            Log.e("User Page activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("User Page activity", "File contained unexpected data type: " + e.toString());
        }
        TextView userText = findViewById(R.id.displayUser);
        userText.setText(user.getUserName());
        TextView gameText = findViewById(R.id.gameName);
        currentPlaying = user.getGameName();
        gameText.setText(currentPlaying);

    }

    /**
     * Add a NewGameButton, jump to game set up Activity once click
     */
    private void addNewGameButtonListener() {
        Button newGame = findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGameSetUpPage();
            }
        });
    }

    /**
     * Add a LoadGameButton, jump to load saved game activity once click
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
     * Add a ScoreBoardButton, jump to ScoreBoard once click
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
     * Add a LogOut button, jump back to UserPage once click
     */
    private void addBackToUserPageButtonListener() {
        Button logOut = findViewById(R.id.LogOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToUserPage();
            }
        });
    }

    /**
     * Switch to gameSetUp page
     */
    private void switchToGameSetUpPage() {
        if(currentPlaying.equals(SlidingTilesGame.GAME_NAME)) {
            Intent tmp = new Intent(this, SlidingSetUpActivity.class);
            startActivity(tmp);
        }
        if(currentPlaying.equals(MineSweeperGame.GAME_NAME)){
            Intent tmp = new Intent(this, MineSweeperSetUpPage.class);
            startActivity(tmp);
        }
        if(currentPlaying.equals(TFEGame.GAME_NAME)){
            Intent tmp = new Intent(this, TFESetUpPage.class);
            startActivity(tmp);
        }
    }

    /**
     * Switch to loading slidingTilesGame page
     */
    private void switchToLoadingPage() {
        if(currentPlaying.equals(SlidingTilesGame.GAME_NAME)) {
            Intent tmp = new Intent(this, SlidingLoadingPageActivity.class);
            startActivity(tmp);
        }
        if(currentPlaying.equals(MineSweeperGame.GAME_NAME)){
            Intent tmp = new Intent(this, MineSweeperLoading.class);
            startActivity(tmp);
        }
        if(currentPlaying.equals(TFEGame.GAME_NAME)){
            Intent tmp = new Intent(this, TFELoadingPage.class);
            startActivity(tmp);
        }
    }

    /**
     * switch to ScoreBoard page
     */
    private void switchToScoreBoard() {
        Intent tmp = new Intent(this, SlidingTileScoreBoardActivity.class);
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
