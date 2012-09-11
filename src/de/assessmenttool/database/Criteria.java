package de.assessmenttool.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Criterion.CriterionType;
import de.assessmenttool.kernel.Log;

public class Criteria {
  private Connection conn;

  ArrayList<HashMap<String, Object>> criteria = new ArrayList<HashMap<String, Object>>();

  ArrayList<HashMap<String, Object>> possibleCriteria = new ArrayList<HashMap<String, Object>>();

  ArrayList<HashMap<String, Object>> criterionParameter = new ArrayList<HashMap<String, Object>>();

  public Criteria(Connection conn2) {
    this.conn = conn2;
  }

  public ArrayList<HashMap<String, Object>> getCriteria() {
    this.criteria = new ArrayList<HashMap<String, Object>>();

    // Keys which the map explicitly contains are: ["id", "name", "critType"]
    try {
      Statement stmt = this.conn.createStatement();
      String query = "SELECT * FROM  at_criteria";
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", rs.getInt("id"));
        hashmap.put("name", rs.getString("name"));
        hashmap.put("critType", getCritTypeName(rs.getInt("critType")));
        this.criteria.add(hashmap);
      }
      rs.close();
    } catch (SQLException s) {
      s.printStackTrace();

      Log.out("Could not find criteria ", Log.STATUS_BAD, Log.DIRECTION_LOG);
    }
    return this.criteria;
  }

  public ArrayList<HashMap<String, Object>> getPossibleCriteria() {
    // Keys which the map explicitly contains are: ["id", "type"]
    try {
      Statement stmt = this.conn.createStatement();
      String query = "SELECT * FROM  at_critPossible";
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", rs.getInt("id"));
        hashmap.put("type", rs.getString("type"));
        this.possibleCriteria.add(hashmap);
      }
      rs.close();
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Could not find at_critPossible ", Log.STATUS_BAD, Log.DIRECTION_LOG);
    }
    return this.possibleCriteria;
  }

  public ArrayList<HashMap<String, Object>> getCriterionParameters(int criterionId) {
    this.criterionParameter = new ArrayList<HashMap<String, Object>>();
    // Keys which the map explicitly contains are: ["id", "name", "depth3d", "creation", "author"]
    try {
      Statement stmt = this.conn.createStatement();
      String query = "SELECT * FROM  at_critParams where criterion = " + criterionId;
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", rs.getInt("id"));
        hashmap.put("key", rs.getString("key"));
        hashmap.put("value", rs.getString("value"));
        hashmap.put("criterion", rs.getInt("criterion"));
        this.criterionParameter.add(hashmap);
      }
      rs.close();
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Could not find at_critParams where criterion = " + criterionId, Log.STATUS_BAD, Log.DIRECTION_LOG);
    }
    return this.criterionParameter;
  }

  public void setConnection(Connection conn2) {
    this.conn = conn2;
  }

  public boolean saveCriterionType(CriterionType crit) {
    try {
      Statement stmt = this.conn.createStatement();
      String query = "SELECT * FROM  at_critPossible where type = '" + crit + "'";
      ResultSet rs = stmt.executeQuery(query);

      if (!rs.next()) {
        Statement st = this.conn.createStatement();
        String mysql = "INSERT INTO at_critPossible (type) VALUES ('" + crit + "')";
        st.executeUpdate(mysql);
      }
      rs.close();
      return true;
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Could not find at_critPossible where type = '" + crit, Log.STATUS_BAD, Log.DIRECTION_LOG);
      return false;
    }
  }

  public boolean saveCriterion(Criterion crit) {
    int id = crit.getId();

    try {
      Statement stmt = this.conn.createStatement();
      String query = "SELECT * FROM  at_criteria WHERE id = " + id;
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next() && id != -1 && rs.getInt("id") == id) {
        return updateCriterion(crit);
      } else {
        return insertCriterion(crit);
      }
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Could not find at_criteria WHERE id = " + id, Log.STATUS_BAD, Log.DIRECTION_LOG);
      return false;
    }
  }

  private int getCritTypeId(CriterionType critType) {
    try {
      Statement stmt = this.conn.createStatement();
      String query = "SELECT * FROM  at_critPossible WHERE type = '" + critType + "' LIMIT 1";
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        return rs.getInt("id");
      }
      return -1;
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Could not find at_critPossible WHERE type = '" + critType, Log.STATUS_BAD, Log.DIRECTION_LOG);
      return -1;
    }
  }

  private String getCritTypeName(int critTypeId) {
    try {
      Statement stmt = this.conn.createStatement();
      String query = "SELECT * FROM  at_critPossible WHERE id = '" + critTypeId + "' LIMIT 1";
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next()) {
        return rs.getString("type");
      }
      return "";
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Could not find at_critPossible WHERE id = '" + critTypeId, Log.STATUS_BAD, Log.DIRECTION_LOG);
      return "";
    }
  }

  private boolean insertCriterion(Criterion crit) {
    try {
      Statement stmt = this.conn.createStatement();

      String mysql = "INSERT INTO at_criteria (name, critType) VALUES " + "('" + crit.getName() + "',"
                     + getCritTypeId(crit.getType()) + ")";
      stmt.executeUpdate(mysql);
      return true;
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Could not create criteria", Log.STATUS_BAD, Log.DIRECTION_LOG);
      return false;
    }
  }

  private boolean updateCriterion(Criterion crit) {
    try {
      Statement stmt = this.conn.createStatement();
      String mysql = "UPDATE at_criteria SET name  = '" + crit.getName() + "', critType = "
                     + getCritTypeId(crit.getType()) + "  WHERE id =" + crit.getId();
      stmt.executeUpdate(mysql);
      return true;
    } catch (SQLException s) {
      s.printStackTrace();
      return false;
    }
  }

  public boolean saveCriterionParameters(Criterion crit) {
    boolean retVal = true;
    HashMap<String, String> params = crit.getParams();
    for (int i = 1; i <= params.size(); i++) {
      int id = i;

      Statement stmt = null;
      try {
        stmt = this.conn.createStatement();
        String query = "SELECT * FROM  at_critParams WHERE criterion = " + crit.getId() + " AND key = '" + id + "'";
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next() && id != -1 && rs.getInt("id") == id) {
          retVal = updateCriterionParam(id, params.get(String.valueOf(id)), crit.getId());
        } else {
          retVal = insertCriterionParam(id, params.get(String.valueOf(id)), crit.getId());
        }
      } catch (SQLException s) {
        s.printStackTrace();
        return false;
      } finally {
        try {
          stmt.close();
        } catch (SQLException e) {
            Log.out("Could not save at_critParams " , Log.STATUS_BAD, Log.DIRECTION_LOG);
          e.printStackTrace();
        }
      }
    }
    return true;
  }

  private boolean insertCriterionParam(int key, String value, int criterion) {
    try {
      Statement stmt = this.conn.createStatement();
      String mysql = "INSERT INTO at_critParams (key, value, criterion) VALUES " + "('" + key + "','" + value + "', "
                     + criterion + ")";
      stmt.executeUpdate(mysql);
      return true;
    } catch (SQLException s) {
      s.printStackTrace();
      return false;
    }
  }

  private boolean updateCriterionParam(int key, String value, int criterion) {
    try {
      Statement stmt = this.conn.createStatement();
      String mysql = "UPDATE at_critParams SET value  = " + value + " WHERE key ='" + key + "' AND criterion = "
                     + criterion;
      stmt.executeUpdate(mysql);
      return true;
    } catch (SQLException s) {
      s.printStackTrace();
      return false;
    }
  }

  public boolean removeCriterion(int id) {
    try {
      Statement stmt = this.conn.createStatement();
      String mysql = "DELETE FROM at_criteria where id = " + id;
      stmt.execute(mysql);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
