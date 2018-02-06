package com.masmoudi.vache_taureau;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView lettersAnswer;
    private TextView digitsAnswer;
    private GridLayout lettersLayout;
    private GridLayout digitsLayout;
    private Game mainGame;

    public void lettersBackspaceClicked(View view) {
        mainGame.backspaceLetter();
    }

    public void digitsBackspaceClicked(View view) {
        mainGame.backspaceDigit();
    }

    public void lettersOkClicked(View view) {
        mainGame.submitLettersAnswer();
    }

    public void digitsOkClicked(View view) {
        mainGame.submitDigitsAnswer();
    }

    public void letterClicked(View view) {
        TextView textView = (TextView) view;
        String letter = textView.getTag().toString();

        mainGame.addLetter(letter);
    }

    public void digitClicked(View view) {
        TextView textView = (TextView) view;
        String digit = textView.getTag().toString();

        mainGame.addDigit(digit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lettersAnswer = (TextView) findViewById(R.id.lettersAnswer);
        digitsAnswer = (TextView) findViewById(R.id.digitsAnswer);
        lettersLayout = (GridLayout) findViewById(R.id.lettersGame);
        digitsLayout = (GridLayout) findViewById(R.id.digitsGame);

        mainGame = new Game(getApplicationContext());
        mainGame.setLettersAnswer(lettersAnswer);
        mainGame.setDigitsAnswer(digitsAnswer);
        mainGame.setLettersLayout(lettersLayout);
        mainGame.setDigitsLayout(digitsLayout);

        mainGame.startGame();
    }
}
