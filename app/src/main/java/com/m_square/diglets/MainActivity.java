package com.m_square.diglets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.m_square.diglets.R.layout.activity_main);

        Button singleModeDigitsButton = (Button) findViewById(com.m_square.diglets.R.id.singleModeDigitsButton);
        Button singleModeLettersButton = (Button) findViewById(com.m_square.diglets.R.id.singleModeLettersButton);

        Intent intent = getIntent();
        boolean playerWon = intent.getBooleanExtra("PLAYER_WON", false);
        boolean opponentWon = intent.getBooleanExtra("OPPONENT_WON", false);

        if(playerWon) {
            Toast.makeText(this, "You win ;)", Toast.LENGTH_SHORT).show();
        }
        if (opponentWon) {
            Toast.makeText(this, "Opponent wins :(", Toast.LENGTH_SHORT).show();
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

        // Make it fullscreen
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}
