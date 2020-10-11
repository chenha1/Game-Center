package group_0522.csc207.gamecentre.Common;

import android.content.Context;
import android.widget.Toast;

import group_0522.csc207.gamecentre.MineSweeper.MineSweeperGame;
import group_0522.csc207.gamecentre.MineSweeper.Timer;
import group_0522.csc207.gamecentre.SlingdingTiles.SlidingTilesGame;
import group_0522.csc207.gamecentre.TwentyFortyEight.TFEGame;
import group_0522.csc207.gamecentre.main.GameManager;


public class MovementController {

    private Game game = null;
    private GameManager manager = null;
    private Timer timer = null;

    public MovementController() {
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setGame(Game Game) {
        this.game = Game;
    }

    public void setManager(GameManager manager) {
        this.manager = manager;
    }
    public void processSwipMovement(Context context, float velocityX,float velocityY){
        if(game instanceof TFEGame){
            TFEGame currentGame = (TFEGame)game;
            if(Math.abs(velocityX)>=(Math.abs(velocityY))){
                if(velocityX > 0){
                    currentGame.swipe(4);

                }
                else{
                    currentGame.swipe(3);
                }

            }
            else{
                if(velocityY <0){
                    currentGame.swipe(2);
                }
                else{
                    currentGame.swipe(1);
                }
            }
        }
    }

    public void processTapMovement(Context context, int position, boolean display) {
        if (game instanceof SlidingTilesGame) {
            SlidingTilesGame slidingTilesGame = (SlidingTilesGame) game;
            processSlidingTileMove(context, position, slidingTilesGame);
        }
        if (game instanceof MineSweeperGame) {
            MineSweeperGame mineSweeperGame = (MineSweeperGame) game;
            if (mineSweeperGame.isClickable()) {
                processMineSweeperMove(context, position, mineSweeperGame);
            }
        }
    }

    public void processLongPressMovement(Context context, int position, boolean display) {
        if (game instanceof MineSweeperGame) {
            MineSweeperGame mineSweeperGame = (MineSweeperGame) game;
            if (mineSweeperGame.isClickable()) {
                processMineLongPressMove(context, position, mineSweeperGame);
            }
        }
        if (game instanceof SlidingTilesGame) {
            SlidingTilesGame slidingTilesGame = (SlidingTilesGame) game;
            processSlidingTileMove(context, position, slidingTilesGame);
        }

    }

    private void processMineSweeperMove(Context context, int position, MineSweeperGame mineSweeperGame) {
        if (mineSweeperGame.isSettingClick()) {
            this.timer.setGame(mineSweeperGame);
            timer.startTimer();
            mineSweeperGame.firstSettingTouchMove(position);
            checkMineGameStatus(context, position, mineSweeperGame);

        } else if (mineSweeperGame.isValidMove(position)) {
            if (!mineSweeperGame.isFirstClicked()) {
                mineSweeperGame.touchMove(position);
                checkMineGameStatus(context, position, mineSweeperGame);
            } else {
                this.timer.setGame(mineSweeperGame);
                timer.startTimer();
                mineSweeperGame.touchMove(position);
                checkMineGameStatus(context, position, mineSweeperGame);
            }
        }
    }

    private void checkMineGameStatus(Context context, int position, MineSweeperGame mineSweeperGame) {
        if (mineSweeperGame.isGameWin()) {
            mineSweeperGame.setGameState("Win");
            mineSweeperGame.gameFinished();
            timer.stopTimer();
            mineSweeperGame.setScore(timer.getTime());
            Toast.makeText(context, "YOU WIN!", Toast.LENGTH_LONG).show();
            manager.finishGame(MineSweeperGame.GAME_NAME, mineSweeperGame.getId());

        }
        if (mineSweeperGame.isGameLost(position)) {
            mineSweeperGame.setGameState("Lose");
            mineSweeperGame.gameFinished();
            timer.stopTimer();
            mineSweeperGame.setScore(timer.getTime());
            Toast.makeText(context, "BOOM!", Toast.LENGTH_LONG).show();
            manager.selectRemove(MineSweeperGame.GAME_NAME, mineSweeperGame.getId());
        }
    }

    private void processMineLongPressMove(Context context, int position, MineSweeperGame mineSweeperGame) {
        if (mineSweeperGame.isValidLongPress(position)) {
            if (!mineSweeperGame.isFirstClicked()) {
                this.timer.setGame(mineSweeperGame);
                timer.startTimer();
                mineSweeperGame.longPressMove(position);
            } else {
                mineSweeperGame.longPressMove(position);
            }
        } else {
            Toast.makeText(context, "Invalid Long Press", Toast.LENGTH_SHORT).show();
        }

    }

    private void processSlidingTileMove(Context context, int position, SlidingTilesGame slidingTilesGame) {
        if (slidingTilesGame.isValidTap(position)) {
            slidingTilesGame.touchMove(position, true );
            if (slidingTilesGame.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
                manager.finishGame(slidingTilesGame.getGameName(), slidingTilesGame.getId());
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}
