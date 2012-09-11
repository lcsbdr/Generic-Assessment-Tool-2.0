package de.assessmenttool.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import de.assessmenttool.kernel.Log;

public class Question {

  ArrayList<HashMap<String, Object>> question = new ArrayList<HashMap<String, Object>>();

  private static Connection conn;

  Question(Connection conn) {
    Question.conn = conn;
  }

  public ArrayList<HashMap<String, Object>> getQuestions(int assessmentId) {
    // Keys which the map explicitly contains are: ["id", "text", "weight",
    // "answered", "note", "category", "criterion", "answer"]
    try {
      Statement stmt = conn.createStatement();
      String query = "SELECT * FROM  at_questions where id = " + assessmentId;
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {

        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", rs.getInt("id"));
        hashmap.put("text", rs.getString("text"));
        hashmap.put("note", rs.getString("note"));
        hashmap.put("weight", rs.getFloat("weight"));
        hashmap.put("criterion", rs.getInt("criterion"));
        hashmap.put("category", rs.getInt("category"));
        hashmap.put("answered", rs.getInt("answered"));
        hashmap.put("answer", rs.getInt("answer"));
        hashmap.put("assessment", rs.getInt("assessment"));
        this.question.add(hashmap);
      }
      rs.close();
    } catch (SQLException s) {
        Log.out("Could not find a question", Log.STATUS_BAD, Log.DIRECTION_LOG);
    }
    return this.question;
  }

  /*
   * public boolean saveQuestions(ArrayList<HashMap<String, Object>>
   * questions){
   * for(HashMap<String, Object> tmp : questions) { int id=
   * Integer.parseInt((String) tmp.get("id")); try{ Statement stmt =
   * conn.createStatement(); String query =
   * "SELECT * FROM  at_questions WHERE id = "+id; ResultSet rs
   * =stmt.executeQuery(query); if(rs.getInt("id")== id){ updateQuestion(tmp);
   * }else{ insertQuestion(tmp); } }catch(SQLException s){
   * System.out.println("es konnten keine Fragen gefunden werden"); } } return
   * true; }
   * private void updateQuestion(HashMap<String, Object> tmp) { try{ Statement
   * stmt = conn.createStatement(); String mysql = "UPDATE at_questions SET "
   * + "`text` = "+tmp.get("text")+", " + "`note` = "+tmp.get("note")+", " +
   * "`weight` = "+((Float) tmp.get("weight")).floatValue()+", " +
   * "`axis` = "+Integer.getInteger((String)tmp.get("axis"))+", " +
   * "`category` ="+Integer.getInteger((String)tmp.get("category"))+
   * "`criterion` ="+Integer.getInteger((String)tmp.get("criterion"))+
   * "`answered` ="+Integer.getInteger((String)tmp.get("answered"))+
   * " WHERE id ="+Integer.getInteger((String)tmp.get("id"));
   * stmt.executeUpdate(mysql);
   * System.out.println("at_questions wurde mit der Frage geupdatet"); } catch
   * (SQLException s){ System.out.println("SQL befehl nicht ausgef�hrt!"); } }
   * private void insertQuestion(HashMap<String, Object> tmp) { try{ Statement
   * stmt = conn.createStatement(); String mysql =
   * "INSERT INTO at_questions (`text`, `note`, `weight`, `axis`, `category`, `criterion`, `answered`) VALUES "
   * + "("+tmp.get("text")+","+tmp.get("texnotet")+", "+((Float)
   * tmp.get("weight")).floatValue()+
   * ", "+Integer.getInteger((String)tmp.get("axis"
   * ))+", "+Integer.getInteger((
   * String)tmp.get("category"))+", "+Integer.getInteger
   * ((String)tmp.get("criterion"
   * ))+", "+Integer.getInteger((String)tmp.get("answered"))+")";
   * stmt.executeUpdate(mysql);
   * System.out.println("at_questions wurde mit der Frage bef�llt"); } catch
   * (SQLException s){ System.out.println("SQL befehl nicht ausgef�hrt!"); }
   * }
   */

  public boolean delete(int questionId) {
    try {
      Statement stmt = conn.createStatement();
      String mysql = "DELETE FROM at_questions where id = " + questionId;
      stmt.execute(mysql);
      return true;
    } catch (SQLException e) {

      Log.out("Could not delete the question", Log.STATUS_BAD, Log.DIRECTION_LOG);
      return false;
    }

  }

  public void setConnection(Connection conn2) {
    Question.conn = conn2;
  }

  public boolean saveQuestions(ArrayList<HashMap<String, Object>> questions) {
    for (HashMap<String, Object> tmp : questions) {
      int id = -1;
      try {
        id = (Integer)tmp.get("id");
      } catch (Exception e) {
        id = -1;
      }

      try {
        Statement stmt = Question.conn.createStatement();
        String query = "SELECT * FROM  at_questions WHERE id = " + id;
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next() && id != -1 && rs.getInt("id") == id) {
          updateQuestion(tmp);
        } else {
          insertQuestion(tmp);
        }
      } catch (SQLException s) {
        s.printStackTrace();
        return false;
      }

    }
    return true;
  }

  private void insertQuestion(HashMap<String, Object> tmp) {
    try {
      Statement stmt = Question.conn.createStatement();
      int answered = 0;
      answered = (Integer)tmp.get("answered");
      String text = (String)tmp.get("text");
      String note = (String)tmp.get("note");
      int category;
      try {
        category = (Integer)tmp.get("category");
      } catch (java.lang.NullPointerException e) {
        category = -1;
      }
      int criterion = (Integer)tmp.get("criterion");
      int assessment;
      try {
        assessment = (Integer)tmp.get("assessment");
      } catch (java.lang.NullPointerException e) {
        assessment = -1;
      }
      int answer = (Integer)tmp.get("answer");
      float weight = (Float)tmp.get("weight");

      System.out.println("frage: " + text + ", Gewichtung: " + weight + ", beantwortet: " + answered);

      String mysql = "INSERT INTO at_questions (text, note, weight, answer, category, criterion, answered, assessment) VALUES "
                     + "('"
                     + text
                     + "','"
                     + note
                     + "', "
                     + weight
                     + ", "
                     + answer
                     + ", "
                     + category
                     + ", "
                     + criterion
                     + ", " + answered + ", " + assessment + ")";
      stmt.executeUpdate(mysql);
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Could not create  question", Log.STATUS_BAD, Log.DIRECTION_LOG);
    }
  }

  private void updateQuestion(HashMap<String, Object> tmp) {
    try {
      Statement stmt = Question.conn.createStatement();
      String mysql = "UPDATE at_questions SET " + " text  = '" + (String)tmp.get("text") + "', " + " note  = '"
                     + (String)tmp.get("note") + "', " + " weight  = " + ((Float)tmp.get("weight")).floatValue() + ", "
                     + " answer = " + tmp.get("answer") + ", " + " category  =" + tmp.get("category")
                     + ", criterion  =" + tmp.get("criterion") + ", answered  =" + tmp.get("answered")
                     + ", assessment  =" + tmp.get("assessment") + " WHERE id =" + tmp.get("id");
      stmt.executeUpdate(mysql);
    } catch (SQLException s) {
      s.printStackTrace();

      Log.out("Could not update  question", Log.STATUS_BAD, Log.DIRECTION_LOG);
    }
  }

  public ArrayList<HashMap<String, Object>> getQuestionsFromAssessment(int assi) {
    this.question = new ArrayList<HashMap<String, Object>>();
    try {
      Statement stmt = conn.createStatement();
      String query = "SELECT * FROM  at_questions where assessment = " + assi;
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", rs.getInt("id"));
        hashmap.put("text", rs.getString("text"));
        hashmap.put("note", rs.getString("note"));
        hashmap.put("weight", rs.getFloat("weight"));
        hashmap.put("criterion", rs.getInt("criterion"));
        hashmap.put("category", rs.getInt("category"));
        hashmap.put("answered", rs.getInt("answered"));
        hashmap.put("answer", rs.getInt("answer"));
        hashmap.put("assessment", rs.getInt("assessment"));
        this.question.add(hashmap);
      }
      rs.close();
    } catch (SQLException s) {
      s.printStackTrace();

      Log.out("Could not find  question", Log.STATUS_BAD, Log.DIRECTION_LOG);
    }

    return this.question;
  }

  public ArrayList<HashMap<String, Object>> getQuestionsFromCategory(int catId) {
    this.question = new ArrayList<HashMap<String, Object>>();
    try {
      Statement stmt = conn.createStatement();
      String query = "SELECT * FROM  at_questions where category = " + catId;
      ResultSet rs = stmt.executeQuery(query);

      while (rs.next()) {
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("id", rs.getInt("id"));
        hashmap.put("text", rs.getString("text"));
        hashmap.put("note", rs.getString("note"));
        hashmap.put("weight", rs.getFloat("weight"));
        hashmap.put("criterion", rs.getInt("criterion"));
        hashmap.put("category", rs.getInt("category"));
        hashmap.put("answered", rs.getInt("answered"));
        hashmap.put("answer", rs.getInt("answer"));
        hashmap.put("assessment", rs.getInt("assessment"));
        this.question.add(hashmap);
      }
      rs.close();
    } catch (SQLException s) {
      s.printStackTrace();

      Log.out("Could not find  question", Log.STATUS_BAD, Log.DIRECTION_LOG);
    }

    return this.question;
  }

  public boolean answerQuestion(int id, int a) {
    try {
      Statement stmt = Question.conn.createStatement();
      String mysql = "UPDATE at_questions SET answer = " + a + " WHERE id =" + id;
      stmt.executeUpdate(mysql);
      return true;
    } catch (SQLException s) {
      s.printStackTrace();
      return false;
    }
  }
}
