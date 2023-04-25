package edu.wpi.tacticaltritons.auth;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PasswordGenerator {
    protected static final char[] ACCEPTED_SYMBOLS = {'!', '@', '#', '$', '%', '^', '&', '*', '?'};
    protected static final char[] ACCEPTED_ALPHABET_LOWER = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    protected static final char[] ACCEPTED_ALPHABET_UPPER = new String(ACCEPTED_ALPHABET_LOWER).toUpperCase().toCharArray();
    protected static final char[] ACCEPTED_NUMBERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    protected static final String lowerCaseRegex = "[a-z]";
    protected static final String upperCaseRegex = "[A-Z]";
    protected static final String symbolsRegex = "[!@#$^%?&*]";
    protected static final String digitsRegex = "[\\d]";

    @Setter protected static int passwordLength = 16;
    @Setter protected static boolean upperChars = true;
    @Setter protected static int upperCharsMax = 6;
    @Setter protected static boolean symbols = true;
    @Setter protected static int symbolsMax = 6;
    @Setter protected static boolean numbers = true;
    @Setter protected static int numbersMax = 6;
    @Setter protected static boolean duplicates = false;
    @Setter protected static boolean sequences = false;
    @Setter protected static int sequenceLength = 4;
    @Setter protected static boolean overwriteMax = false;

    public static String generatePassword(){
        StringBuilder password = new StringBuilder();
        List<String> types = new ArrayList<>();
        types.add("lower");
        if(upperChars) types.add("upper");
        if(symbols) types.add("symbols");
        if(numbers) types.add("numbers");

        List<Character> passwordLowerChars = new ArrayList<>();
        List<Character> passwordUpperChars = new ArrayList<>();
        List<Character> passwordSymbols = new ArrayList<>();
        List<Character> passwordNumbers = new ArrayList<>();

        while(password.length() != passwordLength){
            int next = new Random().nextInt(types.size()) + 1;
            switch(next){
                case 1 -> { //lower
                    char nextChar = generateChar(passwordLowerChars, ACCEPTED_ALPHABET_LOWER, password.toString());
                    passwordLowerChars.add(nextChar);
                    password.append(nextChar);
                }
                case 2 -> { //upper or symbols or numbers
                    switch (types.get(1)) {
                        case "upper" -> {
                            if(passwordUpperChars.size() >= upperCharsMax) break;
                            char nextChar = generateChar(passwordUpperChars, ACCEPTED_ALPHABET_UPPER, password.toString());
                            passwordUpperChars.add(nextChar);
                            password.append(nextChar);
                        }
                        case "symbols" -> {
                            if(passwordSymbols.size() >= symbolsMax) break;
                            char nextChar = generateChar(passwordSymbols, ACCEPTED_SYMBOLS, password.toString());
                            passwordSymbols.add(nextChar);
                            password.append(nextChar);
                        }
                        case "numbers" -> {
                            if (passwordNumbers.size() >= numbersMax) break;
                            char nextChar = generateChar(passwordNumbers, ACCEPTED_NUMBERS, password.toString());
                            passwordNumbers.add(nextChar);
                            password.append(nextChar);
                        }
                    }
                }
                case 3 -> { //symbols or numbers
                    switch (types.get(2)) {
                        case "symbols" -> {
                            if(passwordSymbols.size() >= symbolsMax) break;
                            char nextChar = generateChar(passwordSymbols, ACCEPTED_SYMBOLS, password.toString());
                            passwordSymbols.add(nextChar);
                            password.append(nextChar);
                        }
                        case "numbers" -> {
                            if (passwordNumbers.size() >= numbersMax) break;
                            char nextChar = generateChar(passwordNumbers, ACCEPTED_NUMBERS, password.toString());
                            passwordNumbers.add(nextChar);
                            password.append(nextChar);
                        }
                    }
                }
                case 4 -> { //numbers
                    if (passwordNumbers.size() >= numbersMax) break;
                    char nextChar = generateChar(passwordNumbers, ACCEPTED_NUMBERS, password.toString());
                    passwordNumbers.add(nextChar);
                    password.append(nextChar);
                }
            }
        }
        return password.toString();
    }

    private static Character generateChar(List<Character> charList, char[] acceptableList, String password){
        char nextChar =  acceptableList[new Random().nextInt(acceptableList.length)];
        if(!duplicates && acceptableList.length == charList.size()) {
            charList.clear();
        }
        if(!sequences){
            List<Character> sequenceList = new ArrayList<>();
            sequenceList.add(nextChar);
            if(!duplicates){
                while(charList.contains(nextChar) || Validator.containsSequence(password, password.length(), sequenceList)){
                    nextChar = acceptableList[new Random().nextInt(acceptableList.length)];
                    sequenceList.set(0, nextChar);
                }
            }
            while(Validator.containsSequence(password, password.length(), sequenceList)){
                nextChar = acceptableList[new Random().nextInt(acceptableList.length)];
                sequenceList.set(0, nextChar);
            }
        }
        if(!duplicates){
            while(charList.contains(nextChar)){
                nextChar = acceptableList[new Random().nextInt(acceptableList.length)];
            }
        }
        return nextChar;
    }

    public static EventHandler<ActionEvent> generatePasswordEvent(
            MFXPasswordField passwordField,
            MFXPasswordField confirmPasswordField){
        return event -> {
            String generatedPassword = PasswordGenerator.generatePassword();
            passwordField.setText(generatedPassword);
            passwordField.setShowPassword(true);
            confirmPasswordField.setText(generatedPassword);
            confirmPasswordField.setShowPassword(true);
        };
    }
}
