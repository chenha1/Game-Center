package group_0522.csc207.gamecentre.MineSweeper;

import android.os.Handler;
import android.widget.TextView;

//The idea of using Handler is adapt from https://www.codeproject.com/Articles/113892/Minesweeper-Minesweeper-game-for-Android.
//Codes is modified in order to fit MVC pattern

/**
 * A timer
 */
public class Timer {
    /**
     * The handler
     */
    private Handler timer;
    /**
     * the number of seconds passed
     */
    private int secondsPassed = 0;
    /**
     * the TextView to display the timing
     */
    private TextView timerText;
    /**
     * the MineSweeperGame we keep track of
     */
    private MineSweeperGame game;

    public Timer() {
        this.timer = new Handler();
    }

    /**
     * return the number of seconds passed
     *
     * @return secondsPassed
     */
    public Integer getTime() {
        return this.secondsPassed;
    }

    /**
     * set the game we keep track of
     *
     * @param game
     */
    public void setGame(MineSweeperGame game) {
        this.game = game;
        this.secondsPassed = game.getScore();
    }

    /**
     * set the TextView where we need to display the time
     *
     * @param timerText
     */
    public void setTimerText(TextView timerText) {
        this.timerText = timerText;
    }

    /**
     * start counting time
     */
    public void startTimer() {
        timer.removeCallbacks(updateTimeElapsed);
        // tell timer to run call back after 1 second
        timer.postDelayed(updateTimeElapsed, 1000);
    }

    /**
     * stop counting time
     */
    public void stopTimer() {
        // disable call backs
        timer.removeCallbacks(updateTimeElapsed);
    }

    /**
     * update Time for secondsPassed, the score of the game and the TextView to display time
     */
    private Runnable updateTimeElapsed = new Runnable() {
        public void run() {
            long currentMilliseconds = System.currentTimeMillis();
            ++secondsPassed;
            // update the score for game
            game.setScore(secondsPassed);
            // update the text
            timerText.setText(String.format("%s Second", secondsPassed));
            // add notification
            timer.postAtTime(this, currentMilliseconds);
            // notify to call back after 1 seconds
            // basically to remain in the timer loop
            timer.postDelayed(updateTimeElapsed, 1000);
        }
    };

}