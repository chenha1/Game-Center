package group_0522.csc207.gamecentre.SlingdingTiles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
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
import group_0522.csc207.gamecentre.main.GamePage;
import group_0522.csc207.gamecentre.main.ScoreBoard;

/**
 * The slidingTilesGame activity.
 */
public class SlidingTileGameActivity extends AppCompatActivity implements Observer {

    /**
     * The slidingTilesGame.
     */
    private SlidingTilesGame slidingTilesGame;
    /**
     * Current user
     */
    private Account user;
    /**
     * Current manager
     */
    private GameManager manager;
    /**
     * Converter
     */
    private FileConverter converter;
    /**
     * ScoreBoard
     */
    private ScoreBoard scoreBoard;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;


    /**
     * Grid View and calculated column height and width based on device size
     */
    private GestureDetectGridView gridView;
    /**
     * The width and Height for each column
     */
    private static int columnWidth, columnHeight;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    private void display() {
        setText();
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.converter = new FileConverter(this);
        try {
            user = (Account) converter.loadFromFile(GameCentre.CURRENT_USER);
            slidingTilesGame = (SlidingTilesGame) converter.loadFromFile(GameCentre.TEMP_GAME);
            scoreBoard = (ScoreBoard) converter.loadFromFile(ScoreBoard.FILE_NAME);
            this.manager = user.getGameManager();
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        createTileButtons(this);
        setContentView(R.layout.activity_main);
        addUndoButtonListener();
        addLeaveListener();
        addDiscardButtonListener();

        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(slidingTilesGame.getSlidingBoard().getNumCols());
        gridView.setGame(slidingTilesGame, manager);
        slidingTilesGame.getSlidingBoard().addObserver(this);
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

                        columnWidth = displayWidth / slidingTilesGame.getSlidingBoard().getNumCols();
                        columnHeight = displayHeight / slidingTilesGame.getSlidingBoard().getNumRows();

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
        Board slidingBoard = slidingTilesGame.getSlidingBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != slidingTilesGame.getSlidingBoard().getNumRows(); row++) {
            for (int col = 0; col != slidingTilesGame.getSlidingBoard().getNumCols(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(slidingBoard.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board slidingBoard = slidingTilesGame.getSlidingBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / slidingTilesGame.getSlidingBoard().getNumRows();
            int col = nextPos % slidingTilesGame.getSlidingBoard().getNumRows();
            b.setBackgroundResource(slidingBoard.getTile(row, col).getBackground());
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
            manager.saveGame(slidingTilesGame);
            converter.saveToFile(user.getFileName(), user);
            converter.saveToFile(GameCentre.CURRENT_USER, user);
            converter.saveToFile(ScoreBoard.FILE_NAME, scoreBoard);
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Add an UndoButton, undo one step once click. If there is no more steps to undo, it will notify the user
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.backdo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    slidingTilesGame.undo();
                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getApplicationContext(), "No more steps to undo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Add a leave and save button, save the slidingTilesGame and return to the userPage once click.
     */
    private void addLeaveListener() {
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile();
                switchToUserPage();
            }
        });
    }

    /**
     * Add a DiscardButton, discard the slidingTilesGame and return the the userPage once click.
     */
    private void addDiscardButtonListener() {
        Button discard = findViewById(R.id.discard);
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    manager.selectRemove(slidingTilesGame.getGameName(), slidingTilesGame.getId());
                    converter.saveToFile(user.getFileName(), user);
                    converter.saveToFile(GameCentre.CURRENT_USER, user);
                    converter.saveToFile(ScoreBoard.FILE_NAME, scoreBoard);
                    switchToUserPage();

                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            }
        });
    }


    /**
     * Switch to userPage
     */
    private void switchToUserPage() {
        Intent tmp = new Intent(this, GamePage.class);
        startActivity(tmp);
    }

    @Override
    public void update(Observable o, Object arg) {
        saveToFile();
        if (arg instanceof SlidingTilesGame) {
            switchToUserPage();
        } else {
            display();
        }
    }

    /**
     * Update the text to show the current score
     */
    private void setText() {
        TextView StepText = findViewById(R.id.step);
        StepText.setText(String.format("Total Steps: %s\n Number of undo left: %s", slidingTilesGame.getScore(), slidingTilesGame.getUndoSteps()));
    }

}
