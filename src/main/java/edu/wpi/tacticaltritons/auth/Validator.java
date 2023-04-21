package edu.wpi.tacticaltritons.auth;

import edu.wpi.tacticaltritons.database.Login;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.commons.validator.routines.EmailValidator;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static edu.wpi.tacticaltritons.auth.PasswordGenerator.*;

public class Validator {
    public enum PasswordStrength {Weak, Basic, Good, Strong}

    // 1 valid
    // 2 duplicate email
    @SuppressWarnings("all")
    public static int validate(List<Login> users, String email) throws SQLException {
        if(users != null && users.stream().map(Login::getEmail).collect(Collectors.toList()).contains(email)) return 2;
        return 1;
    }

    public static boolean containsSequence(String password){
        for(int i = 0; i < password.length() - 1; i++){
            String np = i != 0 ? password.substring(0, i) + password.substring(i + 1) : password.substring(1);
            List<Character> memo = new ArrayList<>();
            memo.add(password.charAt(i));
            if(containsSequence(np, i, memo)) {
                return true;
            }
        }
        return false;
    }
    public static boolean containsSequence(String password, int index, List<Character> memo){
        if(memo.size() > sequenceLength) return false;
        if(index >= password.length()) return false;
        if(index < 0) return false;
        char[] pwCharFamily = containingFamily(String.valueOf(password.charAt(index)));
        char[] nextCharFamily = containingFamily(String.valueOf(memo.get(0)));
        assert nextCharFamily != null && pwCharFamily != null;
        if(!Arrays.equals(nextCharFamily, pwCharFamily) || Arrays.equals(nextCharFamily, ACCEPTED_SYMBOLS)) return false;
        memo.add(password.charAt(index));
        if(memo.size() == sequenceLength){
            for(int i = 0; i < nextCharFamily.length; i++){
                if(memo.get(0) == nextCharFamily[i]){
                    if(i + 1 >= nextCharFamily.length || i - 1 < 0) return false;
                    if(memo.get(1) == nextCharFamily[i + 1]) {
                        for (int j = 2; j < memo.size(); j++) {
                            if (i + j >= nextCharFamily.length) return false;
                            if (memo.get(j) != nextCharFamily[i + j]) return false;
                        }
                        return true;
                    }
                    else if(memo.get(1) == nextCharFamily[i - 1]){
                        for (int j = 2; j < memo.size(); j++) {
                            if (i - j < 0) return false;
                            if (memo.get(j) != nextCharFamily[i - j]) return false;
                        }
                        return true;
                    }
                    return false;
                }
            }
        }
        return containsSequence(password,index + 1, memo) || containsSequence(password,index - 1, memo);
    }

    private static char[] containingFamily(String c){
        return Pattern.matches(lowerCaseRegex, c) ? ACCEPTED_ALPHABET_LOWER :
                Pattern.matches(upperCaseRegex, c) ? ACCEPTED_ALPHABET_UPPER :
                        Pattern.matches(symbolsRegex, c) ? ACCEPTED_SYMBOLS :
                                Pattern.matches(digitsRegex, c) ? ACCEPTED_NUMBERS : null;
    }
    public static boolean containsDuplicate(String password){
        for(int i = 0; i < password.length() - 1; i++){
            String np = i != 0 ? password.substring(0, i) + password.substring(i + 1) : password.substring(1);
            if(containsDuplicate(password.charAt(i), np)){
                return true;
            }
        }
        return false;
    }
    private static boolean containsDuplicate(char nextChar, String password){
        List<Character> passwordList = new ArrayList<>();
        char[] nextCharFamily = containingFamily(String.valueOf(nextChar));
        assert nextCharFamily != null;
        for(int i = 0; i < password.length(); i++){
            char[] passwordFamily = containingFamily(String.valueOf(password.charAt(i)));
            assert passwordFamily != null;
            if(Arrays.equals(passwordFamily, nextCharFamily) && !passwordList.contains(password.charAt(i))){
                passwordList.add(password.charAt(i));
                if(passwordList.contains(nextChar)) return true;
            }
            else if(passwordList.contains(password.charAt(i))) return true;
        }
        return false;
    }

    public static PasswordStrength computePasswordStrength(int length, boolean[] parameters){
        //weights
        //LowerCase += 10
        //UpperCase += 20
        //Digit += 30
        //Symbol += 45
        //Duplicate -= 5
        //Sequence -= 10
        //Contains Password -= 40
        //Length
        //less than 4 -= 100
        //greater than or equal to 4 += 10
        //less than or equal to 6 += 15
        //less than or equal to 8 += 25
        //less than or equal to 10 += 40
        //less than or equal to 12 += 55
        //greater than or equal to 16 += 80


        int totalWeight = 0;
        if(parameters[0]) totalWeight += 10;
        if(parameters[1]) totalWeight += 20;
        if(parameters[2]) totalWeight += 30;
        if(parameters[3]) totalWeight += 45;
        if(parameters[4]) totalWeight -= 5;
        if(parameters[5]) totalWeight -= 10;
        if(parameters[6]) totalWeight -= 40;
        if(length < 4) totalWeight -= 100;
        if(length >= 4) totalWeight += 10;
        if(length > 4 && length <= 6) totalWeight += 15;
        if(length > 6 && length <= 8) totalWeight += 25;
        if(length > 8 && length <= 10) totalWeight += 40;
        if(length > 10 && length < 16) totalWeight += 55;
        if(length >= 16) totalWeight += 80;

        PasswordStrength strength = PasswordStrength.Weak;
        if(totalWeight >= 30 && totalWeight < 60) strength = PasswordStrength.Basic;
        if(totalWeight >= 60 && totalWeight < 100) strength = PasswordStrength.Good;
        if(totalWeight >= 100) strength = PasswordStrength.Strong;

        return strength;
    }

    public static ChangeListener<? super String> generateValidatorListener(BooleanProperty validator, String regex, String validatorField, Text validatorNode){
        return (obs, o, n) -> {
            validatorNode.setText(validatorField);
            if(n != null){
                if(Pattern.matches(regex, n)){
                    validator.set(true);
                    if(validatorNode.isVisible()){
                        validatorNode.setVisible(false);
                    }
                }
                else{
                    validator.set(false);
                    if(!validatorNode.isVisible()){
                        validatorNode.setVisible(true);
                    }
                }
            }
        };
    }
    public static ChangeListener<? super String> generateValidatorListener(BooleanProperty validator, EmailValidator emailValidator, String validatorField, Text validatorNode){
        return (obs, o, n) -> {
            validatorNode.setText(validatorField);
            if(n != null){
                if(emailValidator.isValid(n)){
                    validator.set(true);
                    if(validatorNode.isVisible()){
                        validatorNode.setVisible(false);
                    }
                }
                else{
                    validator.set(false);
                    if(!validatorNode.isVisible()){
                        validatorNode.setVisible(true);
                    }
                }
            }
        };
    }
    public static ChangeListener<? super String> generateValidatorListener(BooleanProperty validator, String validatorField, Text validatorNode, DateTimeFormatter... formatters){
        if(formatters == null || formatters.length == 0) return null;
        return (obs, o, n) -> {
            validatorNode.setText(validatorField);
            if(n != null){
                String formattedText = null;
                for(DateTimeFormatter formatter : formatters){
                    try{
                        formattedText = formatter.format(formatter.parse(n));
                        break;
                    }catch(Exception ignored){

                    }
                }
                if(formattedText == null){
                    validator.set(false);
                    if(!validatorNode.isVisible()){
                        validatorNode.setVisible(true);
                    }
                }
                else {
                    validator.set(true);
                    if(validatorNode.isVisible()){
                        validatorNode.setVisible(false);
                    }
                }
            }
        };
    }
    public static ChangeListener<? super String> generateRestrictiveValidatorListener(BooleanProperty validator, String regex, int length, TextField field){
        return (obs, o, n) -> {
            if(n != null){
                if(!Pattern.matches(regex, n)){
                    field.setText(Objects.requireNonNullElse(o,""));
                }
                else{
                    if(n.length() == length){
                        validator.set(true);
                    }
                    else if(n.length() < length){
                        validator.set(false);
                    }
                    else{
                        field.setText(Objects.requireNonNullElse(o,""));
                    }
                }
            }
        };
    }
    public static ListChangeListener<? super Node> generateFormListener(BooleanProperty validator, Node submitButton, int minCount, String validatorText, Text validatorField){
        return c -> {
            validator.set(c.getList().size() >= minCount);
            submitButton.setDisable(!validator.getValue());
            validatorField.setText(validatorText);
            validatorField.setVisible(!validator.getValue());
        };
    }
    public static ChangeListener<? super Boolean> generateFormListener(Node node, BooleanProperty... validators){
        return (obs, o, n) -> {
            if(validators == null || validators.length == 0) node.setDisable(true);
            else{
                boolean invalid = false;
                for (BooleanProperty validator : validators) {
                    if (!validator.get()) {
                        invalid = true;
                        break;
                    }
                }
                node.setDisable(invalid);
            }
        };
    }
}
