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
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import group_0522.csc207.gamecentre.Common.FileConverter;
import group_0522.csc207.gamecentre.R;
import group_0522.csc207.gamecentre.main.Account;
import group_0522.csc207.gamecentre.main.GameCentre;
import group_0522.csc207.gamecentre.main.GamePage;
import group_0522.csc207.gamecentre.main.ScoreBoard;
import group_0522.csc207.gamecentre.main.ScoreItem;
/**
 * This class operates the UI and its components for the scoreboard screen of the slidingTilesGame.
 */
public class SlidingTileScoreBoardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    /**
     * the current user logged into the slidingTilesGame.
     */
    private Account user;

    private Button levelFourScores;
    private Button levelFiveScores;

    /**
     * the list of 5 text UI objects that show the top 5 user scores.
     */
    private TextView[] userScoreDisplay = new TextView[5];

    /**
     * the list of 5 text UI objects that show the top 5 scores of all users.
     */
    private TextView[] allScoreDisplay = new TextView[5];

    /**
     * the scoreboard that holds user score values.
     */
    private ScoreBoard scoreBoard;

    /**
     * used in loading and saving files.
     */
    private FileConverter converter;

    /**
     * none reference set to "none"
     */
    private String none = "None";

    /**
     * the header that indicates the scores that belong to the logged in user.
     */
    private TextView yourScore, title;

    /**
     * the text header object in the UI that indicates which scores belong to all users.
     */
    private TextView allScore;

    /**
     * Drop-down menu fo difficulty settings.
     */
    private Spinner difficultySetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard_generic);
        this.converter = new FileConverter(this);
        try {
            user = (Account) converter.loadFromFile(GameCentre.CURRENT_USER);
            scoreBoard = (ScoreBoard) converter.loadFromFile(ScoreBoard.FILE_NAME);
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }


        //ugly but needs to be brute forced since UI is predetermined by layouts.
        /*
         * the UI buttons that will control the scores displayed.
         */

        Button levelThreeScores = findViewById(R.id.difficultyThree);
        this.levelFourScores = findViewById(R.id.difficultyFour);
        this.levelFiveScores = findViewById(R.id.difficultyFive);
        this.userScoreDisplay[0] = findViewById(R.id.score0);
        this.userScoreDisplay[1] = findViewById(R.id.score1);
        this.userScoreDisplay[2] = findViewById(R.id.score2);
        this.userScoreDisplay[3] = findViewById(R.id.score3);
        this.userScoreDisplay[4] = findViewById(R.id.score4);
        this.allScoreDisplay[0] = findViewById(R.id.score5);
        this.allScoreDisplay[1] = findViewById(R.id.score6);
        this.allScoreDisplay[2] = findViewById(R.id.score7);
        this.allScoreDisplay[3] = findViewById(R.id.score8);
        this.allScoreDisplay[4] = findViewById(R.id.score9);
        this.yourScore = findViewById(R.id.yourScoresText);
        this.allScore = findViewById(R.id.allScoresText);
        this.title = findViewById(R.id.titleText);
        this.difficultySetting = findViewById(R.id.difficulty);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.slidingTilesDifficulties, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySetting.setAdapter(adapter);

        title.setText("Slidingtiles Highscores");
        updateDisplay(3);
        addBackButtonListener();

    }

    /**
     * This method will change the text values of all text labels in the UI, it will change the displayed
     * top scores to those of the difficulty corresponding to the difficulty associated to that button listener.
     *
     * @param difficulty the value, difficulty, is the scores corresponding to a
     *                   (difficulty)*(difficulty) sized slidingTilesGame that the scoreboard will display the highest scores for.
     */
    private void updateDisplay(int difficulty) {
        yourScore.setText(String.format("Your Score for %sx%s", difficulty, difficulty));
        allScore.setText(String.format("All Score for %sx%s", difficulty, difficulty));
        //For User Scores: display top 5 scores, if it exists.
        List<ScoreItem> userScore = scoreBoard.getScorePerGames(SlidingTilesGame.GAME_NAME, difficulty);
        List<ScoreItem> gameScore = scoreBoard.getScorePerUser(SlidingTilesGame.GAME_NAME, user.getUserName(), difficulty);

        int i = 0;
        while (i < gameScore.size() && i < 5) {
            ScoreItem current = gameScore.get(i);
            userScoreDisplay[i].setText(String.format("%s steps", current.getScore()));
            i++;
        }
        while (i < 5) {
            userScoreDisplay[i].setText(none);
            i++;
        }
        //Same for All player scores
        i = 0;
        while (i < userScore.size() && i < 5) {
            ScoreItem current = userScore.get(i);
            allScoreDisplay[i].setText(String.format("%s \n %s steps", current.getUserName(), current.getScore()));
            i++;
        }
        while (i < 5) {
            allScoreDisplay[i].setText(none);
            i++;
        }

    }




    /**
     * This method adds an even listener that will call a method to bring the app back to the
     * user page when pressed.
     */
    private void addBackButtonListener() {
        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchToUserPage();
            }
        });
    }

    /**
     * Switches the UI to the one of the user page and saves the file in its current state and
     */
    private void switchToUserPage() {
        Intent tmp = new Intent(this, GamePage.class);
        try {
            converter.saveToFile(ScoreBoard.FILE_NAME, scoreBoard);
        } catch (IOException e) {
            Log.e("login activity", "Can not store file: " + e.toString());
        }
        startActivity(tmp);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int difficulty = Integer.parseInt(parent.getItemAtPosition(position).toString());
        updateDisplay(difficulty);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //then do nothing; exists to satisfy the interface.
    }

}
