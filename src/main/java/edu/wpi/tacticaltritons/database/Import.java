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
  public static void importFile(File file, String tableName) throws IOException, SQLException, ParseException {
    Connection connection = null;
    try {
      connection = Tdb.getConnection();
      BufferedReader br = new BufferedReader(new FileReader(file));
      String headerLine = br.readLine().toLowerCase();
      CopyManager copyManager = new CopyManager((BaseConnection) connection);
      FileReader fileReader = new FileReader(file);

      if (headerLine.equals("nodeid,xcoord,ycoord,floor,building") && tableName.equals("node")) {
        copyManager.copyIn("COPY node FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("startnode,endnode") && tableName.equals("edge")) {
        copyManager.copyIn("COPY edge FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("longname,shortname,nodetype") && tableName.equals("locationname")) {
        copyManager.copyIn("COPY locationname FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("nodeid,longname,date") && tableName.equals("move")) {
        copyManager.copyIn("COPY move FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("username,email,firstname,lastname,admim,password,salt,lastlogin,narration,language,twofactor,darkmode,twofactormethods,twofactorfrequency,tokentime,algorithmpreference") && tableName.equals("login")) {
        copyManager.copyIn("COPY login FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("ordernum,requesterfirst,requesterlast,patientfirst,patientlast,assignedstafffirst,assignedstafflast,deliverydate,deliverytime,location,items,total,status") && tableName.equals("meal")) {
        copyManager.copyIn("COPY meal FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("ordernum,requesterfirst,requesterlast,patientfirst,patientlast,assignedstafffirst,assignedstafflast,deliverydate,deliverytime,location,items,total,status") && tableName.equals("flower")) {
        copyManager.copyIn("COPY flower FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("ordernum,firstname,lastname,assignedstafffirst,assignedstafflast,date,location,items,status") && tableName.equals("furnitureforms")) {
        copyManager.copyIn("COPY furnitureforms FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("ordernum,firstname,lastname,date,attendance,expectedsize,location,status") && tableName.equals("conference")) {
        copyManager.copyIn("COPY conference FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("ordernum,firstname,lastname,assignedstafffirst,assignedstafflast,date,time,location,items,price,status") && tableName.equals("officesuppliesform")) {
        copyManager.copyIn("COPY officesuppliesform FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("itemname,prices,restaurant") && tableName.equals("requestoptions")) {
        copyManager.copyIn("COPY requestoptions FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("itemname,prices,shop,shopdescription,itemtype,itemdescription") && tableName.equals("flowerrequestoptions")) {
        copyManager.copyIn("COPY flowerrequestoptions FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("itemname,itemtype,itemdescription") && tableName.equals("furniturerequestoptions")) {
        copyManager.copyIn("COPY furniturerequestoptions FROM STDIN (FORMAT csv, HEADER)", fileReader);
      } else if (headerLine.equals("itemname,price,shop,itemtype,itemdescription") && tableName.equals("officesuppliesrequestoptions")) {
        copyManager.copyIn("COPY officesuppliesrequestoptions FROM STDIN (FORMAT csv, HEADER)", fileReader);
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
