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
import group_0522.csc207.gamecentre.R;

/**
 * The signUp activity
 */
public class SignUpActivity extends AppCompatActivity {
    /**
     * The gameCentre
     */
    private GameCentre gameCentre;
    /**
     * The fileConverter for this class
     */
    private FileConverter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        this.converter = new FileConverter(this);
        try {
            gameCentre = (GameCentre) this.converter.loadFromFile(GameCentre.FILE_NAME);
        } catch (IOException e) {
            Log.e("User Page activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("User Page activity", "File contained unexpected data type: " + e.toString());
        }

        addCancelButtonListener();
        addSignUpButtonListener();
    }


    /**
     * Add SignUp button listener, try to create a new account with given userName and password
     */
    private void addSignUpButtonListener() {
        Button signUp = findViewById(R.id.ready);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = ((EditText) findViewById(R.id.createUserName)).getText().toString();
                String password = ((EditText) findViewById(R.id.createPassWord)).getText().toString();
                CreateAccount(userName, password);
            }
        });
    }

    /**
     * Create the account iif the given userName and PassWord is in correct format.
     * If the account created successfully, jump to the userPage
     *
     * @param userName the input userName
     * @param password the input password
     */
    private void CreateAccount(String userName, String password) {
        if (!gameCentre.isValidFormat(userName, password)) {
            Toast.makeText(getApplicationContext(), "Wrong Format", Toast.LENGTH_SHORT).show();
        } else if (!gameCentre.isValidUserName(userName)) {
            Toast.makeText(getApplicationContext(), "User already exist", Toast.LENGTH_SHORT).show();
        } else {
            /*
             * the new created account
             */
            Account user = gameCentre.createAccount(userName, password);
            try {
                converter.saveToFile(GameCentre.CURRENT_USER, user);
                converter.saveToFile(user.getFileName(), user);
                converter.saveToFile(GameCentre.FILE_NAME, gameCentre);
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                switchToUserPage();
            } catch (IOException e) {
                Log.e("User Page activity", "Can not save file: " + e.toString());
            }
        }
    }

    /**
     * Add a CancelButton, switch to LogIn page once click
     */
    private void addCancelButtonListener() {
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLogInPage();
            }
        });
    }


    /**
     * Switch to user page
     */
    private void switchToUserPage() {
        Intent tmp = new Intent(this, UserPage.class);
        startActivity(tmp);
    }

    /**
     * Switch to Log in page
     */
    private void switchToLogInPage() {
        Intent tmp = new Intent(this, LogInActivity.class);
        startActivity(tmp);
    }


}