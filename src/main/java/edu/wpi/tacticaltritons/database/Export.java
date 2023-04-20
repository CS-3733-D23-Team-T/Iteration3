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
}
