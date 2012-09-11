package de.assessmenttool.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Dependencies {

  ArrayList<HashMap<String, Object>> deps = new ArrayList<HashMap<String, Object>>();

  private Connection conn;

  public Dependencies(Connection conn) {
    this.conn = conn;
  }

  public ArrayList<HashMap<String, Object>> getDependencies() {
    this.deps = new ArrayList<HashMap<String, Object>>();

    // Keys which the map explicitly contains are: ["id", "name", "depth3d", "creation", "author"]
    try {
      Statement stmt = this.conn.createStatement();
      String query = "SELECT * FROM  at_dependenciesNew";
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", rs.getInt("id"));
        hashmap.put("quest", rs.getInt("quest"));
        hashmap.put("depQuest", rs.getInt("depQuest"));
        hashmap.put("completionNeeded", rs.getInt("completionNeeded"));
        this.deps.add(hashmap);
      }
      rs.close();
    } catch (SQLException s) {
      s.printStackTrace();
    }
    return this.deps;
  }

  public boolean saveDependency(HashMap<String, Object> tmp) {
    int id = -1;
    try {
      id = Integer.parseInt((String)tmp.get("id"));
    } catch (java.lang.NumberFormatException e) {
      id = -1;
    }

    try {
      Statement stmt = this.conn.createStatement();
      String query = "SELECT * FROM  at_dependenciesNew WHERE id = " + id;
      ResultSet rs = stmt.executeQuery(query);
      if (id != -1 && rs.getInt("id") == id) {
        updateDep(tmp);
      } else {
        insertDep(tmp);
      }
    } catch (SQLException s) {
      s.printStackTrace();
      return false;
    }

    return true;
  }

  private void insertDep(HashMap<String, Object> tmp) {
    try {
      Statement stmt = this.conn.createStatement();
      int quest = (Integer)tmp.get("quest");
      int depQuest = (Integer)tmp.get("depQuest");
      int completionNeeded = (Integer)tmp.get("completionNeeded");

      String mysql = "INSERT INTO at_dependenciesNew (quest,depQuest,completionNeeded ) VALUES" + "('" + quest + "', '"
                     + depQuest + "', '" + completionNeeded + "')";
      stmt.executeUpdate(mysql);
    } catch (SQLException s) {
      s.printStackTrace();
    }
  }

  private void updateDep(HashMap<String, Object> tmp) {
    try {
      Statement stmt = this.conn.createStatement();
      String mysql = "UPDATE at_dependenciesNew SET `quest` = " + Integer.getInteger((String)tmp.get("quest"))
                     + ",`depQuest` = " + Integer.getInteger((String)tmp.get("depQuest")) + ", `completionNeeded` ="
                     + Integer.getInteger((String)tmp.get("completionNeeded")) + " WHERE id ="
                     + Integer.getInteger((String)tmp.get("id"));
      stmt.executeUpdate(mysql);
    } catch (SQLException s) {
      s.printStackTrace();
    }
  }

  public boolean delete(int assessmentId, int depQuestId) {
    try {
      Statement stmt = this.conn.createStatement();
      String mysql = "DELETE FROM at_dependenciesNew where quest = " + assessmentId + " AND depQuest = " + depQuestId;
      stmt.execute(mysql);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }

  }

  public void setConnection(Connection conn2) {
    this.conn = conn2;
  }

}
