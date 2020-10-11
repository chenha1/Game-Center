package group_0522.csc207.gamecentre.TwentyFortyEight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

import group_0522.csc207.gamecentre.Common.FileConverter;
import group_0522.csc207.gamecentre.Common.Game;
import group_0522.csc207.gamecentre.R;
import group_0522.csc207.gamecentre.main.Account;
import group_0522.csc207.gamecentre.main.GameCentre;
import group_0522.csc207.gamecentre.main.GameManager;

public class TFELoadingPage extends AppCompatActivity {
    private Account user;
    /**
     * the slidingTilesGame being load
     */
    private TFEGame tfeGame;
    /**
     * the saved slidingTilesGames
     */
    private List<Game> tfeGames;
    /**
     * a filed converter
     */
    FileConverter converter;
    /**
     * saved buttons
     */
    private Button[] saved = new Button[5];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);
        this.converter = new FileConverter(this);
        try {
            this.user = (Account) converter.loadFromFile(GameCentre.CURRENT_USER);
            GameManager manager = user.getGameManager();
            tfeGames = manager.getGames(TFEGame.GAME_NAME);
        } catch (IOException e) {
            Log.e("User Page activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("User Page activity", "File contained unexpected data type: " + e.toString());
        }
        saved[0] = findViewById(R.id.save1);
        saved[1] = findViewById(R.id.save2);
        saved[2] = findViewById(R.id.save3);
        saved[3] = findViewById(R.id.save4);
        saved[4] = findViewById(R.id.save5);
        updateText();
        addCancelButtonListener();
        addSaveButtonListener1();
        addSaveButtonListener2();
        addSaveButtonListener3();
        addSaveButtonListener4();
        addSaveButtonListener5();


    }

    /**
     * update the text for each button.
     * set it to None if no slidingTilesGame exist
     */
    private void updateText() {
        int i = 0;
        while (i < tfeGames.size() && i < 5) {
            saved[i].setText(tfeGames.get(i).toString());
            i++;
        }
    }

    /**
     * add an save button, jump to the saved slidingTilesGame if exist and click
     */
    private void addSaveButtonListener1() {
        if (tfeGames.size() >= 1) {
            saved[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tfeGame = (TFEGame) tfeGames.get(0);
                    switchToGame();
                }
            });
        }
    }

    /**
     * add an save button, jump to the saved slidingTilesGame if exist and click
     */
    private void addSaveButtonListener2() {
        if (tfeGames.size() >= 2) {
            saved[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tfeGame = (TFEGame) tfeGames.get(1);
                    switchToGame();
                }
            });
        }
    }

    /**
     * add an save button, jump to the saved slidingTilesGame if exist and click
     */
    private void addSaveButtonListener3() {
        if (tfeGames.size() >= 3) {
            saved[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tfeGame = (TFEGame) tfeGames.get(2);
                    switchToGame();
                }
            });
        }
    }

    /**
     * add an save button, jump to the saved slidingTilesGame if exist and click
     */
    private void addSaveButtonListener4() {
        if (tfeGames.size() >= 4) {
            saved[3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tfeGame = (TFEGame) tfeGames.get(3);
                    switchToGame();
                }
            });
        }
    }

    /**
     * add an save button, jump to the saved slidingTilesGame if exist and click
     */
    private void addSaveButtonListener5() {
        if (tfeGames.size() >= 5) {
            saved[4].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tfeGame = (TFEGame) tfeGames.get(4);
                    switchToGame();
                }
            });
        }
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
    private void switchToGame() {
        Intent tmp = new Intent(this, TFEActivity.class);
        try {
            converter.saveToFile(GameCentre.CURRENT_USER, user);
            converter.saveToFile(GameCentre.TEMP_GAME, tfeGame);
            startActivity(tmp);
        } catch (IOException e) {
            Log.e("Loading activity", "Can not save file: " + e.toString());
        }

    }

    /**
     * switch to userPage
     */
    private void switchToGamePage() {
        Intent tmp = new Intent(this, TFESetUpPage.class);
        try {
            converter.saveToFile(GameCentre.CURRENT_USER, user);
        } catch (IOException e) {
            Log.e("login activity", "Can not store file: " + e.toString());
        }
        startActivity(tmp);
    }
}
