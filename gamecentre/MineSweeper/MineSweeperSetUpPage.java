package group_0522.csc207.gamecentre.MineSweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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

public class MineSweeperSetUpPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /**
     * current user
     */
    private Account user;
    /**
     * A file converter
     */
    private FileConverter converter;
    /**
     * current manager
     */
    private GameManager manager;
    /**
     * the number of mine chosen
     */
    private int numOfMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        converter = new FileConverter(this);
        // set up spinner
        setUpSpinner();
        //set up buttons
        Button[] createGameButtons = new Button[3];
        createGameButtons[0] = findViewById(R.id.board1);
        createGameButtons[1] = findViewById(R.id.board2);
        createGameButtons[2] = findViewById(R.id.board3);
        for(int i =0; i< createGameButtons.length; i ++){
            addCreateGameButtonListener(createGameButtons[i],i+7);
        }
        addGoBackListener();
        try {
            user = (Account) converter.loadFromFile(GameCentre.CURRENT_USER);
            manager = user.getGameManager();
        } catch (IOException e) {
            Log.e("User Page activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("User Page activity", "File contained unexpected data type: " + e.toString());
        }
        TextView text1 = findViewById(R.id.difficulty);
        text1.setText("Choose the board");
        TextView text2 = findViewById(R.id.number);
        text2.setText("Number of Mines");

    }

    /**
     * setup the spinner;
     */
    private void setUpSpinner() {
        Spinner undoSteps = findViewById(R.id.undoSteps);
        undoSteps.setOnItemSelectedListener(this);
        List<String> steps = new ArrayList<>();
        steps.add("5 Mines");
        steps.add("6 Mines");
        steps.add("7 Mines");
        steps.add("8 Mines");
        steps.add("9 Mines");
        steps.add("10 Mines");
        ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, steps);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        undoSteps.setAdapter(data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        this.numOfMine = Character.getNumericValue(item.charAt(0));
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg) {
        this.numOfMine = MineSweeperGame.DEFAULT_NUM_MINE;
    }

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

    /**
     * add an goBack button, once click go back to gamePage
     */
    private void addGoBackListener() {
        Button back = findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGamePage();
            }
        });
    }

    /**
     * Switch to gamePage
     */
    private void switchToGamePage() {
        Intent tmp = new Intent(this, GamePage.class);
        try {
            converter.saveToFile(GameCentre.CURRENT_USER, user);
        } catch (IOException e) {
            Log.e("Difficulty activity", "Can not store file: " + e.toString());
        }
        startActivity(tmp);
    }

    /**
     * Switch to MineSweeperGameActivity page
     */
    private void switchToGameActivity() {
        Intent tmp = new Intent(this, MineSweeperActivity.class);
        startActivity(tmp);
    }

    /**
     * Given a complexity create a new MineSweeperGame, save it to file and switch to the MineSweeperGameActivity page
     *
     * @param complexity
     */
    private void addGame(int[] complexity) {
        try {
           /**
             * MineSweeperGame being created
             */
            MineSweeperGame mineSweeperGame = new MineSweeperGame(complexity, manager.getId(), user.getUserName(), this.numOfMine);
            manager.addGame(mineSweeperGame);
            converter.saveToFile(user.getFileName(), user);
            converter.saveToFile(GameCentre.CURRENT_USER, user);
            converter.saveToFile(GameCentre.TEMP_GAME, mineSweeperGame);
            switchToGameActivity();
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(getApplicationContext(), "Your saved games have reached the maximum", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("Difficulty activity", "Can not save file: " + e.toString());
        }
    }
}
