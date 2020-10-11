package group_0522.csc207.gamecentre.TwentyFortyEight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import group_0522.csc207.gamecentre.Common.Board;
import group_0522.csc207.gamecentre.Common.CustomAdapter;
import group_0522.csc207.gamecentre.Common.FileConverter;
import group_0522.csc207.gamecentre.Common.GestureDetectGridView;
import group_0522.csc207.gamecentre.R;
import group_0522.csc207.gamecentre.main.Account;
import group_0522.csc207.gamecentre.main.GameCentre;
import group_0522.csc207.gamecentre.main.GameManager;
import group_0522.csc207.gamecentre.main.ScoreBoard;

public class TFEActivity extends AppCompatActivity implements Observer {
    /**git
     * The slidingTilesGame.
     */
    private TFEGame tfeGame;
    /**
     * current user
     */
    private Account user;
    /**
     * current manager
     */
    private GameManager manager;
    /**
     * converter
     */
    private FileConverter converter;
    /**
     * scoreBoard
     */
    private ScoreBoard scoreBoard;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * Constants for swiping directions. Should be an enum, probably.
     */
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.converter = new FileConverter(this);
        try {
            user = (Account) converter.loadFromFile(GameCentre.CURRENT_USER);
            tfeGame = (TFEGame) converter.loadFromFile(GameCentre.TEMP_GAME);
            scoreBoard = (ScoreBoard) converter.loadFromFile(ScoreBoard.FILE_NAME);
            this.manager = user.getGameManager();
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        createTileButtons(this);
        setContentView(R.layout.activity_tfe);

        addUndoButtonListener();
        addLeaveListener();

        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(tfeGame.getSlidingBoard().getNumCols());
        gridView.setGame(tfeGame, manager);
        tfeGame.getSlidingBoard().addObserver(this);
        manager.addObserver(scoreBoard);
        manager.addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / tfeGame.getSlidingBoard().getNumCols();
                        columnHeight = displayHeight / tfeGame.getSlidingBoard().getNumRows();

                        display();
                    }
                });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board tfeBoard = tfeGame.getSlidingBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != tfeGame.getSlidingBoard().getNumRows(); row++) {
            for (int col = 0; col != tfeGame.getSlidingBoard().getNumCols(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(tfeBoard.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board tfeBoard = tfeGame.getSlidingBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / tfeGame.getSlidingBoard().getNumRows();
            int col = nextPos % tfeGame.getSlidingBoard().getNumRows();
            b.setBackgroundResource(tfeBoard.getTile(row, col).getBackground());
            nextPos++;
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveToFile();
    }

    /**
     * Save the slidingTilesGame.
     */
    public void saveToFile() {
        try {
            manager.saveGame(tfeGame);
            converter.saveToFile(user.getFileName(), user);
            converter.saveToFile(GameCentre.CURRENT_USER, user);
            converter.saveToFile(ScoreBoard.FILE_NAME, scoreBoard);
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * add an UndoButton, undo the step once click.
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tfeGame.undo();
                    display();
                    updateTileButtons();
                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getApplicationContext(), "No more steps to undo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * add a leave and save button, save the slidingTilesGame and return to the userPage once click.
     */
    private void addLeaveListener() {
        Button back = findViewById(R.id.restart);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile();
                switchToGamePage();
            }
        });
    }

    /**
     * switch to userPage
     */
    private void switchToGamePage() {
        Intent tmp = new Intent(this, TFEPage.class);
        startActivity(tmp);
    }

    @Override
    public void update(Observable o, Object arg) {
        saveToFile();
        Toast.makeText(this,"Auto Saved",Toast.LENGTH_SHORT).show();
        if (arg instanceof TFEGame) {
            switchToGamePage();
        } else {
            display();
        }
    }
}
