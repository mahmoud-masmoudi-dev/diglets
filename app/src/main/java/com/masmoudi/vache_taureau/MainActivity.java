package com.masmoudi.vache_taureau;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int ANSWER_LENGTH = 4;
    public TextView lettersAnswer;
    public TextView digitsAnswer;

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

    public void lettersOkClicked(View view) {
        TextView textView = (TextView) view;
        Toast.makeText(this, "lettersOkClicked", Toast.LENGTH_SHORT).show();
        resetLettersAnswer();
    }

    public void digitsOkClicked(View view) {
        TextView textView = (TextView) view;
        Toast.makeText(this, "digitsOkClicked", Toast.LENGTH_SHORT).show();
        resetDigitsAnswer();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetLettersAnswer();
        resetDigitsAnswer();
    }
}
