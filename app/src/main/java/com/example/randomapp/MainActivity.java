
package com.example.randomapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int randVal;
    private int tries;
    private int tempTries;
    private int upperbound;
    private Spinner difficultySpinner;
    private TextView attemptView;
    private TextView welcomeView;
    private SeekBar guessRange;
    private Button startBtn;
    private ImageButton continueBtn;
    private ImageButton returnBtn;
    private AppCompatButton guessBtn;
    private String remainder;
    private final String rules = "RULES:" +
            "\n 1: Press Start to generate a random number and begin playing." +
            "\n 2: The number of attempts are assigned by the difficulty setting" +
            "\n 3: Move the seekbar to choose a number." +
            "\n 4: Tap the 'ENTER' button to make that guess." +
            "\n 5: You get 'HOTTER!' when your guess is more the value" +
            "\n 6: You get 'COLDER!' when your guess is less the value" +
            "\n 7: Guess the correct value to win" +
            "\n\n GOOD LUCK!!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        difficultySpinner  = findViewById(R.id.spinner);
        ImageButton rulesBtn = findViewById(R.id.instructionsBtn);
        welcomeView = findViewById(R.id.welcomeView);
        attemptView = findViewById(R.id.attemptView);
        guessRange = findViewById(R.id.guessControl);
        startBtn = findViewById(R.id.startBtn);
        guessBtn = findViewById(R.id.guessBtn);
        continueBtn = findViewById(R.id.continueBtn);
        returnBtn = findViewById(R.id.returnBtn);

        //final EditText editGuess =  findViewById(R.id.editGuess);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        RelativeLayout mLinearLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this ,R.array.difficulty_list,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        startBtn.setOnClickListener(v -> {
            setDifficulty();
            difficultySpinner.setVisibility(View.INVISIBLE);
            startBtn.setVisibility(View.INVISIBLE);
            welcomeView.setVisibility(View.INVISIBLE);
            attemptView.setVisibility(View.VISIBLE);
            guessBtn.setVisibility(View.VISIBLE);
            guessRange.setVisibility(View.VISIBLE);
            guessRange.setMax(upperbound);
            //editGuess.setVisibility(View.VISIBLE);
            attemptView.setText(remainder);
        });

        returnBtn.setOnClickListener(v ->{
            difficultySpinner.setVisibility(View.VISIBLE);
            startBtn.setVisibility(View.VISIBLE);
            welcomeView.setVisibility(View.VISIBLE);
            attemptView.setVisibility(View.INVISIBLE);
            guessRange.setVisibility(View.INVISIBLE);
            continueBtn.setVisibility(View.INVISIBLE);
            returnBtn.setVisibility(View.INVISIBLE);
        });

        continueBtn.setOnClickListener(v ->{
            tries = tempTries;
            guessBtn.setVisibility(View.VISIBLE);
            continueBtn.setVisibility(View.INVISIBLE);
            returnBtn.setVisibility(View.INVISIBLE);
        });

        guessRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, String.valueOf(progressChanged), Toast.LENGTH_SHORT).show();
            }
        });

        guessBtn.setOnClickListener(v -> {
            final int guessVal = guessRange.getProgress();

            if(guessVal == randVal ){
                Toast.makeText(MainActivity.this, "WELL DONE, YOU'VE WON!", Toast.LENGTH_SHORT).show();
                endCurrentGame();
            } else if (guessVal < randVal && tries > 1){
                removeTries();
                if(tries <= 0){
                    return;
                }
                if(guessVal <= randVal / 2){
                    Toast.makeText(MainActivity.this, "COLD!", Toast.LENGTH_SHORT).show();
                    mLinearLayout.setBackgroundResource(R.mipmap.cold);
                }else {
                    Toast.makeText(MainActivity.this, "COLDER!", Toast.LENGTH_SHORT).show();
                }
            } else if(guessVal > randVal && tries > 1){
                removeTries();
                if(guessVal <= randVal * 2){
                    Toast.makeText(MainActivity.this, "HOT!", Toast.LENGTH_SHORT).show();
                    mLinearLayout.setBackgroundResource(R.mipmap.cold);
                }else {
                    Toast.makeText(MainActivity.this, "HOTTER!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rulesBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage(rules)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }


    public void removeTries() {
        tries--;
        remainder = "Attempt #" + tries;
        attemptView.setText(remainder);
        if (tries <= 0){
            Toast.makeText(MainActivity.this, "BETTER LUCK NEXT TIME!", Toast.LENGTH_SHORT).show();
            endCurrentGame();
        }
    }

    public void endCurrentGame(){
        String ans = "Answer: " + randVal;
        attemptView.setText(ans);
        continueBtn.setVisibility(View.VISIBLE);
        returnBtn.setVisibility(View.VISIBLE);
        guessBtn.setVisibility(View.INVISIBLE);
    }

    public void setDifficulty(){
        Random rand = new Random(); //instance of random class

        if (difficultySpinner.getSelectedItemPosition() == 0){
            // Beginner Difficulty
            tries = 5;
            upperbound = 9;
        }else if(difficultySpinner.getSelectedItemPosition() == 1){
            // Intermediate Difficulty
            tries = 10;
            upperbound = 49;
        }else if(difficultySpinner.getSelectedItemPosition() == 2){
            // Professional Difficulty
            tries = 15;
            upperbound = 99;
        }else{
            tries = 2;
            upperbound = 99;
        }
        remainder = "Attempt #" + tries;
        tempTries = tries;
        //generate random values from 1-30
        int int_random = rand.nextInt(upperbound);
        randVal = int_random + 1;
    }

}