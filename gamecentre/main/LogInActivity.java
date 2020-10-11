package group_0522.csc207.gamecentre.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import group_0522.csc207.gamecentre.Common.FileConverter;
import group_0522.csc207.gamecentre.Common.UserNotFoundException;
import group_0522.csc207.gamecentre.R;

/**
 * LogIn Activity
 */
public class LogInActivity extends AppCompatActivity {
    /**
     * The GameCentre
     */
    private GameCentre gameCentre;
    /**
     * The converter for this activity
     */
    private FileConverter converter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        addLogInButtonListener();
        addSignUpButtonListener();
        this.converter = new FileConverter(this);
        gameCentre = null;
        ScoreBoard scoreBoard = null;
        try {
            gameCentre = (GameCentre) converter.loadFromFile(GameCentre.FILE_NAME);
            scoreBoard = (ScoreBoard) converter.loadFromFile(ScoreBoard.FILE_NAME);
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        //if it is the first time opening the app, create a new gameCentre
        if (gameCentre == null) {
            gameCentre = new GameCentre();
            saveGameCentre();
        }
        //if it is the first time opening the app, create a new scoreBoard
        if (scoreBoard == null) {
            scoreBoard = new ScoreBoard();
            try {
                converter.saveToFile(ScoreBoard.FILE_NAME, scoreBoard);
            } catch (IOException e) {
                Log.e("login activity", "Can not store file: " + e.toString());
            }
        }

    }

    /**
     * Add a logIn Button, try to log in using the input userName and password once click
     */
    private void addLogInButtonListener() {
        Button logIn = findViewById(R.id.logIn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = ((EditText) findViewById(R.id.userName)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                LogIn(userName, password);
            }
        });
    }

    /**
     * Login, switch to userPage iff logIn Successfully
     *
     * @param userName the input userName
     * @param password the input password
     */
    private void LogIn(String userName, String password) {
        try {
            String userFile = gameCentre.getUserFile(userName);
            Account currentUser = (Account) converter.loadFromFile(userFile);
            if (gameCentre.logIn(password, currentUser)) {
                converter.saveToFile(GameCentre.CURRENT_USER, currentUser);
                Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                switchToUserPage();
            } else {
                Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            Log.e("login activity", "Can not load file: " + e.toString());
            Toast.makeText(getApplicationContext(), "Can not load file", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "unreadable: " + e.toString());
            Toast.makeText(getApplicationContext(), "Can not load file", Toast.LENGTH_SHORT).show();
        } catch (UserNotFoundException e) {
            Toast.makeText(getApplicationContext(), "User doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Add a SignUpButton, switch to signUp page once click
     */
    private void addSignUpButtonListener() {
        Button signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSignUp();
            }
        });
    }

    /**
     * Switch to GamePage
     */
    private void switchToUserPage() {
        Intent tmp = new Intent(this, UserPage.class);
        saveGameCentre();
        startActivity(tmp);
    }


    /**
     * Switch to SignUpPage
     */
    private void switchToSignUp() {
        Intent tmp = new Intent(this, SignUpActivity.class);
        saveGameCentre();
        startActivity(tmp);
    }

    /**
     * Save the gameCentre class to file
     */
    private void saveGameCentre() {
        try {
            converter.saveToFile(GameCentre.FILE_NAME, gameCentre);
        } catch (IOException e) {
            Log.e("login activity", "Can not store file: " + e.toString());
        }
    }


}