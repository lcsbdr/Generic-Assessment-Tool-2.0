package de.assessmenttool.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import de.assessmenttool.kernel.Log;

public class Category {

  ArrayList<HashMap<String, Object>> category = new ArrayList<HashMap<String, Object>>();

  private static Connection conn;

  Category(Connection conn) {
    Category.conn = conn;
  }

  public ArrayList<HashMap<String, Object>> getCategories(int assessmentId) {
    // Keys which the map explicitly contains are: ["id", "name", "note", "weight", "category"]
    this.category = new ArrayList<HashMap<String, Object>>();
    try {
      Statement stmt = conn.createStatement();
      String query = "SELECT * FROM  at_categories where category = " + assessmentId;
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {

        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", rs.getInt("id"));
        hashmap.put("name", rs.getString("name"));
        hashmap.put("note", rs.getString("note"));
        hashmap.put("weight", rs.getFloat("weight"));
        hashmap.put("category", rs.getInt("category"));
        hashmap.put("assessment", rs.getInt("assessment"));
        this.category.add(hashmap);
      }
      rs.close();
    } catch (SQLException s) {
      Log.out("No Category found.", Log.STATUS_BAD, Log.DIRECTION_LOG);
    }
    return this.category;
  }

  public ArrayList<HashMap<String, Object>> getCategoriesFromAssessment(int assessmentId) {
    // Keys which the map explicitly contains are: ["id", "name", "note", "weight", "category"]
    this.category = new ArrayList<HashMap<String, Object>>();
    try {
      Statement stmt = conn.createStatement();
      String query = "SELECT * FROM  at_categories where assessment = " + assessmentId;
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {

        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", rs.getInt("id"));
        hashmap.put("name", rs.getString("name"));
        hashmap.put("note", rs.getString("note"));
        hashmap.put("weight", rs.getFloat("weight"));
        hashmap.put("category", rs.getInt("category"));
        hashmap.put("assessment", rs.getInt("assessment"));
        this.category.add(hashmap);
      }
      rs.close();
    } catch (SQLException s) {
    	Log.out("No Category found where assessment = " + assessmentId, Log.STATUS_BAD, Log.DIRECTION_LOG);

    }
    return this.category;
  }

  /*
   * public boolean saveCategories(ArrayList<HashMap<String, Object>> categories){
   * for(HashMap<String, Object> tmp : categories) {
   * int id= Integer.parseInt((String) tmp.get("id"));
   * try{
   * Statement stmt = conn.createStatement();
   * String query = "SELECT * FROM  at_categories WHERE id = "+id;
   * ResultSet rs =stmt.executeQuery(query);
   * if(rs.getInt("id")== id){
   * updateCategory(tmp);
   * }else{
   * insertCategory(tmp);
   * }
   * }catch(SQLException s){
   * System.out.println("es konnten keine Fragen gefunden werden");
   * }
   * }
   * return true;
   * }
   * private void updateCategory(HashMap<String, Object> tmp) {
   * try{
   * Statement stmt = conn.createStatement();
   * String mysql =
   * "UPDATE at_categories SET `name` = "+tmp.get("name")+", `note` = "+tmp.get("note")+", `weight` = "+((Float)
   * tmp.get(
   * "note")).floatValue()+", `axis` = "+Integer.getInteger((String)tmp.get("axis"))+", `category` ="+Integer.getInteger
   * ((String)tmp.get("category"))+" WHERE id ="+Integer.getInteger((String)tmp.get("id"));
   * stmt.executeUpdate(mysql);
   * System.out.println("at_questaxes wurde mit der Frage geupdatet");
   * }
   * catch (SQLException s){
   * System.out.println("SQL befehl nicht ausgef�hrt!");
   * }
   * }
   * private void insertCategory(HashMap<String, Object> tmp) {
   * try{
   * Statement stmt = conn.createStatement();
   * String mysql =
   * "INSERT INTO at_categories (`name`, `position`, `questionnaire`) VALUES ("+tmp.get("name")+", "+Integer
   * .getInteger((String)tmp.get("position"))+", "+Integer.getInteger((String)tmp.get("questionnaire"))+")";
   * stmt.executeUpdate(mysql);
   * System.out.println("at_questaxes wurde mit der Frage bef�llt");
   * }
   * catch (SQLException s){
   * System.out.println("SQL befehl nicht ausgef�hrt!");
   * }
   * }
   */
  public boolean delete(int categoryId) {
    try {
      Statement stmt = conn.createStatement();
      String mysql = "DELETE FROM at_categories where id = " + categoryId;
      stmt.execute(mysql);
      return true;
    } catch (SQLException e) {
        Log.out("Could not delete Category.", Log.STATUS_BAD, Log.DIRECTION_LOG);
      return false;
    }

  }

  public void setConnection(Connection conn2) {
    Category.conn = conn2;
  }

  public boolean saveCategories(ArrayList<HashMap<String, Object>> assessments) {
    for (HashMap<String, Object> tmp : assessments) {
      int id = -1;
      try {
        if ((Integer)tmp.get("id") != null) {
          id = (Integer)tmp.get("id");
        }
      } catch (java.lang.NumberFormatException e) {
        id = -1;
      }
      try {
        Statement stmt = Category.conn.createStatement();
        String query = "SELECT * FROM  at_categories WHERE id = " + id;
        ResultSet rs = stmt.executeQuery(query);
        if (id != -1 && rs.next() && rs.getInt("id") == id) {
          updateCategories(tmp);
        } else {
          insertCategories(tmp);
        }
      } catch (SQLException s) {
        s.printStackTrace();
        return false;
      }

    }
    return true;
  }

  private void insertCategories(HashMap<String, Object> tmp) {
    try {
      Statement stmt = Category.conn.createStatement();
      String name = (String)tmp.get("name");
      String note = (String)tmp.get("note");
      float weight = (Float)tmp.get("weight");
      int category = -1, assessment = -1;
      if (tmp.get("category") != null) category = (Integer)tmp.get("category");
      if (tmp.get("assessment") != null) assessment = (Integer)tmp.get("assessment");

      System.out.println(">>> NEW CATEOGRY: " + category + " assi: " + assessment + " text: " + name);
      
      String mysql = "INSERT INTO at_categories (name , note, weight, category, assessment) VALUES ('" + name + "', '"
                     + note + "', " + weight + ", " + category + ", " + assessment + ")";
      stmt.executeUpdate(mysql);

      Log.out(">>> NEW CATEOGRY was created successfull", Log.STATUS_GOOD, Log.DIRECTION_LOG);
    } catch (SQLException s) {
      s.printStackTrace();
    }
  }

  private void updateCategories(HashMap<String, Object> tmp) {
    try {
      Statement stmt = Category.conn.createStatement();
      int cat = -1, assessment = -1;
      if (tmp.get("category") != null) {
        cat = (Integer)tmp.get("category");
      }
      if (tmp.get("assessment") != null) {
        assessment = (Integer)tmp.get("assessment");
      }

      String mysql = "UPDATE at_categories SET  name  = '" + (String)tmp.get("name") + "', note = '"
                     + (String)tmp.get("note") + "', weight =" + ((Float)tmp.get("weight")).floatValue()
                     + ", category = " + cat + ", assessment = " + assessment + " WHERE id =" + tmp.get("id");
      stmt.executeUpdate(mysql);
      Log.out("Update of Category was a success", Log.STATUS_GOOD, Log.DIRECTION_LOG);
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Could not update category", Log.STATUS_BAD, Log.DIRECTION_LOG);
    }
  }

}
