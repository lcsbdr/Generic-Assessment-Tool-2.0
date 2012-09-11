package de.assessmenttool.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Criterion.CriterionType;
import de.assessmenttool.kernel.DatabaseIF;
import de.assessmenttool.kernel.Log;

public class SQLConnection implements DatabaseIF {

  Category category = new Category(this.getConn());

  Questionnaires assessment = new Questionnaires(this.getConn());

  Question quest = new Question(this.getConn());

  Criteria crit = new Criteria(this.getConn());

  Dependencies dep = new Dependencies(this.getConn());

  // Herstellen der Verbindung zur Datenbank automatisch
  // private static SQLConnection instance = null;

  private Connection conn = null;

  // Hostnames
  private static String dbHost = "jdbc:hsqldb:file:database";

  // Port -- Standard: 3306
  private static String dbPort = "3306";

  // Datenbankname
  private static String database = "Assessment";

  // Datenbankuser
  private static String dbUser = "root";

  // Datenbankpasswort
  private static String dbPassword = "";

  private final boolean tablesCreated = false;

  private final boolean deleteTablesOnStartup = false;

  public SQLConnection() {
    // try {
    //
    // // Class.forName("com.mysql.jdbc.Driver"); -> mysql
    // Class.forName("org.hsqldb.jdbcDriver");// -> hsql
    //
    // /*
    // * conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":"
    // * + dbPort + "/" + database + "?" + "user=" + dbUser + "&" +
    // * "password=" + dbPassword);
    // */
    //
    // conn = DriverManager.getConnection(dbHost + "; shutdown=true", dbUser, dbPassword);
    // createTable();
    // } catch (ClassNotFoundException e) {
    // System.out.println("Treiber nicht gefunden");
    // } catch (SQLException e) {
    // System.out.println("Connect nicht moeglich");
    // } finally {
    // try {
    // disconnect();
    // } catch (SQLException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
  }

  /*
   * public SQLConnection getInstance() {
   * if (instance == null)
   * instance = new SQLConnection();
   * System.out.println("Verbindung hergestellt");
   * return instance;
   * }
   */

  public Connection getConn() {
    return this.conn;
  }

  private void deleteTables() {
    deleteTable("AT_CATDEPMAP");
    deleteTable("AT_CATEGORIES");
    deleteTable("AT_CRITPARAMS");
    deleteTable("AT_CRITERIA");
    deleteTable("AT_CRITPOSSIBLE");
    deleteTable("at_dependenciesNew");
    deleteTable("AT_QUESTIONNAIRES");
    deleteTable("AT_USERS");
    deleteTable("AT_ROLE");
  }

  private void deleteTable(String tname) {
    String hql = "DROP TABLE " + tname;
    Statement st;
    try {
      st = this.conn.createStatement();
      st.execute(hql);
    } catch (SQLException e) {
      e.printStackTrace();
      Log.out("Could not delete Table", Log.STATUS_BAD, Log.DIRECTION_LOG);
    }
  }

  public void createTables() {
    CreateTables table = new CreateTables(this.getConn());

    table.createRoleTable();
    table.createUserTable();
    table.createAssessmentTable();
    table.createCategoryTable();
    table.createDependenciesTable();
    table.createCriteriaTable();
    table.createQuestionsTable();

    table.createCatDepTable();
    table.createCritParamsTable();
    table.createCritPossibleTable();
    table.createQuestionDepTable();
    table.createQuestionDepTable();
    // writeTestEintrag();
  }

  // TEst
  private void writeTestEintrag() {

    String hql = "INSERT INTO at_users ( name) VALUES ('test')";
    String hsql2 = "Select * FROM at_users";

    Statement st;
    try {
      st = this.conn.createStatement();
      ResultSet rs = st.executeQuery(hsql2);
      while (rs.next()) {

        System.out.println(rs.getString("name"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public ArrayList<HashMap<String, Object>> getAssessments() {
    return this.assessment.getAssessments();
  }

  @Override
  public boolean saveAssessments(ArrayList<HashMap<String, Object>> assessments) {
    return this.assessment.saveAssessments(assessments);
  }

  @Override
  public ArrayList<HashMap<String, Object>> getCategories(int categoryId) {
    return this.category.getCategories(categoryId);
  }

  @Override
  public boolean saveCategories(ArrayList<HashMap<String, Object>> categories) {
    return this.category.saveCategories(categories);
  }

  @Override
  public ArrayList<HashMap<String, Object>> getQuestions(int categoryId) {
    ArrayList<HashMap<String, Object>> list = this.quest.getQuestions(categoryId);
    return list;
  }

  @Override
  public boolean saveQuestions(ArrayList<HashMap<String, Object>> questions) {
    return this.quest.saveQuestions(questions);
  }

  @Override
  public boolean deleteAssessment(int assessmentId) {
    this.assessment.delete(assessmentId);
    return true;
  }

  @Override
  public boolean deleteCategory(int categoryId) {
    return this.category.delete(categoryId);
  }

  @Override
  public boolean deleteQuestion(int questionId) {
    return this.quest.delete(questionId);
  }

  @Override
  public ArrayList<HashMap<String, Object>> getCriteria() {
    return this.crit.getCriteria();
  }

  @Override
  public ArrayList<HashMap<String, Object>> getPossibleCriteria() {

    return this.crit.getPossibleCriteria();
  }

  @Override
  public ArrayList<HashMap<String, Object>> getCriterionParameters(int criterionId) {

    return this.crit.getCriterionParameters(criterionId);
  }

  @Override
  public boolean disconnect() throws SQLException {
    this.conn.close();
    return true;
  }

  private boolean connect(boolean isIntern) {
    try {
      if (!isIntern) {
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + database + "?"
                                                + "user=" + dbUser + "&" + "password=" + dbPassword);
      } else {
        Class.forName("org.hsqldb.jdbcDriver");// -> hsql
        this.conn = DriverManager.getConnection(dbHost + "; shutdown=true", dbUser, dbPassword);
      }
      this.assessment.setConnection(this.conn);
      this.category.setConnection(this.conn);
      this.quest.setConnection(this.conn);
      this.crit.setConnection(this.conn);
      this.dep.setConnection(this.conn);
      if (this.deleteTablesOnStartup) {
        deleteTables();
      }
      createTables();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      Log.out("Could not connect", Log.STATUS_BAD, Log.DIRECTION_LOG);
      return false;
    }
    return true;
  }

  @Override
  public boolean dbConnectIntern() {
    dbHost = "jdbc:hsqldb:file:database";
    dbPort = "3306";
    database = "Assessment";
    dbUser = "root";
    dbPassword = "";

    if (connect(true)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dbConnectExtern(String host, String port, String db, String user, String pwd) {
    SQLConnection.dbHost = host;
    SQLConnection.dbPort = port;
    SQLConnection.database = db;
    SQLConnection.dbUser = user;
    SQLConnection.dbPassword = pwd;

    if (connect(false)) {
      return true;
    }
    return false;
  }

  @Override
  public ArrayList<HashMap<String, Object>> getCategoriesFromAssessment(int assi) {
    return this.category.getCategoriesFromAssessment(assi);
  }

  @Override
  public boolean saveCriterionType(CriterionType crit) {
    return this.crit.saveCriterionType(crit);
  }

  @Override
  public boolean saveCriterion(Criterion crit) {
    return this.crit.saveCriterion(crit);
  }

  @Override
  public boolean saveCriterionParameters(Criterion crit) {
    return this.crit.saveCriterionParameters(crit);
  }

  @Override
  public ArrayList<HashMap<String, Object>> getQuestionsFromAssessment(int assi) {
    return this.quest.getQuestionsFromAssessment(assi);
  }

  @Override
  public ArrayList<HashMap<String, Object>> getQuestionsFromCategory(int catId) {
    return this.quest.getQuestionsFromCategory(catId);
  }

  @Override
  public boolean answerQuestion(int id, int a) {
    return this.quest.answerQuestion(id, a);
  }

  public boolean removeCriterion(int id) {
    return this.crit.removeCriterion(id);
  }

  public boolean removeAssessment(int id) {
    return this.assessment.delete(id);
  }

  public boolean saveDependency(HashMap<String, Object> deps) {
    return this.dep.saveDependency(deps);
  }

  public boolean removeDependency(int questId, int depQuestId) {
    return this.dep.delete(questId, depQuestId);
  }

  public ArrayList<HashMap<String, Object>> getDependencies() {
    return this.dep.getDependencies();
  }
}
