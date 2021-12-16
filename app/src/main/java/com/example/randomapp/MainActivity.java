
package com.example.randomapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    public int randVal;
    public int tries;
    private Spinner difficultySpinner;
    public String rules = "RULES:" +
            "\n Press Start to generate a random number and begin playing." +
            "\n The number of attempts are assigned by the difficulty setting" +
            "\n Move the seekbar to choose a number." +
            "\n Tap the 'ENTER' button to make that guess." +
            "\n You get 'HOTTER!' when your guess is more the value" +
            "\n You get 'COLDER!' when your guess is less the value" +
            "\n Guess the correct value to win" +
            "\n\n GOOD LUCK!!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        difficultySpinner  = findViewById(R.id.spinner);
        final ImageButton rulesBtn = findViewById(R.id.instructionsBtn);

        final TextView welcomeView = findViewById(R.id.welcomeView);
        final TextView attemptView = findViewById(R.id.attemptView);
        final SeekBar guessRange = findViewById(R.id.guessControl);

        final Button startBtn = findViewById(R.id.startBtn);
        final AppCompatButton guessBtn = findViewById(R.id.guessBtn);

        //final EditText editGuess =  findViewById(R.id.editGuess);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        /*RelativeLayout mLinearLayout = (RelativeLayout) findViewById(R.id.relativeLayoutID);*/

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this ,R.array.difficulty_list,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        startBtn.setOnClickListener(v -> {
            setDifficulty();
            difficultySpinner.setVisibility(View.INVISIBLE);
            startBtn.setVisibility(View.INVISIBLE);
            welcomeView.setVisibility(View.INVISIBLE);
            guessBtn.setVisibility(View.VISIBLE);
            guessRange.setVisibility(View.VISIBLE);
            //editGuess.setVisibility(View.VISIBLE);
            attemptView.setText(String.valueOf(tries));
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
                Toast.makeText(MainActivity.this, "Number: " + progressChanged, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        guessBtn.setOnClickListener(v -> {
            final int guessVal = guessRange.getProgress();

            try{
                if(guessVal == randVal){
                    Toast.makeText(MainActivity.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                } else if (guessVal < randVal){
                    removeTries();
                    if(guessVal <= randVal / 2){
                        Toast.makeText(MainActivity.this, "COLD!", Toast.LENGTH_SHORT).show();
                        //mLinearLayout.setBackgroundResource(R.mipmap.cold);
                    }else {
                        Toast.makeText(MainActivity.this, "COLDER!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    removeTries();
                    if(guessVal <= randVal * 2){
                        Toast.makeText(MainActivity.this, "HOT!", Toast.LENGTH_SHORT).show();
                        //mLinearLayout.setBackgroundResource(R.mipmap.cold);
                    }else {
                        Toast.makeText(MainActivity.this, "HOTTER!", Toast.LENGTH_SHORT).show();
                    }
                }
                attemptView.setText(String.valueOf(tries));
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Integer Required!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                e.printStackTrace();
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
        if (tries <= 0){
            Toast.makeText(MainActivity.this, "YOU LOSE!", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
    }

    public void setDifficulty(){
        Random rand = new Random(); //instance of random class
        int upperbound;

        if (difficultySpinner.getSelectedItemPosition() == 0){
            // Beginner Difficulty
            tries = 5;
            upperbound = 10;
        }else if(difficultySpinner.getSelectedItemPosition() == 1){
            // Intermediate Difficulty
            tries = 10;
            upperbound = 50;
        }else{
            // Professional Difficulty
            tries = 15;
            upperbound = 100;
        }
        //generate random values from 1-30
        int int_random = rand.nextInt(upperbound);
        randVal = int_random + 1;
    }
}