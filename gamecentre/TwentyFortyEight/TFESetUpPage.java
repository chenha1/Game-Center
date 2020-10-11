package group_0522.csc207.gamecentre.TwentyFortyEight;

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


public class TFESetUpPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /**
     * current user
     */
    private Account user;
    /**
     * A file converter
     */
    private FileConverter converter;
    /**
     * slidingTilesGame being created
     */
    private TFEGame tfeGame;
    /**
     * current manager
     */
    private GameManager manager;
    private int setUndoSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        converter = new FileConverter(this);
        // set up spinner
        setUpSpinner();
        //set up buttons
        addThreeListener();
        addFourListener();
        addFiveListener();
        addGoBackListener();
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
        undoSteps.setPrompt(String.format("Default: %s steps", TFEGame.DEFAULT_UNDO_STEPS));
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
        this.setUndoSteps = TFEGame.DEFAULT_UNDO_STEPS;
    }

    /**
     * add 3X3 button, created the slidingTilesGame once click
     */
    private void addThreeListener() {
        Button three = findViewById(R.id.board1);
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] complexity = {3, 3};
                addGame(complexity);
            }
        });
    }

    /**
     * add 4x4 button, created the slidingTilesGame once click
     */
    private void addFourListener() {
        Button four = findViewById(R.id.board2);
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] complexity = {4, 4};
                addGame(complexity);
            }
        });
    }

    /**
     * add 5x5 button, created the slidingTilesGame once click
     */
    private void addFiveListener() {
        Button five = findViewById(R.id.board3);
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] complexity = {5, 5};
                addGame(complexity);
            }
        });
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
        Intent tmp = new Intent(this, TFEPage.class);
        try {
            converter.saveToFile(GameCentre.CURRENT_USER, user);
        } catch (IOException e) {
            Log.e("Difficulty activity", "Can not store file: " + e.toString());
        }
        startActivity(tmp);
    }

    /**
     * Switch to slidingTilesGame page
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, TFEActivity.class);
        startActivity(tmp);
    }

    /**
     * Given a complexity create a new slidingTilesGame, save it to file and switch to the slidingTilesGame page
     *
     * @param complexity
     */
    private void addGame(int[] complexity) {
        try {
            tfeGame = new TFEGame(complexity, manager.getId(), user.getUserName(), this.setUndoSteps);
            manager.addGame(tfeGame);
            converter.saveToFile(user.getFileName(), user);
            converter.saveToFile(GameCentre.CURRENT_USER, user);
            converter.saveToFile(GameCentre.TEMP_GAME, tfeGame);
            switchToGame();
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(getApplicationContext(), "Your saved games have reached the maximum", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("Difficulty activity", "Can not save file: " + e.toString());
        }
    }
}
