//package edu.wpi.tacticaltritons;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import edu.wpi.tacticaltritons.navigation.Navigation;
//import edu.wpi.tacticaltritons.navigation.Screen;
//import org.junit.jupiter.api.Test;
//
//public class NavigationTest {
//
////  @Test
////  public void path1Test() {
////    Navigation.setDefaultScreen(Screen.TEST_1);
////    Navigation.navigate(Screen.TEST_2);
////    assertEquals(Screen.TEST_2,Navigation.journey.value);
////  }
//
//  @Test
//  public void path2Test() {
//    Navigation.setDefaultScreen(Screen.TEST_1);
//    Navigation.navigate(Screen.TEST_2);
//    Navigation.goBackward();
//    assertEquals(Screen.TEST_1, Navigation.journey.value);
//  }
//
////  @Test
////  public void path3Test() {
////    Navigation.setDefaultScreen(Screen.TEST_1);
////    Navigation.navigate(Screen.TEST_2);
////    Navigation.goBackward();
////    Navigation.goForward();
////    assertEquals(Screen.TEST_2, Navigation.journey.value);
////  }
//
//  @Test
//  public void path4Test() {
//    Navigation.setDefaultScreen(Screen.TEST_1);
//    Navigation.navigate(Screen.TEST_2);
//    Navigation.navigate(Screen.TEST_3);
//    Navigation.goBackward();
//    Navigation.goBackward();
//    assertEquals(Screen.TEST_1,Navigation.journey.value);
//  }
//
////  @Test
////  public void path5Test() {
////    Navigation.setDefaultScreen(Screen.TEST_1);
////    Navigation.navigate(Screen.TEST_2);
////    Navigation.navigate(Screen.TEST_3);
////    Navigation.goBackward();
////    Navigation.goBackward();
////    Navigation.goForward();
////    Navigation.goForward();
////    assertEquals(Screen.TEST_3,Navigation.journey.value);
////  }
//
//  @Test
//  public void path6Test(){
//    Navigation.setDefaultScreen(Screen.TEST_1);
//    Navigation.navigate(Screen.TEST_2);
//    Navigation.navigate(Screen.TEST_3);
//    //start a new path
//    Navigation.goBackward();
//    Navigation.navigate(Screen.TEST_4);
//    Navigation.navigate(Screen.TEST_5);
//    Navigation.goBackward();
//    Navigation.goBackward();
//    Navigation.goBackward();
//    assertEquals(Screen.TEST_1,Navigation.journey.value);
//  }
//
////  @Test
////  public void path7Test(){
////    Navigation.setDefaultScreen(Screen.TEST_1);
////    Navigation.navigate(Screen.TEST_2);
////    Navigation.navigate(Screen.TEST_3);
////    //start a new path
////    Navigation.goBackward();
////    Navigation.navigate(Screen.TEST_4);
////    Navigation.navigate(Screen.TEST_5);
////    Navigation.goBackward();
////    Navigation.goBackward();
////    Navigation.goBackward();
////    // try forward connection
////    Navigation.goForward();
////    Navigation.goForward();
////    Navigation.goForward();
////    assertEquals(Screen.TEST_5,Navigation.journey.value);
////  }
//
//  @Test
//  public void path8Test(){
//    Navigation.setDefaultScreen(Screen.TEST_1);
//    Navigation.navigate(Screen.TEST_2);
//    Navigation.navigate(Screen.TEST_3);
//    Navigation.goBackward();
//    Navigation.goBackward();
//    Navigation.navigate(Screen.TEST_3);
//    Navigation.navigate(Screen.TEST_2);
//    Navigation.goBackward();
//    Navigation.goBackward();
//    assertEquals(Screen.TEST_1,Navigation.journey.value);
//  }
//
////  @Test
////  public void path9Test(){
////    Navigation.setDefaultScreen(Screen.TEST_1);
////    Navigation.navigate(Screen.TEST_2);
////    Navigation.navigate(Screen.TEST_3);
////    Navigation.goBackward();
////    Navigation.goBackward();
////    Navigation.navigate(Screen.TEST_3);
////    Navigation.navigate(Screen.TEST_2);
////    Navigation.goBackward();
////    Navigation.goBackward();
////    Navigation.goForward();
////    Navigation.goForward();
////    assertEquals(Screen.TEST_2,Navigation.journey.value);
////  }
//
//  @Test
//  public void path10Test(){
//    Navigation.setDefaultScreen(Screen.TEST_1);
//    Navigation.navigate(Screen.TEST_2);
//    Navigation.navigate(Screen.TEST_2);
//    Navigation.goBackward();
//    assertEquals(Screen.TEST_1,Navigation.journey.value);
//  }
//
//}
//
//
