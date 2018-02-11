package com.masmoudi.vache_taureau;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
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

    public enum GAME_MODE {
        SINGLE_MODE_DIGITS,
        SINGLE_MODE_LETTERS
    }

    private Context context;
    private TextView playerAnswer;
    private TextView opponentAnswer;

    private int numberToGuess;
    private ArrayList<String> possibleGuesses;
    private GAME_MODE gameMode;

    // PUBLIC methods
    public Game(Context context) {
        this.playerAnswer = null;
        this.opponentAnswer = null;
        this.context = context;
        this.possibleGuesses = new ArrayList<String>();

        // Fill possibleGuesses
        for(int i = 1023; i < 9877; i++) {
            if(isValidNumber(i)) {
                possibleGuesses.add(Integer.toString(i));
            }
        }
    }

    public void setPlayerAnswer(TextView playerAnswer) {
        this.playerAnswer = playerAnswer;
    }

    public void setOpponentAnswer(TextView opponentAnswer) {
        this.opponentAnswer = opponentAnswer;
    }

    public void backspaceAnswer() {
        String playerAnswerText = playerAnswer.getText().toString();

        playerAnswerText = removeLastLetterFromAnswer(playerAnswerText);
        playerAnswer.setText(playerAnswerText);
    }

    public void addLetter(String letter) {
        String playerAnswerText = playerAnswer.getText().toString();

        playerAnswerText = addLetterToAnswer(letter, playerAnswerText);
        playerAnswer.setText(playerAnswerText);
    }

    public void addDigit(String digit) {
        String playerAnswerText = playerAnswer.getText().toString();

        playerAnswerText = addDigitToAnswer(digit, playerAnswerText);
        playerAnswer.setText(playerAnswerText);
    }

    public void submitAnswer(ROUND round) {
        switch(round) {
            case DIGITS:
                submitDigitsAnswer();
                break;

            case LETTERS:
                submitLettersAnswer();
                break;

            default:
                break;
        }
    }
    
    public void submitLettersAnswer() {
        String sortedAnswer = sortAnswerLetters(getCleanAnswer(playerAnswer.getText().toString()));
        if(sortedAnswer.isEmpty()) {
            sortedAnswer = "Ø";
        }

        switch(gameMode) {
            case SINGLE_MODE_DIGITS:
                break;

            case SINGLE_MODE_LETTERS:
                if(sortedAnswer.equals("TTTT")) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("OPPONENT_WON", true);
                    context.startActivity(intent);
                } else {
                    String guess = opponentAnswer.getText().toString();
                    updatePossibleGuesses(guess, sortedAnswer);
                    GameActivity.appendAnswerToHistory(sortedAnswer, guess);

                    guess = guessNumber();
                    opponentAnswer.setText(guess);

                    resetPlayerAnswer();
                }
                break;

            default:
                break;
        }

    }

    public void submitDigitsAnswer() {
        String answerText = playerAnswer.getText().toString();
        if(isAnswerFull(answerText)) {
            String result = compareNumbers(answerText, Integer.toString(numberToGuess));
            if(!result.equals("TTTT")) {
                if(result.isEmpty()) {
                    result = "Ø";
                }
                opponentAnswer.setText(result);
                GameActivity.appendAnswerToHistory(answerText, result);

                switch (gameMode) {
                    case SINGLE_MODE_DIGITS:
                        // TODO : Do something
                        resetPlayerAnswer();
                        break;

                    default:
//                        switchToRound(ROUND.LETTERS);
                        break;
                }
            } else {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("PLAYER_WON", true);
                context.startActivity(intent);
            }
        } else {
            Toast.makeText(context, "Answer is incomplete", Toast.LENGTH_SHORT).show();
        }
    }

    public void startGame(GAME_MODE gameMode) {
        this.gameMode = gameMode;
        switch(gameMode) {
            case SINGLE_MODE_DIGITS:
                resetPlayerAnswer();
                resetOpponentAnswer();
                numberToGuess = generateRandomNumber();
                switchToRound(ROUND.DIGITS);
                Toast.makeText(context, "Single mode digits started", Toast.LENGTH_SHORT).show();
                break;

            case SINGLE_MODE_LETTERS:
                resetPlayerAnswer();
                String numberGuess = guessNumber();
                opponentAnswer.setText(numberGuess);
                switchToRound(ROUND.LETTERS);
                Toast.makeText(context, "Single mode letters started", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    // PRIVATE methods
    private void resetPlayerAnswer() {
        playerAnswer.setText("----");
    }

    private void resetOpponentAnswer() {
        opponentAnswer.setText("");
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
                GameActivity.attachDigitsKeyboard();
                break;

            case LETTERS:
                GameActivity.attachLettersKeyboard();
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

    /**
     * Update possibleGuesses ArrayList
     * @param digits opponent's suggested number
     * @param letters player's response. Must be clean answer (without dashes)
     */
    private void updatePossibleGuesses(String digits, String letters) {
        if(letters.equals("Ø")) {
            letters = "";
        }

        int i = 0;
        while(!possibleGuesses.isEmpty() && i < possibleGuesses.size()) {
            if(!letters.equals(compareNumbers(possibleGuesses.get(i), digits))) {
                possibleGuesses.remove(i);
            } else {
                i++;
            }
        }
        
        if(possibleGuesses.isEmpty()) {
            Toast.makeText(context, "You're cheating ! :p", Toast.LENGTH_SHORT).show();
        }
    }

    private String guessNumber() {
        Random numberGenerator = new Random();
        if(!possibleGuesses.isEmpty()) {
            int guessIndex = numberGenerator.nextInt(possibleGuesses.size());
            return possibleGuesses.get(guessIndex);
        } else {
            return "Ø";
        }
    }
}
