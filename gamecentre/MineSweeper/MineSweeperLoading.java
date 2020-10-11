package group_0522.csc207.gamecentre.MineSweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import group_0522.csc207.gamecentre.Common.FileConverter;
import group_0522.csc207.gamecentre.Common.Game;
import group_0522.csc207.gamecentre.R;
import group_0522.csc207.gamecentre.main.Account;
import group_0522.csc207.gamecentre.main.GameCentre;
import group_0522.csc207.gamecentre.main.GameManager;
import group_0522.csc207.gamecentre.main.GamePage;

public class MineSweeperLoading extends AppCompatActivity {

    /**
     */
    private Account user;
    /**
     * The slidingTilesGame being load
     */
    private MineSweeperGame mineSweeperGame;
    /**
     * the savedButtons slidingTilesGames
     */
    private List<Game> mineSweeperGames;
    /**
     * a filed converter
     */
    private FileConverter converter;
    /**
     * savedButtons buttons
     */
    private Button[] savedButtons = new Button[5];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);
        this.converter = new FileConverter(this);
        try {
            this.user = (Account) converter.loadFromFile(GameCentre.CURRENT_USER);
            GameManager manager = user.getGameManager();
            mineSweeperGames = manager.getGames(MineSweeperGame.GAME_NAME);
        } catch (IOException e) {
            Log.e("User Page activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("User Page activity", "File contained unexpected data type: " + e.toString());
        }
        savedButtons[0] = findViewById(R.id.save1);
        savedButtons[1] = findViewById(R.id.save2);
        savedButtons[2] = findViewById(R.id.save3);
        savedButtons[3] = findViewById(R.id.save4);
        savedButtons[4] = findViewById(R.id.save5);
        updateText();
        addCancelButtonListener();
        for (int i =0;i<mineSweeperGames.size();i++){
            addSaveButtonListener(i);
        }


    }

    /**
     * update the text for each button.
     * set it to None if no slidingTilesGame exist
     */
    private void updateText() {
        int i = 0;
        while (i < mineSweeperGames.size() && i < 5) {
            savedButtons[i].setText(mineSweeperGames.get(i).toString());
            i++;
        }
    }

    /**
     * add save buttons for the savedButtons[i], jump to the savedButtons slidingTilesGames[i] if exist and click
     */
    // we need to set i final otherwise we cannot access it from inner class
    private void addSaveButtonListener(final int i) {
        savedButtons[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mineSweeperGame = (MineSweeperGame) mineSweeperGames.get(i);
                switchToGameActivity();
            }
        });
    }

    /**
     * add an cancel button, return to userPage once click
     */
    private void addCancelButtonListener() {
        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGamePage();
            }
        });
    }

    /**
     * switch to the slidingTilesGame
     */
    private void switchToGameActivity() {
        Intent tmp = new Intent(this, MineSweeperActivity.class);
        try {
            converter.saveToFile(GameCentre.CURRENT_USER, user);
            converter.saveToFile(GameCentre.TEMP_GAME, mineSweeperGame);
            startActivity(tmp);
        } catch (IOException e) {
            Log.e("Loading activity", "Can not save file: " + e.toString());
        }

    }

    /**
     * switch to userPage
     */
    private void switchToGamePage() {
        Intent tmp = new Intent(this, GamePage.class);
        try {
            converter.saveToFile(GameCentre.CURRENT_USER, user);
        } catch (IOException e) {
            Log.e("login activity", "Can not store file: " + e.toString());
        }
        startActivity(tmp);
    }
}
