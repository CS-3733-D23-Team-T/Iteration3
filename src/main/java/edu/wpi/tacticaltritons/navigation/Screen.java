package edu.wpi.tacticaltritons.navigation;

import java.util.regex.Pattern;

public enum Screen {
  LOGIN("views/login/Login.fxml"),
  LOGIN_AUTHENTICATION("views/login/LoginAuthentication.fxml"),
  APP_LOGIN_AUTHENTICATION("views/login/LoginAppAuthentication.fxml"),
  RESET_PASSWORD("views/login/ResetPassword.fxml"),
  LOGIN_CONTAINER("views/login/LoginContainer.fxml"),

  ROOT("views/Root.fxml"),
  HOME("views/Home.fxml"),

  SETTINGS("views/settings/Settings.fxml"),
  USER_OPTIONS("views/settings/UserOptions.fxml"),
  CREATE_ACCOUNT("views/settings/CreateAccount.fxml"),
  EDIT_USER("views/settings/EditUser.fxml"),
  CHANGE_EMAIL("views/settings/ChangeEmail.fxml"),
  CHANGE_PASSWORD("views/settings/ChangePassword.fxml"),
  TWO_FACTOR_AUTH("views/settings/TwoFactorAuth.fxml"),

  TWO_FACTOR_EMAIL("views/settings/twoFactor/Email.fxml"),
  TWO_FACTOR_APP("views/settings/twoFactor/App.fxml"),
  TWO_FACTOR_PHONE("views/settings/twoFactor/Phone.fxml"),


  CONFERENCE_ROOM_REQUEST("views/serviceRequest/ConferenceRoomRequest.fxml"),
  MEAL_RESTAURANT("views/serviceRequest/MealDeliveryRestaurants.fxml"),
  MEAL_REQUEST("views/serviceRequest/MealDeliveryItems.fxml"),
  MEAL_SUBMIT("views/serviceRequest/MealDeliverySubmit.fxml"),

  FLOWER_REQUEST("views/serviceRequest/FlowerDelivery.fxml"),
  FLOWER_CHOICE("views/serviceRequest/FlowerChoice.fxml"),
  FLOWER_CHECKOUT("views/serviceRequest/FlowerCheckout.fxml"),

  SIGNAGE("views/signagePages/Signage.fxml"),
  PATHFINDING("views/PathfindingFullScreen.fxml"),
  EDIT_MAP("views/EditMapFullScreen.fxml"),
  VIEW_MAP("views/ViewMapFullScreen.fxml"),

  DATABASE("views/database/Database.fxml"),
  DATABASE_HELP("views/database/DatabaseHelp.fxml"),
  EDIT_DATABASE("views/database/EditDatabase.fxml"),
  FURNITURE_DELIVERY("views/serviceRequest/FurnitureDelivery.fxml"),
  FURNITURE_CHECKOUT("views/serviceRequest/FurnitureCheckout.fxml"),
  CONFERENCE_ROOM("views/serviceRequest/ConferenceRoomRequest.fxml"),
  OFFICE_SUPPLIES("views/OfficeSupplies.fxml"),
  VIEW_SERVICE_REQUEST("views/serviceRequest/ViewServiceRequests.fxml"),
  MOVE("views/Move.fxml"),

  TEST_1("views/test_pages/p1.fxml"),
  TEST_2("views/test_pages/p2.fxml"),
  TEST_3("views/test_pages/p3.fxml"),
  TEST_4("views/test_pages/p4.fxml"),
  TEST_5("views/test_pages/p5.fxml")
  ;
  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }

  public String formatScreenName() {
    String screenName =
        this.filename.substring(this.filename.lastIndexOf('/') + 1, this.filename.lastIndexOf('.'));

    for (int i = 1; i < screenName.length(); i++) {
      if (Pattern.matches("[A-Z_]", String.valueOf(screenName.charAt(i)))) {
        screenName = screenName.substring(0, i) + " " + screenName.substring(i);
        i++;
      }
    }

    return screenName;
  }
}
