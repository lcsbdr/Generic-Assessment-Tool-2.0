package de.assessmenttool.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.assessmenttool.kernel.Log;

public class Questionnaires {

  ArrayList<HashMap<String, Object>> assessment = new ArrayList<HashMap<String, Object>>();

  private Connection conn;

  public Questionnaires(Connection conn) {
    this.conn = conn;
  }

  public ArrayList<HashMap<String, Object>> getAssessments() {
    this.assessment = new ArrayList<HashMap<String, Object>>();

    // Keys which the map explicitly contains are: ["id", "name", "depth3d", "creation", "author"]
    try {
      Statement stmt = this.conn.createStatement();
      String query = "SELECT * FROM  at_questionnaires";
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", rs.getInt("id"));
        hashmap.put("name", rs.getString("name"));
        hashmap.put("depth3d", rs.getInt("depth3d"));
        hashmap.put("creation", rs.getTimestamp("creation"));
        hashmap.put("author", rs.getString("author"));
        this.assessment.add(hashmap);
      }
      rs.close();
    } catch (SQLException s) {
      s.printStackTrace();

      Log.out("Could not find at_questionnaires " +s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);
    }
    return this.assessment;
  }

  public boolean saveAssessments(ArrayList<HashMap<String, Object>> assessments) {
    for (HashMap<String, Object> tmp : assessments) {
      int id = -1;
      try {
        id = Integer.parseInt((String)tmp.get("id"));
      } catch (java.lang.NumberFormatException e) {
        id = -1;
      }

      try {
        Statement stmt = this.conn.createStatement();
        String query = "SELECT * FROM  at_questionnaires WHERE id = " + id;
        ResultSet rs = stmt.executeQuery(query);
        if (id != -1 && rs.getInt("id") == id) {
          updateAssessment(tmp);
        } else {
          insertAssessment(tmp);
        }
      } catch (SQLException s) {
        s.printStackTrace();
        Log.out("Could not save questionnaires " + s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);

        return false;
      }

    }
    return true;
  }

  private void insertAssessment(HashMap<String, Object> tmp) {
    try {
      Statement stmt = this.conn.createStatement();
      int is3D = 0;
      if ((Boolean)tmp.get("depth3d")) is3D = 1;
      String name = (String)tmp.get("name");
      int author = (Integer)tmp.get("author");
      Date creation = (Date)tmp.get("creation");
      System.out.println("dep: " + is3D + " name: " + name + " author:" + author);

      String mysql = "INSERT INTO at_questionnaires (name, depth3d, creation,author ) VALUES" + "('" + name + "', '"
                     + is3D + "', '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(creation) + "', '" + author
                     + "')";
      stmt.executeUpdate(mysql);
    } catch (SQLException s) {
      s.printStackTrace();

      Log.out("Could not create questionnair " +s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);
    }
  }

  private void updateAssessment(HashMap<String, Object> tmp) {
    try {
      Statement stmt = this.conn.createStatement();
      String mysql = "UPDATE at_questionnaires SET `name` = " + tmp.get("name") + ",`depth3d` = "
                     + Integer.getInteger((String)tmp.get("depth3d")) + ", `author` =" + tmp.get("author")
                     + " WHERE id =" + Integer.getInteger((String)tmp.get("id"));
      stmt.executeUpdate(mysql);
    } catch (SQLException s) {

    }
  }

  public boolean delete(int assessmentId) {
    try {
      Statement stmt = this.conn.createStatement();
      String mysql = "DELETE FROM at_questionnaires where id = " + assessmentId;
      stmt.execute(mysql);
      System.out.println("Assessment gel�scht");
      return true;
    } catch (SQLException e) {

      return false;
    }

  }

  public void setConnection(Connection conn2) {
    this.conn = conn2;
  }

}
