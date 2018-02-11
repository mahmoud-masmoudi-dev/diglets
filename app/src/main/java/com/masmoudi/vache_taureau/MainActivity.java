package com.masmoudi.vache_taureau;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button singleModeDigitsButton = (Button) findViewById(R.id.singleModeDigitsButton);
        Button singleModeLettersButton = (Button) findViewById(R.id.singleModeLettersButton);

        Intent intent = getIntent();
        boolean playerWon = intent.getBooleanExtra("PLAYER_WON", false);
        boolean opponentWon = intent.getBooleanExtra("OPPONENT_WON", false);

        if(playerWon) {
            Snackbar.make(singleModeDigitsButton, "You win ;)", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show();
        }
        if (opponentWon) {
            Snackbar.make(singleModeDigitsButton, "Opponent wins :(", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show();
        }

        singleModeDigitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("GAME_MODE", Game.GAME_MODE.values()[0]);
                startActivity(intent);
            }
        });

        singleModeLettersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("GAME_MODE", Game.GAME_MODE.values()[1]);
                startActivity(intent);
            }
        });
    }
}
