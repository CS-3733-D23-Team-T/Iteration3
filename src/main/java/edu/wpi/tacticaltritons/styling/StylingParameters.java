package edu.wpi.tacticaltritons.styling;

import javafx.scene.paint.Color;

import java.util.Objects;


class StylingParameters {
     static final double shadowRadius = 15;
     static final double shadowOffsetX = 0;
     static final double shadowOffsetY = 0;
     //static final String invalid = Objects.requireNonNull(StylingParameters.class.getResource("../stylesheets/InvalidMFXTextFields.css")).toString();
     static final Color shandowColor = Color.web(ThemeColors.GRAY.getColor());
     static final String noFirstName = "Please input first name";
     static final String noLastName = "Please input last name ";
     static final String noDate = "Please select date";
     static final String noRoom = "Please select room";
     static final String noTime = "Provide Time";
     static final String FirstName = "First name";
     static final String LastName = "Last name ";
     static final String Date = "Select date";
     static final String Room = "Search for room";
     static final String Time = "Select time";
     static final String assignedComboBox = "Select a staff member";

}
