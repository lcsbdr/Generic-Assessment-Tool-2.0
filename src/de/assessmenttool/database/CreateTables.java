package de.assessmenttool.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import de.assessmenttool.kernel.Log;

public class CreateTables {

  String table;

  private final Connection conn;

  public CreateTables(Connection con) {
    this.conn = con;

  }

  public void createQuestionsTable() {
    this.table = "CREATE TABLE IF NOT EXISTS  at_questions   ("
                 + "   id  int GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,"
                 + "   text  CLOB NOT NULL," + "   weight  float NOT NULL," + "   answered  tinyint NOT NULL,"
                 + "   note  CLOB NOT NULL," + "   category  int," + "   assessment  int," + "   criterion  int,"
                 + "   answer int )";

    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.execute(this.table);
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Table Questions could not be createt", Log.STATUS_BAD, Log.DIRECTION_LOG);
    } finally {
      try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void createCategoryTable() {
    this.table = "CREATE TABLE IF NOT EXISTS  at_categories  ("
                 + "   id  int GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,"
                 + "   name  CLOB NOT NULL," + "   weight  float NOT NULL," + "   note  CLOB NOT NULL,"
                 + "   category int NOT NULL," + "   assessment  int NOT NULL)";

    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.execute(this.table);
    } catch (SQLException s) {
        Log.out("Table categories could not be createt" + s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);
    } finally {
      try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void createAssessmentTable() {
    this.table = "CREATE TABLE IF NOT EXISTS  at_questionnaires ("
                 + "   id  int GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,"
                 + "   name CLOB NOT NULL," + "   author  int NOT NULL," // FOREIGN KEY references at_users(id) TODO
                 + "   depth3d  TINYINT NOT NULL," + "   creation  TIMESTAMP(8) NOT NULL)";
    // this.table = "DROP TABLE  at_questionnaires ";

    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.execute(this.table);
    } catch (SQLException s) {
        Log.out("Table at_questionnaires could not be createt" + s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);
    } finally {
      try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void createUserTable() {
    this.table = "CREATE TABLE IF NOT EXISTS at_users ("
                 + "   id  int GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,"
                 + "   username  CLOB NOT NULL," + "   passwort  CLOB NOT NULL,"
                 + "   role  int NOT NULL FOREIGN KEY references at_role(id)," + "   creation  TIMESTAMP(8))";

    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.execute(this.table);
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Table at_users could not be createt" + s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);

    } finally {
      try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void createRoleTable() {
    this.table = "CREATE TABLE IF NOT EXISTS at_role ("
                 + "  id int  GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,"
                 + "  name CLOB NOT NULL)"; // CLOB für sehr lange strings
    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.executeUpdate(this.table);
      String mysql = "INSERT INTO at_role (name) VALUES ('Admin')";
      st.executeUpdate(mysql);
    } catch (SQLException s) {
      s.printStackTrace();
      Log.out("Table at_role could not be createt" + s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);
    } finally {
      try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void createCriteriaTable() {
    this.table = "CREATE TABLE IF NOT EXISTS  at_criteria  ("
                 + "   id  int GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,"
                 + "   name  CLOB NOT NULL," + "   crittype  int NOT NULL FOREIGN KEY references at_critPossible(id))";
    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.execute(this.table);
    } catch (SQLException s) {
        Log.out("Table at_criteria could not be createt" + s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);

    } finally {
      try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void createCritPossibleTable() {
    this.table = "CREATE TABLE IF NOT EXISTS  at_critPossible  ("
                 + "   id  int GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,"
                 + "  type CLOB NOT NULL)";

    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.execute(this.table);
    } catch (SQLException s) {
        Log.out("Table at_critPossible could not be createt" + s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);

    } finally {
      try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void createCritParamsTable() {
    this.table = "CREATE TABLE IF NOT EXISTS  at_critParams  ("
                 + "   id  int GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,"
                 + "   key CLOB NOT NULL," + "  	value CLOB NOT NULL,"
                 + "   criterion  int  NOT NULL FOREIGN KEY references at_criteria(id))";
    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.execute(this.table);
    } catch (SQLException s) {
        Log.out("Table at_critParams could not be createt" + s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);

    } finally {
      try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void createCatDepTable() { // name noch einmal ï¿½berarbeiten
    this.table = "CREATE TABLE IF NOT EXISTS  at_catDepMap  ("
                 + "   category  int NOT NULL FOREIGN KEY references at_categories(id),"
            	 + "   dependency  int NOT NULL FOREIGN KEY references at_dependenciesNew(id),"
                 + "   isCondition  tinyint NOT NULL)";
    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.execute(this.table);
    } catch (SQLException s) {
        Log.out("Table at_catDepMap could not be createt" + s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);
    } finally {
      try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void createQuestionDepTable() { // name noch einmal ï¿½berarbeiten
    this.table = "CREATE TABLE IF NOT EXISTS  at_questionDepTable  ("
                 + "    question  int  NOT NULL FOREIGN KEY references at_questions(id),"
                + "   dependency  int  NOT NULL FOREIGN KEY references at_dependenciesNew(id),"
                 + "   isCondition  tinyint NOT NULL)";

    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.execute(this.table);
    } catch (SQLException s) {
        Log.out("Table at_questionDepTable could not be createt" + s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);

    } finally {
      try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void createDependenciesTable() {
    this.table = "CREATE TABLE IF NOT EXISTS  at_dependenciesNew  ("
                 + "  id int  GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,"
                 + "  quest int NOT NULL,  depQuest int NOT NULL,   completionNeeded  int NOT NULL)";

    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.execute(this.table);
    } catch (SQLException s) {
        Log.out("Table at_dependencies could not be createt" + s.getMessage(), Log.STATUS_BAD, Log.DIRECTION_LOG);

    } finally {
      try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
