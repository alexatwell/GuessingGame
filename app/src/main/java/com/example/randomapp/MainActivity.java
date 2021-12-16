
package com.example.randomapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public int randVal;
    public int guessVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText guess =  findViewById(R.id.guess);
        final AppCompatButton guessBtn = findViewById(R.id.guessBtn);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        guessBtn.setOnClickListener(v -> {

            //progressDialog.show();

            final String guessText = guess.getText().toString();

            if (guessText.isEmpty()){
                Toast.makeText(MainActivity.this, "Entry Required!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {
                try{
                    guessVal = Integer.parseInt(guessText);
                    guessValue();
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Integer Required!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });

    }

    public void generateButton(View view) {

        Random rand = new Random(); //instance of random class
        int upperbound = 30;
        //generate random values from 1-30
        int int_random = rand.nextInt(upperbound);
        //double double_random=rand.nextDouble();
        //float float_random=rand.nextFloat();
        randVal = int_random + 1;
    }


    public void guessValue() {
        if(guessVal == randVal){
            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
        } else if (guessVal < randVal){
            Toast.makeText(MainActivity.this, "Too Cold", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Too Hot", Toast.LENGTH_SHORT).show();
        }
    }
}