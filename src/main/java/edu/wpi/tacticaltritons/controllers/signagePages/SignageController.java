package edu.wpi.tacticaltritons.controllers.signagePages;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.pathfinding.AStarAlgorithm;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import edu.wpi.tacticaltritons.styling.*;
import javafx.scene.paint.Color;

public class SignageController {
  private String signageBlockPath = "../../views/signagePages/signageBlocks.fxml";
  @FXML private VBox basePane;
  @FXML private MFXFilterComboBox locationSelection;

  private int signageFontSize = 50;
  private enum signageDirections{
    FORWARD("mfx-chevron-up"),
    BACK("mfx-chevron-down"),
    LEFT("mfx-chevron-left"),
    RIGHT("mfx-chevron-right"),
    ;
    String arrowDiscription;
    signageDirections(String discription){
      arrowDiscription = discription;
    }
  }
  AStarAlgorithm pathFinder = new AStarAlgorithm();
  Map<Node, Set<Node>> neighbors;
  List<Move> moves;
  HashMap<String , Node> nodeTable;
  HashMap<Node , String> locationNameTable;
  HashMap<Node, String> nodeTypeTable;
  Node testNode = DAOFacade.getNode(1410);
  public SignageController() throws SQLException {
  }

  public void initialize() throws IOException, SQLException {
    nodeTable = new HashMap<>();
    locationNameTable = new HashMap<>();
    nodeTypeTable = new HashMap<>();

    neighbors = pathFinder.precomputeNeighbors(DAOFacade.getAllEdges());
    moves = DAOFacade.getAllMoves();
    ObservableList<String> locationNames = FXCollections.observableArrayList();
    for(Move locationInfo: moves){
//      Set<Node> neighborsOfThisNode = neighbors.get(locationInfo.getNode());
//      int roomCounter = 0;
//      for(Node neighbor: neighborsOfThisNode){
//        if(!nodeAndMoveTable.get(neighbor).getLocation().getNodeType().equals("HALL")) roomCounter++;
//      }
//      if(roomCounter > 0){
//        String locationLongName = locationInfo.getLocation().getLongName();
//        Node locationNode = locationInfo.getNode();
//        locationNames.add(locationLongName);
//        nodeTable.put(locationLongName, locationNode);
//        locationNameTable.put(locationNode,locationLongName);
//      }else{
//        neighbors.remove(locationInfo.getNode());
//      }
        String locationLongName = locationInfo.getLocation().getLongName();
        Node locationNode = locationInfo.getNode();
        String locationNodeType = locationInfo.getLocation().getNodeType();
        locationNames.add(locationLongName);
        nodeTable.put(locationLongName, locationNode);
        locationNameTable.put(locationNode,locationLongName);
        nodeTypeTable.put(locationNode,locationNodeType);
    }

    locationSelection.setItems(locationNames);
    locationSelection.setOnHidden(event -> {
      try {
        locationSelectionOnAction();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private void formatSignages(){
    EffectGenerator.generateSpacing(basePane,20);
    EffectGenerator.generateShadowEffect(basePane);
  }
  private void locationSelectionOnAction() throws IOException {
    String selectedLocation = locationSelection.getText();
    if(!selectedLocation.isEmpty()){
      loadAllSignage(selectedLocation);
      formatSignages();
    }
  }

  private void loadAllSignage(String locationLongName) throws IOException {
    basePane.getChildren().clear();
    Node fromNode = nodeTable.get(locationLongName);
    int fromX = fromNode.getXcoord();
    int fromY = fromNode.getYcoord();
    Set<Node> neighborNodesSet = neighbors.get(fromNode);
    for(Node neighborNode: neighborNodesSet){
      boolean addThisSignage = true;
      if(addThisSignage){
        int toX = neighborNode.getXcoord();
        int toY = neighborNode.getYcoord();
        int relativeX = toX - fromX;
        int relativeY = toY - fromY;
        signageDirections arrowDirection = generateDirection(relativeX,relativeY);
        String destinationName = locationNameTable.get(neighborNode);
        if(destinationName != null){
          if(!nodeTypeTable.get(neighborNode).equals("HALL")){
            loadSignageBlock(destinationName,arrowDirection);
          }
        }
      }
    }
  }

  private signageDirections generateDirection(int relativeX, int relativeY){
    if(relativeX > 0){
      if(relativeY > 0){
        if(relativeX - relativeY > 0){
          return signageDirections.FORWARD;
        }else{
          return signageDirections.RIGHT;
        }
      }else{
        if(relativeX + relativeY > 0){
          return signageDirections.FORWARD;
        }else{
          return signageDirections.LEFT;
        }
      }
    }else{
      if(relativeY > 0){
        if(relativeX + relativeY < 0){
          return signageDirections.BACK;
         }else{
          return signageDirections.RIGHT;
        }
      }else{
        if(relativeX - relativeY < 0){
          return signageDirections.BACK;
         }else{
          return signageDirections.LEFT;
        }
      }
    }
  }
  private void loadSignageBlock(String text,signageDirections direction) throws IOException {
    URL signageBlockURL = this.getClass().getResource(signageBlockPath);
    FXMLLoader loader = new FXMLLoader(signageBlockURL);
    HBox signageBlock = loader.load();
    signageBlock.setSpacing(signageFontSize);
    Label signageText = new Label(text);
    signageText.setStyle("-fx-font-size: " + signageFontSize);
    MFXFontIcon LeftArrow = new MFXFontIcon(direction.arrowDiscription,signageFontSize, Color.web(ThemeColors.YELLOW.getColor()));
    MFXFontIcon RightArrow = new MFXFontIcon(direction.arrowDiscription,signageFontSize, Color.web(ThemeColors.YELLOW.getColor()));
    signageBlock.getChildren().add(LeftArrow);
    signageBlock.getChildren().add(signageText);
    signageBlock.getChildren().add(RightArrow);
    basePane.getChildren().add(signageBlock);
  }
}
