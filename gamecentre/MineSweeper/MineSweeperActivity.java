package group_0522.csc207.gamecentre.MineSweeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import group_0522.csc207.gamecentre.Common.CustomAdapter;
import group_0522.csc207.gamecentre.Common.FileConverter;
import group_0522.csc207.gamecentre.Common.GestureDetectGridView;
import group_0522.csc207.gamecentre.R;
import group_0522.csc207.gamecentre.main.Account;
import group_0522.csc207.gamecentre.main.GameCentre;
import group_0522.csc207.gamecentre.main.GameManager;
import group_0522.csc207.gamecentre.main.GamePage;
import group_0522.csc207.gamecentre.main.ScoreBoard;

public class MineSweeperActivity extends AppCompatActivity implements Observer {
    /**
     * The mineSweeperGame.
     */
    private MineSweeperGame mineSweeperGame;
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
    private Timer timer;
    private TextView state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.converter = new FileConverter(this);
        try {
            user = (Account) converter.loadFromFile(GameCentre.CURRENT_USER);
            mineSweeperGame = (MineSweeperGame) converter.loadFromFile(GameCentre.TEMP_GAME);
            scoreBoard = (ScoreBoard) converter.loadFromFile(ScoreBoard.FILE_NAME);
            this.manager = user.getGameManager();
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        createTileButtons(this);
        setContentView(R.layout.activity_mine_sweeper);
        addLeaveListener();
        addDiscardButtonListener();
        TextView scoreText = findViewById(R.id.scoreDisplay);
        scoreText.setText(String.format("%s Second", mineSweeperGame.getScore()));
        state = findViewById(R.id.gameState);
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(mineSweeperGame.getMineSweeperBoard().getNumCols());
        gridView.setGame(mineSweeperGame, manager);
        timer = new Timer();
        gridView.setTimer(timer, scoreText);
        mineSweeperGame.getMineSweeperBoard().addObserver(this);
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

                        columnWidth = displayWidth / mineSweeperGame.getMineSweeperBoard().getNumCols();
                        columnHeight = displayHeight / mineSweeperGame.getMineSweeperBoard().getNumRows();

                        display();
                    }
                });
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    private void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }


    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        MineSweeperBoard board = mineSweeperGame.getMineSweeperBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != board.getNumRows(); row++) {
            for (int col = 0; col != board.getNumCols(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                tmp.setText(board.getTile(row, col).getText());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        MineSweeperBoard board = mineSweeperGame.getMineSweeperBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / board.getNumRows();
            int col = nextPos % board.getNumRows();
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            b.setText(board.getTile(row, col).getText());
            b.setTextColor(board.getTile(row, col).getColor());
            b.setTextSize(MineSweeperTile.TEXT_SIZE);
            nextPos++;
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        timer.stopTimer();
        mineSweeperGame.pauseGame();
        saveToFile();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.stopTimer();
        mineSweeperGame.pauseGame();
        saveToFile();
    }

    /**
     * Save the mineSweeperGame.
     */
    public void saveToFile() {
        try {
            manager.saveGame(mineSweeperGame);
            converter.saveToFile(user.getFileName(), user);
            converter.saveToFile(GameCentre.CURRENT_USER, user);
            converter.saveToFile(ScoreBoard.FILE_NAME, scoreBoard);
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    /**
     * add a leave and save button, save the mineSweeperGame and return to the userPage once click.
     */
    private void addLeaveListener() {
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.stopTimer();
                mineSweeperGame.pauseGame();
                saveToFile();
                switchToGamePage();
            }
        });
    }

    /**
     * add a DiscardButton, discard the mineSweeperGame and return the the userPage once click.
     */
    private void addDiscardButtonListener() {
        Button discard = findViewById(R.id.discard);
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    manager.selectRemove(mineSweeperGame.getGameName(), mineSweeperGame.getId());
                    converter.saveToFile(user.getFileName(), user);
                    converter.saveToFile(GameCentre.CURRENT_USER, user);
                    converter.saveToFile(ScoreBoard.FILE_NAME, scoreBoard);
                    switchToGamePage();

                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }

            }
        });
    }


    /**
     * switch to userPage
     */
    private void switchToGamePage() {
        Intent tmp = new Intent(this, GamePage.class);
        startActivity(tmp);
    }

    @Override
    public void update(Observable o, Object arg) {
        saveToFile();
        this.state.setText(mineSweeperGame.getGameState());
        display();
    }
}

