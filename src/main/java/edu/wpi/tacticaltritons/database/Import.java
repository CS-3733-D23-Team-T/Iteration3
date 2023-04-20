package edu.wpi.tacticaltritons.database;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Import {

  /**
   * Import node file method
   *
   * @param file a file with the .csv file extension
   * @throws IOException when file is not properly imported
   * @throws SQLException for bad table insert
   */
  public static void importFile(File file) throws IOException, SQLException, ParseException {
    Connection connection = null;
    try {
      connection = Tdb.getConnection();
      BufferedReader br = new BufferedReader(new FileReader(file));
      String headerLine = br.readLine().toLowerCase();
      CopyManager copyManager = new CopyManager((BaseConnection) connection);
      FileReader fileReader = new FileReader(file);

      if (headerLine.equals("nodeid,xcoord,ycoord,floor,building")) {
        copyManager.copyIn("COPY node FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("startnode,endnode")) {
        copyManager.copyIn(
                "COPY edge (startNode, endNode) FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("longname,shortname,nodetype")) {
        copyManager.copyIn("COPY locationname FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("nodeid,longname,date")) {
        copyManager.copyIn("COPY move FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else {
        //todo do something
      }
    } catch (SQLException e){
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      if(connection != null){
        connection.close();
      }
    }
  }
}
