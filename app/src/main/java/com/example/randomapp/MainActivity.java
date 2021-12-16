
package com.example.randomapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public int randVal;
    public int guessVal;
    public int tries = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AppCompatButton guessBtn = findViewById(R.id.guessBtn);
        final Button randomBtn = (Button) findViewById(R.id.randomBtn);
        final TextView guessView = findViewById(R.id.guessView);
        final TextView welcomeView = findViewById(R.id.welcomeView);
        final EditText editGuess =  findViewById(R.id.editGuess);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        randomBtn.setOnClickListener(v -> {

            //progressDialog.show();

            Random rand = new Random(); //instance of random class
            int upperbound = 30;
            //generate random values from 1-30
            int int_random = rand.nextInt(upperbound);
            //double double_random=rand.nextDouble();
            //float float_random=rand.nextFloat();
            randVal = int_random + 1;

            randomBtn.setVisibility(View.INVISIBLE);
            welcomeView.setVisibility(View.INVISIBLE);
            guessBtn.setVisibility(View.VISIBLE);
            editGuess.setVisibility(View.VISIBLE);
            guessView.setText(String.valueOf(tries));
        });

        guessBtn.setOnClickListener(v -> {

            //progressDialog.show();

            final String guessText = editGuess.getText().toString();

            if (guessText.isEmpty()){
                Toast.makeText(MainActivity.this, "Entry Required!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {
                try{
                    guessVal = Integer.parseInt(guessText);
                    guessValue();
                    guessView.setText(String.valueOf(tries));
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Integer Required!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });

    }


    public void guessValue() {
        if(guessVal == randVal){
            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
        } else if (guessVal < randVal){
            Toast.makeText(MainActivity.this, "Too Cold", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Too Hot", Toast.LENGTH_SHORT).show();
        }
        tries--;
        if (tries <= 0){
            System.exit(0);
        }
    }
}