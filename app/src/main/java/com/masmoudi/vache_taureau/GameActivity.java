package com.masmoudi.vache_taureau;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    // Attributes
    private View drawer;
    private boolean isDrawerOpened = false;

    private TextView playerAnswer;
    private TextView opponentAnswer;
    private Game mainGame;
    private ListView listView;
    private static ArrayList<String> arrayList;
    private static ArrayAdapter<String> arrayAdapter;

    public static View digitsKeyboard;
    public static View lettersKeyboard;
    public static View keyboardLayout;

    // Methods
    public void lettersBackspaceClicked(View view) {
        mainGame.backspaceAnswer();
    }

    public void digitsBackspaceClicked(View view) {
        mainGame.backspaceAnswer();
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

    public static void attachDigitsKeyboard() {
        // Remove wichever parent of digitsKeyboard
        ViewParent parent = digitsKeyboard.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(digitsKeyboard);
        }
        // Empty keyboardLayout to accept the new keyboard
        ((ViewGroup)keyboardLayout).removeAllViews();
        // Attach the new keyboard to keyboardLayout
        ((ViewGroup)keyboardLayout).addView(digitsKeyboard);
    }

    public static void attachLettersKeyboard() {
        // Remove wichever parent of lettersKeyboard
        ViewParent parent = lettersKeyboard.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(lettersKeyboard);
        }
        // Empty keyboardLayout to accept the new keyboard
        ((ViewGroup)keyboardLayout).removeAllViews();
        // Attach the new keyboard to keyboardLayout
        ((ViewGroup)keyboardLayout).addView(lettersKeyboard);
    }

    public static void appendAnswerToHistory(String answer) {
        arrayList.add(answer);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        drawer = findViewById(R.id.history_drawer);
        ImageView tab = (ImageView) findViewById(R.id.history_drawer_tab);
        drawer.setTranslationX(-500f);

        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDrawerOpened) {
                    isDrawerOpened = true;
                    drawer.animate().translationXBy(500f).setDuration(500);
                } else {
                    isDrawerOpened = false;
                    drawer.animate().translationXBy(-500f).setDuration(500);
                }
            }
        });

        listView = (ListView) findViewById(R.id.history_drawer_list);
        arrayList = new ArrayList<String>();
        arrayList.add("Example");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        arrayAdapter.setNotifyOnChange(true);

        listView.setAdapter(arrayAdapter);

        Intent intent = getIntent();
        Game.GAME_MODE gameMode = (Game.GAME_MODE) intent.getSerializableExtra("GAME_MODE");

        playerAnswer = (TextView) findViewById(R.id.playerAnswer);
        opponentAnswer = (TextView) findViewById(R.id.opponentAnswer);

        digitsKeyboard = findViewById(R.id.digitsKeyboard);
        lettersKeyboard = findViewById(R.id.lettersKeyboard);
        keyboardLayout = findViewById(R.id.keyboardLayout);

        mainGame = new Game(getApplicationContext());
        mainGame.setPlayerAnswer(playerAnswer);
        mainGame.setOpponentAnswer(opponentAnswer);

        switch (gameMode) {
            case SINGLE_MODE_DIGITS:
                mainGame.startGame(Game.GAME_MODE.SINGLE_MODE_DIGITS);
                break;

            case SINGLE_MODE_LETTERS:
                mainGame.startGame(Game.GAME_MODE.SINGLE_MODE_LETTERS);
                break;

            default:
                break;
        }
    }
}
