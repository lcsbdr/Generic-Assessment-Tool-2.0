package de.assessmenttool.kernel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Criterion.CriterionType;

public interface DatabaseIF {
  /** version number */
  public static final String VER = "$Revision$";

  /**
   * Disconnects from the database.
   * 
   * @return <code>true</code>, if the disconnect was successful, otherwise <code>false</code>.
   * @throws SQLException
   */
  public boolean disconnect() throws SQLException;

  /**
   * Connect to a new internal database.
   * 
   * @return true, if the connect was successful.
   */
  public boolean dbConnectIntern();

  /**
   * Connect to an external MySQL database with the given information.
   * 
   * @param driver The database driver.
   * @param db The database.
   * @param user The database user.
   * @param pwd The user password.
   * @return true, if the connect was successful.
   */
  public boolean dbConnectExtern(String host, String port, String db, String user, String pwd);

  /**
   * Returns a list of maps containing all assessments.
   * Keys which the map explicitly contains are: ["id", "name", "depth3d", "creation", "author"]
   * 
   * @return A list of maps containing all assessments.
   */
  public ArrayList<HashMap<String, Object>> getAssessments();

  /**
   * Saves the given assessments.
   * Keys which the map explicitly contains are: ["id", "name", "depth3d", "creation", "author"]
   * If "id" is null a new entry will be generated in the database, otherwise an existing entry will be updated.
   * 
   * @param assessments All assessments to save.
   * @return <code>true</code>, if the assessments were saved correctly, otherwise <code>false</code>.
   */
  public boolean saveAssessments(ArrayList<HashMap<String, Object>> assessments);

  /**
   * Returns a list of maps containing all categories for a specific category.
   * Keys which the map explicitly contains are: ["id", "name", "note", "weight", "assessment", "category"]
   * 
   * @return A list of maps containing all categories of a category.
   */
  public ArrayList<HashMap<String, Object>> getCategories(int categoryId);

  /**
   * Returns a list of maps containing all categories for a specific assessment.
   * Keys which the map explicitly contains are: ["id", "name", "note", "weight", "assessment", "category"]
   * 
   * @return A list of maps containing all categories of a category.
   */
  public ArrayList<HashMap<String, Object>> getCategoriesFromAssessment(int categoryId);

  /**
   * Returns a list of maps containing all questions for a specific assessment.
   * Keys which the map explicitly contains are: [QUESTMAP]
   * 
   * @return A list of maps containing all questions of an assessment.
   */
  public ArrayList<HashMap<String, Object>> getQuestionsFromAssessment(int assiId);

  /**
   * Returns a list of maps containing all questions for a specific category.
   * Keys which the map explicitly contains are: [QUESTMAP]
   * 
   * @return A list of maps containing all questions of a category.
   */
  public ArrayList<HashMap<String, Object>> getQuestionsFromCategory(int catId);

  /**
   * Saves the given categories.
   * Keys which the map explicitly contains are: ["id", "name", "note", "weight", "assessment", "category"]
   * 
   * @param categories All categories to save.
   * @return <code>true</code>, if the categories were saved correctly, otherwise <code>false</code>.
   */
  public boolean saveCategories(ArrayList<HashMap<String, Object>> categories);

  /**
   * Returns a list of maps containing all questions for a specific category.
   * Keys which the map explicitly contains are: ["id", "text", "weight", "answered", "note", "category", "criterion",
   * "assessment"]
   * 
   * @return A list of maps containing all questions of a category.
   */
  public ArrayList<HashMap<String, Object>> getQuestions(int categoryId);

  /**
   * Saves the given questions.
   * Keys which the map explicitly contains are: ["id", "text", "weight", "answered", "note", "category", "criterion",
   * "assessment", "answer"]
   * 
   * @param questions All questions to save.
   * @return <code>true</code>, if the questions were saved correctly, otherwise <code>false</code>.
   */
  public boolean saveQuestions(ArrayList<HashMap<String, Object>> questions);

  /**
   * Deletes the given assessment.
   * 
   * @param assessmentId The assessment to delete.
   * @return <code>true</code>, if the assessment was deleted correctly, otherwise <code>false</code>.
   */
  public boolean deleteAssessment(int assessmentId);

  /**
   * Deletes the given category.
   * 
   * @param categoryId The category to delete.
   * @return <code>true</code>, if the category was deleted correctly, otherwise <code>false</code>.
   */
  public boolean deleteCategory(int categoryId);

  /**
   * Deletes the given question.
   * 
   * @param questionId The question to delete.
   * @return <code>true</code>, if the question was deleted correctly, otherwise <code>false</code>.
   */
  public boolean deleteQuestion(int questionId);

  /**
   * Returns a list of maps containing all criteria.
   * Keys which the map explicitly contains are: ["id", "name", "critType"]
   * 
   * @return A list of maps containing all criteria.
   */
  public ArrayList<HashMap<String, Object>> getCriteria();

  /**
   * Returns a list of maps containing all possible criteria.
   * Keys which the map explicitly contains are: ["id", "type"]
   * 
   * @return A list of maps containing all possible criteria.
   */
  public ArrayList<HashMap<String, Object>> getPossibleCriteria();

  /**
   * Returns a list of maps containing all parameters of a criterion.
   * Keys which the map explicitly contains are: ["id", "key", "value", "criterion"]
   * 
   * @return A list of maps containing all parameters of a criterion.
   */
  public ArrayList<HashMap<String, Object>> getCriterionParameters(int criterionId);

  /**
   * Saves a given CriterionType in the database.
   * 
   * @param crit The criterion type to save.
   * @return <code>true</code>, if the CriterionType was saved correctly, otherwise <code>false</code>.
   */
  public boolean saveCriterionType(CriterionType crit);

  /**
   * Saves a given Criterion in the database.
   * 
   * @param crit The criterion to save.
   * @return <code>true</code>, if the Criterion was saved correctly, otherwise <code>false</code>.
   */
  public boolean saveCriterion(Criterion crit);

  /**
   * Saves the criterion parameters for a given criterion.
   * 
   * @param crit The criterion for which the parameters should be saved.
   * @return <code>true</code>, if the criterion parameters were saved correctly, otherwise <code>false</code>.
   */
  public boolean saveCriterionParameters(Criterion crit);

  /**
   * Answers the given question with the given answer.
   * 
   * @param id The question to answer.
   * @param a The answer.
   * @return <code>true</code>, if the answer was saved correctly, otherwise <code>false</code>.
   */
  public boolean answerQuestion(int id, int a);
}
