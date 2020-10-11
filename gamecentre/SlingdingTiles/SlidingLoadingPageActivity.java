package group_0522.csc207.gamecentre.SlingdingTiles;

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
import group_0522.csc207.gamecentre.main.GamePage;

/**
 * A choosing saved slidingTilesGame to load activity
 */
public class SlidingLoadingPageActivity extends AppCompatActivity {
    /**
     * The current user
     */
    private Account user;
    /**
     * The slidingTilesGame being selected
     */
    private SlidingTilesGame slidingTilesGame;
    /**
     * The List of saved slidingTilesGames
     */
    private List<Game> slidingTilesGames;
    /**
     * A filed converter
     */
    private FileConverter converter;
    /**
     * SavedButtons
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
            slidingTilesGames = manager.getGames(SlidingTilesGame.GAME_NAME);
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
        for (int i = 0; i < slidingTilesGames.size(); i++) {
            addSaveButtonListener(i);
        }


    }

    /**
     * Update the text for each button.
     * set it to None if no slidingTilesGame exist
     */
    private void updateText() {
        int i = 0;
        while (i < slidingTilesGames.size() && i < 5) {
            savedButtons[i].setText(slidingTilesGames.get(i).toString());
            i++;
        }
    }

    /**
     * Add save buttons for the savedButtons[i], jump to the slidingTilesGameActivity if exist and click
     */
    // we need to set i final otherwise we cannot access it from inner class
    private void addSaveButtonListener(final int i) {
        savedButtons[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingTilesGame = (SlidingTilesGame) slidingTilesGames.get(i);
                switchToGame();
            }
        });
    }

    /**
     * Add an cancel button, return to userPage once click
     */
    private void addCancelButtonListener() {
        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToUserPage();
            }
        });
    }

    /**
     * Switch to the slidingTilesGameActivity
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, SlidingTileGameActivity.class);
        saveToFile();
        startActivity(tmp);

    }

    /**
     * Save the currentUser and slidingTilesGame to corresponding directory
     */
    private void saveToFile() {
        try {
            converter.saveToFile(GameCentre.CURRENT_USER, user);
            converter.saveToFile(GameCentre.TEMP_GAME, slidingTilesGame);
        } catch (IOException e) {
            Log.e("Loading activity", "Can not save file: " + e.toString());
        }
    }

    /**
     * Switch to userPage
     */
    private void switchToUserPage() {
        Intent tmp = new Intent(this, GamePage.class);
        saveToFile();
        startActivity(tmp);
    }
}
