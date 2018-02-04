package com.masmoudi.vache_taureau;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public enum GAME {
        LETTERS,
        DIGITS,
        NONE
    }

    public static final int ANSWER_LENGTH = 4;
    public TextView lettersAnswer;
    public TextView digitsAnswer;
    public GAME game = GAME.NONE;

    public void resetLettersAnswer() {
        lettersAnswer = (TextView) findViewById(R.id.lettersAnswer);
        String lettersAnswerText = "----";
        lettersAnswer.setText(lettersAnswerText);
    }

    public void resetDigitsAnswer() {
        digitsAnswer = (TextView) findViewById(R.id.digitsAnswer);
        String digitsAnswerText = "----";
        digitsAnswer.setText(digitsAnswerText);
    }

    /**
     * This supposes that str is a ANSWER_LENGTH-letter string with possible trailing dashes
     *
     * @param str string to check
     * @return number of trailing dashes
     */
    public int getNbDashes(String str) {
        int dashIndex = str.indexOf("-");

        if (dashIndex >= 0) {
            return ANSWER_LENGTH - dashIndex;
        } else {
            return 0;
        }
    }

    public boolean isAnswerEmpty(String answer) {
        int nbDashes = getNbDashes(answer);
        if (nbDashes == ANSWER_LENGTH) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAnswerFull(String answer) {
        int nbDashes = getNbDashes(answer);
        if (nbDashes == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove dashes from the answer string
     * This supposes that answer is a ANSWER_LENGTH-letter string with possible trailing dashes
     *
     * @param answer
     * @return answer string without trailing dashes
     */
    public String getCleanAnswer(String answer) {
        int nbDashes = getNbDashes(answer);
        return answer.substring(0, answer.length() - nbDashes);
    }

    public String getFilledAnswer(String answer) {
        int nbDashes = getNbDashes(answer);
        String filledAnswer = answer;

        if (answer.length() < ANSWER_LENGTH && nbDashes == 0) {
            int nbDashesToAdd = ANSWER_LENGTH - answer.length();

            for (int i = 0; i < nbDashesToAdd; i++) {
                filledAnswer += "-";
            }
        }

        return filledAnswer;
    }

    public String removeLastLetterFromAnswer(String answer) {
        int nbDashes = getNbDashes(answer);

        if (!isAnswerEmpty(answer)) {
            String newAnswer = getCleanAnswer(answer);
            newAnswer = newAnswer.substring(0, newAnswer.length() - 1);
            newAnswer = getFilledAnswer(newAnswer);

            return newAnswer;
        } else {
            Toast.makeText(this, "Answer is already empty", Toast.LENGTH_SHORT).show();
            return answer;
        }
    }

    public String addLetterToAnswer(String letter, String answer) {
        if (!isAnswerFull(answer)) {
            String newAnswer = getCleanAnswer(answer);
            newAnswer += letter;
            newAnswer = getFilledAnswer(newAnswer);

            return newAnswer;
        } else {
            Toast.makeText(this, "Answer is already full", Toast.LENGTH_SHORT).show();
            return answer;
        }
    }

    public String addDigitToAnswer(String digit, String answer) {
        if (!isAnswerFull(answer)) {
            String newAnswer = getCleanAnswer(answer);

            // Ignore the cases below :
            if (isAnswerEmpty(answer) && digit.equals("0")) {
                // 0 at the beginning encountered
                Toast.makeText(this, "Number should not start with 0", Toast.LENGTH_SHORT).show();
            } else if (answer.indexOf(digit) >= 0) {
                // duplicate digit encountered
                Toast.makeText(this, "Number already contains " + digit, Toast.LENGTH_SHORT).show();
            } else {
                newAnswer += digit;
            }

            newAnswer = getFilledAnswer(newAnswer);

            return newAnswer;
        } else {
            return answer;
        }
    }

    public void lettersBackspaceClicked(View view) {
        TextView textView = (TextView) view;
        lettersAnswer = (TextView) findViewById(R.id.lettersAnswer);
        String lettersAnswerText = lettersAnswer.getText().toString();

        lettersAnswerText = removeLastLetterFromAnswer(lettersAnswerText);
        lettersAnswer.setText(lettersAnswerText);
    }

    public void digitsBackspaceClicked(View view) {
        TextView textView = (TextView) view;
        digitsAnswer = (TextView) findViewById(R.id.digitsAnswer);
        String digitsAnswerText = digitsAnswer.getText().toString();

        digitsAnswerText = removeLastLetterFromAnswer(digitsAnswerText);
        digitsAnswer.setText(digitsAnswerText);
    }

    public String sortAnswerLetters(String str) {
        String vPart = "";
        String tPart = "";
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == 'V') {
                vPart += "V";
            } else if(str.charAt(i) == 'T') {
                tPart += "T";
            }
        }

        return vPart+tPart;
    }

    public void submitLettersAnswer() {
        TextView answer = (TextView)findViewById(R.id.lettersAnswer);
        String sortedAnswer = sortAnswerLetters(getCleanAnswer(answer.getText().toString()));
        Toast.makeText(this, sortedAnswer, Toast.LENGTH_SHORT).show();
        switchToGame(GAME.DIGITS);
    }

    public void submitDigitsAnswer() {
        TextView answer = (TextView)findViewById(R.id.digitsAnswer);
        if(isAnswerFull(answer.getText().toString())) {
            Toast.makeText(this, answer.getText().toString(), Toast.LENGTH_SHORT).show();
            switchToGame(GAME.LETTERS);
        } else {
            Toast.makeText(this, "Answer is incomplete", Toast.LENGTH_SHORT).show();
        }
    }

    public void lettersOkClicked(View view) {
        submitLettersAnswer();
    }

    public void digitsOkClicked(View view) {
        submitDigitsAnswer();
    }

    public void letterClicked(View view) {
        TextView textView = (TextView) view;
        String tag = textView.getTag().toString();

        lettersAnswer = (TextView) findViewById(R.id.lettersAnswer);
        String lettersAnswerText = lettersAnswer.getText().toString();

        lettersAnswerText = addLetterToAnswer(tag, lettersAnswerText);
        lettersAnswer.setText(lettersAnswerText);
    }

    public void digitClicked(View view) {
        TextView textView = (TextView) view;
        String tag = textView.getTag().toString();

        digitsAnswer = (TextView) findViewById(R.id.digitsAnswer);
        String digitsAnswerText = digitsAnswer.getText().toString();

        digitsAnswerText = addDigitToAnswer(tag, digitsAnswerText);
        digitsAnswer.setText(digitsAnswerText);

    }

    public void switchToGame(GAME game) {
        GridLayout layout;
        switch (game) {
            case DIGITS:
                layout = (GridLayout) findViewById(R.id.digitsGame);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    layout.getChildAt(i).setEnabled(true);
                }
                layout = (GridLayout) findViewById(R.id.lettersGame);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    layout.getChildAt(i).setEnabled(false);
                }
                resetDigitsAnswer();
                break;

            case LETTERS:
                layout = (GridLayout) findViewById(R.id.digitsGame);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    layout.getChildAt(i).setEnabled(false);
                }
                layout = (GridLayout) findViewById(R.id.lettersGame);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    layout.getChildAt(i).setEnabled(true);
                }
                resetLettersAnswer();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchToGame(GAME.LETTERS);
    }
}
