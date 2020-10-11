package group_0522.csc207.gamecentre.SlingdingTiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import group_0522.csc207.gamecentre.Common.FileConverter;
import group_0522.csc207.gamecentre.R;
import group_0522.csc207.gamecentre.main.Account;
import group_0522.csc207.gamecentre.main.GameCentre;
import group_0522.csc207.gamecentre.main.GameManager;
import group_0522.csc207.gamecentre.main.GamePage;

/**
 * The set up activity for SlidingTileGame
 */
public class SlidingSetUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /**
     * Current user
     */
    private Account user;
    /**
     * A file converter
     */
    private FileConverter converter;
    /**
     * The gameManager for current user
     */
    private GameManager manager;
    /**
     * The chosen undoSteps
     */
    private int setUndoSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        converter = new FileConverter(this);
        // set up spinner
        setUpSpinner();
        //set up buttons
        addGoBackListener();
        Button[] createGameButtons = new Button[3];
        createGameButtons[0] = findViewById(R.id.board1);
        createGameButtons[1] = findViewById(R.id.board2);
        createGameButtons[2] = findViewById(R.id.board3);
        for(int i =0; i< createGameButtons.length; i ++){
            addCreateGameButtonListener(createGameButtons[i],i+3);
        }
        try {
            user = (Account) converter.loadFromFile(GameCentre.CURRENT_USER);
            manager = user.getGameManager();
        } catch (IOException e) {
            Log.e("User Page activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("User Page activity", "File contained unexpected data type: " + e.toString());
        }

    }

    /**
     * setup the spinner;
     */
    private void setUpSpinner() {
        Spinner undoSteps = findViewById(R.id.undoSteps);
        undoSteps.setOnItemSelectedListener(this);
        List<String> steps = new ArrayList<>();
        steps.add("3 steps");
        steps.add("4 steps");
        steps.add("5 steps");
        steps.add("6 steps");
        steps.add("unlimited steps");
        ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, steps);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        undoSteps.setAdapter(data);
        undoSteps.setPrompt(String.format("Default: %s steps", SlidingTilesGame.DEFAULT_UNDO_STEPS));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if (item.charAt(0) == 'u') {
            this.setUndoSteps = -1;

        } else {
            this.setUndoSteps = Character.getNumericValue(item.charAt(0));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg) {
        this.setUndoSteps = SlidingTilesGame.DEFAULT_UNDO_STEPS;
    }

    /**
     * add an goBack button, once click go back to userPage
     */
    private void addGoBackListener() {
        Button back = findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToUserPage();
            }
        });
    }

    /**
     * Switch to userPage
     */
    private void switchToUserPage() {
        Intent tmp = new Intent(this, GamePage.class);
        try {
            converter.saveToFile(GameCentre.CURRENT_USER, user);
        } catch (IOException e) {
            Log.e("Set up activity", "Can not store file: " + e.toString());
        }
        startActivity(tmp);
    }

    /**
     * Switch to slidingTilesGame page
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, SlidingTileGameActivity.class);
        startActivity(tmp);
    }

    /**
     * Given a complexity create a new slidingTilesGame, save it to file and switch to the slidingTilesGame page
     *
     * @param complexity
     */
    private void addGame(int[] complexity) {
        try {
            SlidingTilesGame slidingTilesGame = new SlidingTilesGame(complexity, manager.getId(), user.getUserName(), this.setUndoSteps);
            manager.addGame(slidingTilesGame);
            converter.saveToFile(user.getFileName(), user);
            converter.saveToFile(GameCentre.CURRENT_USER, user);
            converter.saveToFile(GameCentre.TEMP_GAME, slidingTilesGame);
            switchToGame();
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(getApplicationContext(), "Your saved games have reached the maximum", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("Difficulty activity", "Can not save file: " + e.toString());
        }
    }

    /**
     * Create the listener for createGameButton, set the difficulty text for it and linked it with addGame
     * @param createGameButton the button needed to add listener
     * @param difficulty the difficulty chosen
     */
    private void addCreateGameButtonListener(Button createGameButton, final int difficulty) {
        createGameButton.setText(String.format("%sX%s", difficulty, difficulty));
        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] complexity = {difficulty, difficulty};
                addGame(complexity);
            }
        });
    }
}