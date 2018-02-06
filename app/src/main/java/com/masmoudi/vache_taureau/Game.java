package com.masmoudi.vache_taureau;

import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.util.Random;

/**
 * Created by Mahmoud on 06/02/2018.
 */

public class Game {

    private static final int ANSWER_LENGTH = 4;

    private enum ROUND {
        LETTERS,
        DIGITS,
        NONE
    }

    private Context context;
    private TextView lettersAnswer;
    private TextView digitsAnswer;
    private GridLayout lettersLayout;
    private GridLayout digitsLayout;
    private int numberToGuess;

    // PUBLIC methods
    public Game(Context context) {
        this.lettersAnswer = null;
        this.digitsAnswer = null;
        this.lettersLayout = null;
        this.digitsLayout = null;
        this.context = context;
    }

    public void setDigitsAnswer(TextView digitsAnswer) {
        this.digitsAnswer = digitsAnswer;
    }

    public void setLettersLayout(GridLayout lettersLayout) {
        this.lettersLayout = lettersLayout;
    }

    public void setDigitsLayout(GridLayout digitsLayout) {
        this.digitsLayout = digitsLayout;
    }

    public void setLettersAnswer(TextView lettersAnswer) {
        this.lettersAnswer = lettersAnswer;
    }

    public void backspaceLetter() {
        String lettersAnswerText = lettersAnswer.getText().toString();

        lettersAnswerText = removeLastLetterFromAnswer(lettersAnswerText);
        lettersAnswer.setText(lettersAnswerText);
    }

    public void backspaceDigit() {
        String digitsAnswerText = digitsAnswer.getText().toString();

        digitsAnswerText = removeLastLetterFromAnswer(digitsAnswerText);
        digitsAnswer.setText(digitsAnswerText);
    }

    public void addLetter(String letter) {
        String lettersAnswerText = lettersAnswer.getText().toString();

        lettersAnswerText = addLetterToAnswer(letter, lettersAnswerText);
        lettersAnswer.setText(lettersAnswerText);
    }

    public void addDigit(String digit) {
        String digitsAnswerText = digitsAnswer.getText().toString();

        digitsAnswerText = addDigitToAnswer(digit, digitsAnswerText);
        digitsAnswer.setText(digitsAnswerText);
    }

    public void submitLettersAnswer() {
        String sortedAnswer = sortAnswerLetters(getCleanAnswer(lettersAnswer.getText().toString()));
        if(sortedAnswer.isEmpty()) {
            Toast.makeText(context, "Ø", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, sortedAnswer, Toast.LENGTH_SHORT).show();
        }
        switchToRound(ROUND.DIGITS);
    }

    public void submitDigitsAnswer() {
        String answerText = digitsAnswer.getText().toString();
        if(isAnswerFull(answerText)) {
            String result = compareNumbers(answerText, Integer.toString(numberToGuess));
            if(!result.equals("TTTT")) {
                Toast.makeText(context, result+"("+numberToGuess+")", Toast.LENGTH_SHORT).show();
                switchToRound(ROUND.LETTERS);
            } else {
                Toast.makeText(context, "You win :)", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Answer is incomplete", Toast.LENGTH_SHORT).show();
        }
    }

    public void startGame() {
        numberToGuess = generateRandomNumber();
        switchToRound(ROUND.LETTERS);
        Toast.makeText(context, "Game started ("+numberToGuess+")", Toast.LENGTH_SHORT).show();
    }

    // PRIVATE methods
    private void resetLettersAnswer() {
        String lettersAnswerText = "----";
        if(lettersAnswer != null) {
            lettersAnswer.setText(lettersAnswerText);
        }
    }

    private void resetDigitsAnswer() {
        String digitsAnswerText = "----";
        if(digitsAnswer != null) {
            digitsAnswer.setText(digitsAnswerText);
        }
    }

    private int getNbDashes(String str) {
        int dashIndex = str.indexOf("-");

        if (dashIndex >= 0) {
            return ANSWER_LENGTH - dashIndex;
        } else {
            return 0;
        }
    }

    private boolean isAnswerEmpty(String answer) {
        int nbDashes = getNbDashes(answer);
        if (nbDashes == ANSWER_LENGTH) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isAnswerFull(String answer) {
        int nbDashes = getNbDashes(answer);
        if (nbDashes == 0) {
            return true;
        } else {
            return false;
        }
    }

    private String getCleanAnswer(String answer) {
        int nbDashes = getNbDashes(answer);
        return answer.substring(0, answer.length() - nbDashes);
    }

    private String getFilledAnswer(String answer) {
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

    private String removeLastLetterFromAnswer(String answer) {
        int nbDashes = getNbDashes(answer);

        if (!isAnswerEmpty(answer)) {
            String newAnswer = getCleanAnswer(answer);
            newAnswer = newAnswer.substring(0, newAnswer.length() - 1);
            newAnswer = getFilledAnswer(newAnswer);

            return newAnswer;
        } else {
            Toast.makeText(context, "Answer is empty", Toast.LENGTH_SHORT).show();
            return answer;
        }
    }

    private String addLetterToAnswer(String letter, String answer) {
        if (!isAnswerFull(answer)) {
            String newAnswer = getCleanAnswer(answer);
            newAnswer += letter;
            newAnswer = getFilledAnswer(newAnswer);

            return newAnswer;
        } else {
            Toast.makeText(context, "Answer is full", Toast.LENGTH_SHORT).show();
            return answer;
        }
    }

    private String addDigitToAnswer(String digit, String answer) {
        if (!isAnswerFull(answer)) {
            String newAnswer = getCleanAnswer(answer);

            // Ignore the cases below :
            if (isAnswerEmpty(answer) && digit.equals("0")) {
                // 0 at the beginning encountered
                Toast.makeText(context, "Number should not start with 0", Toast.LENGTH_SHORT).show();
            } else if (answer.indexOf(digit) >= 0) {
                // duplicate digit encountered
                Toast.makeText(context, "Number already contains "+digit, Toast.LENGTH_SHORT).show();
            } else {
                newAnswer += digit;
            }

            newAnswer = getFilledAnswer(newAnswer);

            return newAnswer;
        } else {
            Toast.makeText(context, "Answer is already full", Toast.LENGTH_SHORT).show();
            return answer;
        }
    }

    private String sortAnswerLetters(String str) {
        String vPart = "";
        String tPart = "";
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == 'V') {
                vPart += "V";
            } else if(str.charAt(i) == 'T') {
                tPart += "T";
            }
        }

        return tPart+vPart;
    }

    private void switchToRound(ROUND round) {
        switch (round) {
            case DIGITS:
                for (int i = 0; i < digitsLayout.getChildCount(); i++) {
                    digitsLayout.getChildAt(i).setEnabled(true);
                }
                for (int i = 0; i < lettersLayout.getChildCount(); i++) {
                    lettersLayout.getChildAt(i).setEnabled(false);
                }
                resetDigitsAnswer();
                break;

            case LETTERS:
                for (int i = 0; i < digitsLayout.getChildCount(); i++) {
                    digitsLayout.getChildAt(i).setEnabled(false);
                }
                for (int i = 0; i < lettersLayout.getChildCount(); i++) {
                    lettersLayout.getChildAt(i).setEnabled(true);
                }
                resetLettersAnswer();
                break;

            default:
                break;
        }
    }

    private boolean isValidNumber(int number) {
        // Check bounds (and leading 0)
        if(number < 1023 || number > 9876) {
            return false;
        }

        String numberStr = Integer.toString(number);
        String digitToTest;
        String remainingDigits;
        for(int i = 0; i < numberStr.length()-1; i++) { // Stop at the before-the-last digit
            digitToTest = numberStr.substring(i, i+1);
            remainingDigits = numberStr.substring(i+1, numberStr.length());

            if(remainingDigits.indexOf(digitToTest) != -1) {
                return false;
            }
        }

        return true;
    }

    private int generateRandomNumber() {
        Random numberGenerator = new Random();
        int number;

        do {
            number = numberGenerator.nextInt(9876+1 - 1023) + 1023;
        }while(!isValidNumber(number));

        return number;
    }

    private String compareNumbers(String testNumber, String secretNumber) {
        int currentCharComparison;
        String tPart = "";
        String vPart = "";

        for(int i = 0; i < testNumber.length(); i++) {
            currentCharComparison = secretNumber.indexOf(testNumber.charAt(i));
            if(currentCharComparison == i) {
                tPart += "T";
            } else if(currentCharComparison != i && currentCharComparison != -1) {
                vPart += "V";
            }
        }

        return tPart+vPart;
    }
}
