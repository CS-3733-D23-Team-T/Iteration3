package edu.wpi.tacticaltritons.controllers.announcements;

import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.auth.Validator;
import edu.wpi.tacticaltritons.data.AnnouncementType;
import edu.wpi.tacticaltritons.database.Announcements;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Session;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.apache.commons.validator.routines.EmailValidator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateAnnouncementsController {
    @FXML private MFXTextField emailField;
    @FXML private Text emailValidator;
    @FXML private MFXTextField firstNameField;
    @FXML private Text firstNameValidator;
    @FXML private MFXTextField lastNameField;
    @FXML private Text lastNameValidator;
    @FXML private MFXDatePicker dateField;
    @FXML private Text dateValidator;
    @FXML private MFXTextField timeField;
    @FXML private Text timeValidator;
    @FXML private MFXCheckbox adminCheckbox;
    @FXML private MFXCheckbox staffCheckbox;
    @FXML private Text recipientValidator;
    @FXML private MFXButton clearButton;
    @FXML private MFXButton submitButton;
    @FXML private MFXTextField titleField;
    @FXML private Text titleValidator;
    @FXML private MFXComboBox<String> typeCombobox;
    @FXML private Text typeValidator;
    @FXML private TextArea textArea;
    @FXML private Text textAreaValidator;

    @FXML
    private void initialize(){
        Session user = UserSessionToken.getUser();
        emailField.setText(user.getEmail());
        BooleanProperty validEmail = new SimpleBooleanProperty(true);
        emailField.textProperty().addListener(Validator.generateValidatorListener(validEmail,
                EmailValidator.getInstance(), emailValidator.getText(), emailValidator));

        firstNameField.setText(user.getFirstname());
        BooleanProperty validFirstName = new SimpleBooleanProperty(true);
        firstNameField.textProperty().addListener(Validator.generateValidatorListener(validFirstName,
                "[A-Za-z]{1,50}", firstNameValidator.getText(), firstNameValidator));

        lastNameField.setText(user.getLastname());
        BooleanProperty validLastName = new SimpleBooleanProperty(true);
        lastNameField.textProperty().addListener(Validator.generateValidatorListener(validLastName,
                "[A-Za-z]{1,50}", lastNameValidator.getText(), lastNameValidator));

        BooleanProperty validRecipients = new SimpleBooleanProperty(false);
        adminCheckbox.selectedProperty().addListener((obs, o, n) -> {
            if(recipientValidator.isVisible()){
                recipientValidator.setVisible(false);
            }
            validRecipients.set(n || staffCheckbox.isSelected());
        });
        staffCheckbox.selectedProperty().addListener((obs, o, n) -> {
            if(recipientValidator.isVisible()){
                recipientValidator.setVisible(false);
            }
            validRecipients.set(n || staffCheckbox.isSelected());
        });


        BooleanProperty validTitle = new SimpleBooleanProperty(false);
        titleField.textProperty().addListener(Validator.generateValidatorListener(validTitle,
                "[\\sA-Za-z0-9!?.]{4,32}", titleValidator.getText(), titleValidator));

        DateTimeFormatter[] dateFormatters = new DateTimeFormatter[]{
                DateTimeFormatter.ofPattern("MMM dd, yyyy"),
                DateTimeFormatter.ofPattern("MMM d, yyyy")
        };
        try {
            dateField.setValue(LocalDate.from(dateFormatters[1].parse(dateFormatters[1].format(LocalDateTime.now()))));
        } catch (Exception ignored) {
            try {
                dateField.setValue(LocalDate.from(dateFormatters[0].parse(dateFormatters[0].format(LocalDateTime.now()))));
            } catch (Exception ignore) {

            }
        }
        BooleanProperty validDate = new SimpleBooleanProperty(true);
        dateField.textProperty().addListener(Validator.generateValidatorListener(validDate,
                dateValidator.getText(), dateValidator, dateFormatters));

        DateTimeFormatter timeFormatter[] = new DateTimeFormatter[]{
                DateTimeFormatter.ofPattern("HH:mm"),
                DateTimeFormatter.ofPattern("H:mm:ss"),
                DateTimeFormatter.ofPattern("H:mm"),
                DateTimeFormatter.ofPattern("HH:mm:ss")
        };
        BooleanProperty validTime = new SimpleBooleanProperty(true);
        timeField.setText(timeFormatter[0].format(LocalDateTime.now()));
        timeField.textProperty().addListener(Validator.generateValidatorListener(validTime,
                timeValidator.getText(), timeValidator, timeFormatter));

        BooleanProperty validType = new SimpleBooleanProperty(false);
        typeCombobox.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> validType.set(n != null));
        List<String> types = AnnouncementType.collect();
        typeCombobox.getItems().addAll(types);

        BooleanProperty validMessage = new SimpleBooleanProperty(false);
        textArea.textProperty().addListener(Validator.generateValidatorListener(validMessage,
                "[A-Za-z0-9\\s!?.,$%*#]{8,512}", textAreaValidator.getText(), textAreaValidator));

        validEmail.addListener(Validator.generateFormListener(submitButton,
                validEmail,
                validFirstName,
                validLastName,
                validRecipients,
                validTitle,
                validDate,
                validTime,
                validType,
                validMessage));

        validFirstName.addListener(Validator.generateFormListener(submitButton,
                        validEmail,
                        validFirstName,
                        validLastName,
                        validRecipients,
                        validTitle,
                        validDate,
                        validTime,
                        validType,
                        validMessage));

        validLastName.addListener(Validator.generateFormListener(submitButton,
                validEmail,
                validFirstName,
                validLastName,
                validRecipients,
                validTitle,
                validDate,
                validTime,
                validType,
                validMessage));

        validRecipients.addListener(Validator.generateFormListener(submitButton,
                validEmail,
                validFirstName,
                validLastName,
                validRecipients,
                validTitle,
                validDate,
                validTime,
                validType,
                validMessage));

        validTitle.addListener(Validator.generateFormListener(submitButton,
                validEmail,
                validFirstName,
                validLastName,
                validRecipients,
                validTitle,
                validDate,
                validTime,
                validType,
                validMessage));

        validDate.addListener(Validator.generateFormListener(submitButton,
                validEmail,
                validFirstName,
                validLastName,
                validRecipients,
                validTitle,
                validDate,
                validTime,
                validType,
                validMessage));

        validMessage.addListener(Validator.generateFormListener(submitButton,
                validEmail,
                validFirstName,
                validLastName,
                validRecipients,
                validTitle,
                validDate,
                validTime,
                validType,
                validMessage));

        validTime.addListener(Validator.generateFormListener(submitButton,
                validEmail,
                validFirstName,
                validLastName,
                validRecipients,
                validTitle,
                validDate,
                validTime,
                validType,
                validMessage));

        validType.addListener(Validator.generateFormListener(submitButton,
                validEmail,
                validFirstName,
                validLastName,
                validRecipients,
                validTitle,
                validDate,
                validTime,
                validType,
                validMessage));

        clearButton.setOnAction(event -> {
            emailField.setText("");
            firstNameField.setText("");
            lastNameField.setText("");
            staffCheckbox.setSelected(false);
            adminCheckbox.setSelected(false);
            titleField.setText("");
            dateField.setText("");
            timeField.setText("");
            typeCombobox.getSelectionModel().clearSelection();
            textArea.clear();

        });

        submitButton.setOnAction(event -> {
            String audience = "";
            if(adminCheckbox.isSelected()) audience += "admin ";
            if(staffCheckbox.isSelected()) audience += "staff ";

            LocalDateTime time = LocalDateTime.of(dateField.getValue(), LocalTime.parse(timeField.getText()));

            audience = audience.substring(0, audience.length() - 1);
            Announcements announcement = new Announcements(
                    UUID.randomUUID(),
                    titleField.getText(),
                    textArea.getText(),
                    firstNameField.getText() + ", " + lastNameField.getText(),
                    Timestamp.valueOf(LocalDateTime.now()),
                    Timestamp.valueOf(time),
                    AnnouncementType.parse(typeCombobox.getSelectedItem()).name(),
                    audience,
                    1
            );

            new Thread(() -> {
                try {
                    DAOFacade.insertAnnouncements(announcement);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            Navigation.navigate(Screen.HOME);
        });
    }
}
