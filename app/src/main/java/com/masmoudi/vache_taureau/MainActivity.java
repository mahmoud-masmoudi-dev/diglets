package com.masmoudi.vache_taureau;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public TextView lettersAnswer;

    public void lettersBackspaceClicked(View view) {
        TextView textView = (TextView)view;
        lettersAnswer = (TextView)findViewById(R.id.lettersAnswer);
        String lettersAnswerText = lettersAnswer.getText().toString();
        Toast.makeText(this, lettersAnswerText, Toast.LENGTH_SHORT).show();
        if(!lettersAnswerText.isEmpty()) {
            lettersAnswerText = lettersAnswerText.substring(0, lettersAnswerText.length() - 1);
            lettersAnswer.setText(lettersAnswerText);
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
