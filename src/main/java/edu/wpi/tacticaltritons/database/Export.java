package edu.wpi.tacticaltritons.database;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Export {
  private static String convertToCSV(String[] data) {
    return Stream.of(data).map(Export::escapeSpecialCharacters).collect(Collectors.joining(","));
  }

  private static String escapeSpecialCharacters(String data) {
    String escapedData = data.replaceAll("\\R", " ");
    if (data.matches(",\"'")) {
      data = data.replace("\"", "\"\"");
      escapedData = "\"" + data + "\"";
    }
    return escapedData;
  }

  public static void export(File file, String tableName) throws Exception {
    PrintWriter writer = new PrintWriter(file);
    List<String[]> table = new ArrayList<>();
    if (tableName.equals("node")) {
      table = Export.nodeData();
    } else if (tableName.equals("edge")) {
      table = Export.edgeData();
    } else if (tableName.equals("locationname")) {
      table = Export.locationNameData();
    } else if (tableName.equals("move")) {
      table = Export.moveData();
    } else if (tableName.equals("login")) {
      table = Export.loginData();
    } else if (tableName.equals("meal")) {
      table = Export.loginData();
    }

    table.stream().map(Export::convertToCSV).forEach(writer::println);
    writer.close();

    if (!file.exists()) throw new Exception("Failed to export");
  }

  public static List<String[]> nodeData() throws SQLException {
    DAOFacade data = new DAOFacade();
    List<Node> allNodes = data.getAllNodes();
    List<String[]> table = new ArrayList<>();

    String[] header = {"nodeID", "xcoord", "ycoord", "building", "floor"};
    table.add(header);
    for (Node node : allNodes) {
      String[] dataRow = new String[5];
      dataRow[0] = Integer.toString(node.getNodeID());
      dataRow[1] = Integer.toString(node.getXcoord());
      dataRow[2] = Integer.toString(node.getYcoord());
      dataRow[3] = node.getBuilding();
      dataRow[4] = node.getFloor();
      table.add(dataRow);
    }
    return table;
  }

  public static List<String[]> edgeData() throws SQLException {
    DAOFacade data = new DAOFacade();
    List<Edge> allEdges = data.getAllEdges();
    List<String[]> table = new ArrayList<>();

    String[] header = {"startNode", "endNode"};
    table.add(header);
    for (Edge edge : allEdges) {
      String[] dataRow = new String[2];
      dataRow[0] = Integer.toString(edge.getStartNode().getNodeID());
      dataRow[1] = Integer.toString(edge.getEndNode().getNodeID());
      table.add(dataRow);
    }
    return table;
  }

  public static List<String[]> locationNameData() throws SQLException {
    DAOFacade data = new DAOFacade();
    List<LocationName> allLocationNames = data.getAllLocationNames();
    List<String[]> table = new ArrayList<>();

    String[] header = {"longName", "shortName", "nodeType"};
    table.add(header);
    for (LocationName locationName : allLocationNames) {
      String[] dataRow = new String[3];
      dataRow[0] = locationName.getLongName();
      dataRow[1] = locationName.getShortName();
      dataRow[2] = locationName.getNodeType();
      table.add(dataRow);
    }
    return table;
  }

  public static List<String[]> moveData() throws SQLException {
    DAOFacade data = new DAOFacade();
    List<Move> allMoves = data.getAllMoves();
    List<String[]> table = new ArrayList<>();

    String[] header = {"nodeID", "longName", "date"};
    table.add(header);
    for (Move move : allMoves) {
      String[] dataRow = new String[3];
      dataRow[0] = Integer.toString(move.getNode().getNodeID());
      dataRow[1] = move.getLocation().getLongName();
      dataRow[2] = String.valueOf(move.getMoveDate());
      table.add(dataRow);
    }
    return table;
  }

  public static List<String[]> loginData() throws SQLException {
    DAOFacade data = new DAOFacade();
    List<Login> allLogin = data.getAllLogins();
    List<String[]> table = new ArrayList<>();

    String[] header = {"user", "email", "firstname", "lastname", "admin"};
    table.add(header);
    for (Login login : allLogin) {
      String[] dataRow = new String[5];
      dataRow[0] = login.getUsername();
      dataRow[1] = login.getEmail();
      dataRow[2] = login.getFirstName();
      dataRow[3] = login.getLastName();
      dataRow[4] = String.valueOf(login.isAdmin());
      table.add(dataRow);
    }
    return table;
  }

  public static List<String[]> mealData() throws SQLException {
    DAOFacade data = new DAOFacade();
    List<Meal> allMeal = data.getAllMeal();
    List<String[]> table = new ArrayList<>();

    String[] header = {"ordernum", "requesterfirst", "requesterlast", "patientfirst", "patientlast", "assignedstafffirst", "assignedstafflast", "deliverydate", "deliverytime", "location", "items", "total", "status"};
    table.add(header);
    for (Meal meal : allMeal) {
      String[] dataRow = new String[13];
      dataRow[0] = String.valueOf(meal.getOrderNum());
      dataRow[1] = meal.getRequesterFirst();
      dataRow[2] = meal.getRequesterLast();
      dataRow[3] = meal.getPatientFirst();
      dataRow[4] = meal.getPatientLast();
      dataRow[5] = meal.getAssignedStaffFirst();
      dataRow[6] = meal.getAssignedStaffLast();
      dataRow[7] = String.valueOf(meal.getDeliveryDate());
      dataRow[8] = String.valueOf(meal.getDeliveryTime());
      dataRow[9] = meal.getLocation();
      dataRow[10] = meal.getItems();
      dataRow[11] = String.valueOf(meal.getTotal());
      dataRow[12] = meal.getStatus().toString();
      table.add(dataRow);
    }
    return table;
  }

  public static List<String[]> flowerData() throws SQLException {
    DAOFacade data = new DAOFacade();
    List<Flower> allFlower = data.getAllFlower();
    List<String[]> table = new ArrayList<>();

    String[] header = {"ordernum", "requesterfirst", "requesterlast", "patientfirst", "patientlast", "assignedstafffirst", "assignedstafflast", "deliverydate", "deliverytime", "location", "items", "total", "status"};
    table.add(header);
    for (Flower flower : allFlower) {
      String[] dataRow = new String[13];
      dataRow[0] = String.valueOf(flower.getOrderNum());
      dataRow[1] = flower.getRequesterFirst();
      dataRow[2] = flower.getRequesterLast();
      dataRow[3] = flower.getPatientFirst();
      dataRow[4] = flower.getPatientLast();
      dataRow[5] = flower.getAssignedStaffFirst();
      dataRow[6] = flower.getAssignedStaffLast();
      dataRow[7] = String.valueOf(flower.getDeliveryDate());
      dataRow[8] = String.valueOf(flower.getDeliveryTime());
      dataRow[9] = flower.getLocation();
      dataRow[10] = flower.getItems();
      dataRow[11] = String.valueOf(flower.getTotal());
      dataRow[12] = flower.getStatus().toString();
      table.add(dataRow);
    }
    return table;
  }
}
