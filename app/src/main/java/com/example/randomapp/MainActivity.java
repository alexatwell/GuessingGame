
package com.example.randomapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int randVal;
    private int tries = 1;
    private int maxTries;
    private int levelCounter = 1;
    private int upperbound;
    private Spinner difficultySpinner;
    private TextView attemptView;
    private TextView levelView;
    private TextView welcomeView;
    private EditText gRangeValue;
    private SeekBar guessRange;
    private Button startBtn;
    private ImageButton retryBtn;
    private ImageButton returnBtn;
    private ImageButton levelUpBtn;
    private AppCompatButton guessBtn;
    private String remainder;
    private String level;
    RelativeLayout middleSec;
    private final String rules = "RULES:" +
            "\n 1: Press Start to generate a random number and begin playing." +
            "\n 2: The number of attempts is assigned by the difficulty setting" +
            "\n 3: Move the seek bar to choose a number." +
            "\n 4: Tap the 'GUESS' button to make that guess." +
            "\n 5: You get 'HOTTER!' when your guess is more than the value" +
            "\n 6: You get 'COLDER!' when your guess is less than the value" +
            "\n 7: Guess the correct value to win" +
            "\n\n GOOD LUCK!!!";

    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI elements
        difficultySpinner  = findViewById(R.id.spinner);
        ImageButton rulesBtn = findViewById(R.id.instructionsBtn);
        welcomeView = findViewById(R.id.welcomeView);
        attemptView = findViewById(R.id.attemptView);
        levelView = findViewById(R.id.levelView);
        guessRange = findViewById(R.id.guessControl);
        gRangeValue = findViewById(R.id.guessControlVal);
        gRangeValue.setEnabled(false);
        startBtn = findViewById(R.id.startBtn);
        guessBtn = findViewById(R.id.guessBtn);
        retryBtn = findViewById(R.id.retryBtn);
        returnBtn = findViewById(R.id.returnBtn);
        levelUpBtn = findViewById(R.id.levelUpBtn);

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        //final EditText editGuess =  findViewById(R.id.editGuess);
        Random rand = new Random();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        middleSec = findViewById(R.id.middleSection);

        // Add difficulty options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this ,R.array.difficulty_list,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        // New game
        startBtn.setOnClickListener(v -> {
            setDifficulty();
            difficultySpinner.setVisibility(View.INVISIBLE);
            startBtn.setVisibility(View.INVISIBLE);
            welcomeView.setVisibility(View.INVISIBLE);
            attemptView.setVisibility(View.VISIBLE);
            levelView.setVisibility(View.VISIBLE);
            guessBtn.setVisibility(View.VISIBLE);
            guessRange.setVisibility(View.VISIBLE);
            guessRange.setMax(upperbound);
            guessRange.setProgress(rand.nextInt(upperbound));
            gRangeValue.setVisibility(View.VISIBLE);
            gRangeValue.setText(String.valueOf(guessRange.getProgress()));
            attemptView.setText(remainder);
            levelView.setText(level);
        });

        // Return to Home screen
        returnBtn.setOnClickListener(v ->{
            difficultySpinner.setVisibility(View.VISIBLE);
            startBtn.setVisibility(View.VISIBLE);
            welcomeView.setVisibility(View.VISIBLE);
            attemptView.setVisibility(View.INVISIBLE);
            levelView.setVisibility(View.INVISIBLE);
            guessRange.setVisibility(View.INVISIBLE);
            gRangeValue.setVisibility(View.INVISIBLE);
            retryBtn.setVisibility(View.INVISIBLE);
            returnBtn.setVisibility(View.INVISIBLE);
        });

        // Continue current difficulty
        retryBtn.setOnClickListener(v ->{
            setDifficulty();
            guessBtn.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.INVISIBLE);
            returnBtn.setVisibility(View.INVISIBLE);
            guessRange.setProgress(rand.nextInt(upperbound));
            gRangeValue.setText(String.valueOf(guessRange.getProgress()));
            attemptView.setText(remainder);
            levelView.setText(level);
        });

        // Continue to next level
        levelUpBtn.setOnClickListener(v ->{
            raiseDifficulty();
            guessBtn.setVisibility(View.VISIBLE);
            levelUpBtn.setVisibility(View.INVISIBLE);
            retryBtn.setVisibility(View.INVISIBLE);
            returnBtn.setVisibility(View.INVISIBLE);
            guessRange.setMax(upperbound);
            guessRange.setProgress(rand.nextInt(upperbound));
            gRangeValue.setText(String.valueOf(guessRange.getProgress()));
            levelView.setText(level);
        });

        // Update the edittext to show the selected value on the seekbar
        guessRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress + 1;
                gRangeValue.setText(String.valueOf(progressChanged));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Determine when a game is won
        guessBtn.setOnClickListener(v -> {
            final int guessVal = guessRange.getProgress() + 1;
            if(monitorTries()) {
                if (guessVal < randVal) {
                    if (tries <= 0) {
                        return;
                    }
                    if (guessVal >= randVal / 2) {
                        mToast.setText("COLD");
                    } else {
                        mToast.setText("COLDER");
                    }
                    mToast.show();
                    middleSec.setBackgroundResource(R.drawable.ic_baseline_snowflake_24);
                } else {
                    if (guessVal <= randVal * 2) {
                        mToast.setText("HOT");
                    } else {
                        mToast.setText("HOTTER");
                    }
                    mToast.show();
                    middleSec.setBackgroundResource(R.drawable.ic_baseline_fire_24);
                }
            }
        });

        // Show the rules
        rulesBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage(rules)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    // Decrement tries and end game if player is out of attempts
    public boolean monitorTries() {
        tries++;
        if (tries == maxTries){
            remainder = "Last Chance!!";
        }else{
            remainder = "Attempt #" + tries;
        }
        attemptView.setText(remainder);
        if (tries > maxTries && Integer.parseInt(String.valueOf(gRangeValue.getText())) != randVal ){
            mToast.setText("BETTER LUCK NEXT TIME!!");
            mToast.show();
            lostCurrentGame();
            return false;
        }else if(Integer.parseInt(String.valueOf(gRangeValue.getText()))  == randVal ){
            mToast.setText("WELL DONE, YOU'VE WON!!");
            mToast.show();
            wonCurrentGame();
            return false;
        }
        return true;
    }

    // Stop the current game and reset variables in the event that the game is lost
    public void lostCurrentGame(){
        String ans = "GAME OVER!"; //"Answer: " + randVal;
        tries = 1;
        middleSec.setBackgroundResource(0);
        attemptView.setText(ans);
        retryBtn.setVisibility(View.VISIBLE);
        returnBtn.setVisibility(View.VISIBLE);
        guessBtn.setVisibility(View.INVISIBLE);
    }

    // Stop the current game and reset variables if the level was cleared
    public void wonCurrentGame(){
        String ans = "VICTORY!!!"; //"Answer: " + randVal;
        tries = 1;
        middleSec.setBackgroundResource(0);
        attemptView.setText(ans);
        levelUpBtn.setVisibility(View.VISIBLE);
        returnBtn.setVisibility(View.VISIBLE);
        guessBtn.setVisibility(View.INVISIBLE);
    }

    // Set the number of tries. determine the generated number, and set the seekbar's limit
    public void setDifficulty(){
        Random rand = new Random(); //instance of random class
        int x = difficultySpinner.getSelectedItemPosition();
        levelCounter = 1;

        if (x == 0){
            // Beginner Difficulty
            maxTries = 4;
            upperbound = 9;
        }else if(x == 1){
            // Intermediate Difficulty
            maxTries = 5;
            upperbound = 49;
        }else if(x == 2){
            // Professional Difficulty
            maxTries = 10;
            upperbound = 99;
        }else{
            // Expert Difficulty
            maxTries = 1;
            upperbound = 99;
            remainder = "Only One Chance!";
            int int_random = rand.nextInt(upperbound);
            randVal = int_random + 1;
            return;
        }
        level = "LEVEL " + levelCounter;
        remainder = "Attempt #" + tries;
        //generate random values from 1 to the upperbound value
        int int_random = rand.nextInt(upperbound);
        randVal = int_random + 1;
    }

    // Increase the difficulty
    public void raiseDifficulty(){
        Random rand = new Random(); //instance of random class
        int x = difficultySpinner.getSelectedItemPosition();
        levelCounter++;

        if (x == 0){
            // Beginner Difficulty
            upperbound = upperbound + 9;
        }else if(x == 1){
            // Intermediate Difficulty
            upperbound += 49;
        }else if(x == 2){
            // Professional Difficulty
            upperbound += 99;
        }else{
            // Expert Difficulty
            upperbound += 99;
            remainder = "Only One Chance!";
            int int_random = rand.nextInt(upperbound);
            randVal = int_random + 1;
            return;
        }
        level = "LEVEL " + levelCounter;
        remainder = "Attempt #" + tries;
        //generate random values from 1 to the upperbound value
        int int_random = rand.nextInt(upperbound);
        randVal = int_random + 1;
    }

}