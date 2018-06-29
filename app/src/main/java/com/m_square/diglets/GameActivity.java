package com.m_square.diglets;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity
        implements DigitsFragment.OnDigitsFragmentInteractionListener,
        LettersFragment.OnLettersFragmentInteractionListener {
    // Attributes
    private View drawer;
    private boolean isDrawerOpened = false;

    private TextView playerAnswer;
    private TextView opponentAnswer;
    private Game mainGame;
    private ListView listView;
    private static ArrayList<String> playerAnswers;
    private static ArrayList<String> opponentAnswers;
    private static CustomAdapter customAdapter;

    // Methods
    private static final GameActivity ourInstance = new GameActivity();

    public static GameActivity getInstance() {
        return ourInstance;
    }

    public GameActivity() {
    }

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

    public void attachDigitsKeyboard() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        DigitsFragment fragment = new DigitsFragment();
        fragmentTransaction.replace(R.id.keyboardLayout, fragment);
        fragmentTransaction.commit();
    }

    public void attachLettersKeyboard() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LettersFragment fragment = new LettersFragment();
        fragmentTransaction.replace(R.id.keyboardLayout, fragment);
        fragmentTransaction.commit();
    }

    public static void appendAnswerToHistory(String playerAnswer, String opponentAnswer) {
        playerAnswers.add(playerAnswer);
        opponentAnswers.add(opponentAnswer);
        customAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.m_square.diglets.R.layout.activity_game);

        drawer = findViewById(com.m_square.diglets.R.id.history_drawer);
        ImageView tab = (ImageView) findViewById(com.m_square.diglets.R.id.history_drawer_tab);
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

        listView = (ListView) findViewById(com.m_square.diglets.R.id.history_drawer_list);

        playerAnswers = new ArrayList<String>();
        opponentAnswers = new ArrayList<String>();

        customAdapter = new CustomAdapter(this, playerAnswers, opponentAnswers);
        customAdapter.setNotifyOnChange(true);

        listView.setAdapter(customAdapter);

        Intent intent = getIntent();
        Game.GAME_MODE gameMode = (Game.GAME_MODE) intent.getSerializableExtra("GAME_MODE");

        playerAnswer = (TextView) findViewById(com.m_square.diglets.R.id.playerAnswer);
        opponentAnswer = (TextView) findViewById(com.m_square.diglets.R.id.opponentAnswer);

        mainGame = new Game(getApplicationContext());
        mainGame.setPlayerAnswer(playerAnswer);
        mainGame.setOpponentAnswer(opponentAnswer);

        switch (gameMode) {
            case SINGLE_MODE_DIGITS:
                mainGame.startGame(Game.GAME_MODE.SINGLE_MODE_DIGITS);
                attachDigitsKeyboard();
                break;

            case SINGLE_MODE_LETTERS:
                mainGame.startGame(Game.GAME_MODE.SINGLE_MODE_LETTERS);
                attachLettersKeyboard();
                break;

            default:
                break;
        }

        // Make it fullscreen
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    @Override
    public void onDigitsInteraction(Uri uri) {

    }

    @Override
    public void onLettersInteraction(Uri uri) {

    }
}
