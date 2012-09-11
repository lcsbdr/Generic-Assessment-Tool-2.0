package de.assessmenttool.kernel;

import java.util.ArrayList;

import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Question;

public interface LogicIF {

  /**
   * Returns a list containing all available assessments.
   * 
   * @return A list containing all available assessments.
   */
  public ArrayList<Assessment> getAssessments();

  /**
   * Deletes the given assessment.
   * 
   * @param assessment The assessment to delete.
   */
  public void removeAssessment(Assessment assessment);

  /**
   * Creates a new assessment with the given name.
   * 
   * @param name The name of the new assessment.
   */
  public void newAssessment(String name);

  /**
   * Returns all possible criteria.
   * 
   * @return A list of all possible criteria.
   */
  public ArrayList<Criterion> getPossibleCriteria();

  /**
   * Saves a given Criterion in the database.
   * 
   * @param crit The criterion to save.
   * @return <code>true</code>, if the Criterion was saved correctly, otherwise <code>false</code>.
   */
  public void saveCriterion(Criterion crit);

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
  public boolean dbConnectExtern(String host, String port, String db, String user, char[] pwd);

  /**
   * Disconnect the current database connection.
   * 
   * @return true, if the disconnect was successful.
   */
  public boolean dbDisconnect();

  /**
   * Saves a specific question.
   * 
   * @param question The question to save.
   * @param topElement The top element (category/assessment) of this question.
   */
  public void saveQuestion(Question question, Object topElement);

  /**
   * Removes the given question.<Question:int>
   * 
   * @param question The question to remove.
   */
  public void removeQuestion(Question question);

  /**
   * Saves the specific category.
   * 
   * @param category The category to save.
   * @param topElement The top element (category/assessment) of this category.
   */
  public void saveCategory(Category category, Object topElement);

  /**
   * Removes the given category.
   * 
   * @param category The category to remove.
   */
  public void removeCategory(Category category);

  /**
   * Returns the Assessment in which a Question/Category is included
   * 
   * @param o Question or Category
   * @return the Assessment that surrounds the Question / Category
   */
  public Assessment getRelatedAssessment(Object o);

  /**
   * Answers the given question.
   * 
   * @param question
   * @param answer
   */
  public void answerQuestion(Question question, int answer);

  /**
   * Get the related category for a given question.
   * 
   * @param question
   * @return
   */
  public Category getRelatedCategory(Question question);

  /**
   * Delete the given criterion from the database.
   * 
   * @param criterion
   */
  public void removeCriterion(Criterion criterion);

  /**
   * Checks if a DB connection exists or not.
   * 
   * @return
   */
  public boolean dbConnected();

  /**
   * Exit the program.
   */
  public void quit();

  /**
   * Generate XML for the given assessment.
   * 
   * @param relatedAssessment
   */
  public void generateXML(Assessment relatedAssessment, String path);

  /**
   * Generate XLS for the given assessment.
   * 
   * @param relatedAssessment
   */
  public void generateXLS(Assessment relatedAssessment, String path);

  /**
   * Generate PDF for the given assessment.
   * 
   * @param relatedAssessment
   */
  public void generatePDF(Assessment relatedAssessment, String path);

  public void setDependency(Question activeQuestion, Question tmpQuest, Integer needtmp);

  public void removeDependency(Question activeQuestion, Question tmpQuest);

  public void saveAssessment(Assessment assi);

  public Assessment importXML(String path);
}
