package de.assessmenttool.assessments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Question {

  /** The database id. */
  private int id = -1;

  /** The name / text of this question. */
  private String name;

  /** The note / comment of this questions. */
  private String note;

  /** The answer of this question. */
  private int answer = 0;

  /** Indicates whether this question is answered correctly or not. */
  private boolean answered;

  /** The weight of this category. */
  private float weight;

  /** The criterion to answer this question. */
  private Criterion criterion;

  /** The dependencies of this question. */
  private ArrayList<Question> dependencies = new ArrayList<Question>();

  private ArrayList<Integer> answersNeeded = new ArrayList<Integer>();

  /**
   * Add a new question to the dependencies map.
   * 
   * @param quest
   */
  public void addDependency(Question quest, int answerNeeded) {
    this.dependencies.add(quest);
    this.answersNeeded.add(answerNeeded);
  }

  public boolean hasDependencies() {
    if (this.dependencies.size() > 0) {
      return true;
    }
    return false;
  }

  public void removeDependencies() {
    this.dependencies = new ArrayList<Question>();
    this.answersNeeded = new ArrayList<Integer>();
  }

  public ArrayList<Question> getDependencies() {
    return this.dependencies;
  }

  public ArrayList<Integer> getAnswersNeeded() {
    return this.answersNeeded;
  }

  public void setDependencies(HashMap<Question, Integer> vals) {
    Iterator<?> it = vals.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<?, ?> pairs = (Map.Entry<?, ?>)it.next();
      addDependency((Question)pairs.getKey(), ((Integer)pairs.getValue()) + 1);
    }
  }

  public boolean isLocked() {
    Question tmp;
    for (int i = 0; i < this.dependencies.size(); i++) {
      tmp = this.dependencies.get(i);
      if (tmp.getAnswerInt() == 0 || tmp.getAnswerInt() > this.answersNeeded.get(i)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Create a new question with the given name and criterion.
   * 
   * @param name The name.
   * @param crit The criterion.
   */
  public Question(String name, Criterion crit) {
    this.name = name;
    this.criterion = crit;
  }

  /**
   * Create a new question with the given name, note and criterion.
   * 
   * @param name The name.
   * @param note The note.
   * @param crit The criterion.
   */
  public Question(String name, String note, Criterion crit) {
    this(name, crit);
    this.note = note;
  }

  /**
   * Sets the weight of this category.
   * 
   * @param weight The weight to set.
   */
  public void setWeight(float weight) {
    this.weight = weight;
  }

  /**
   * Get the weight of this category.
   * 
   * @return The weight of this category.
   */
  public float getWeight() {
    return this.weight;
  }

  /**
   * Get the name of this question.
   * 
   * @return The name of this question.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Get the database id.
   * 
   * @return The database id.
   */
  public int getId() {
    return this.id;
  }

  /**
   * Set the name of this question.
   * 
   * @param name The name of this question.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns if this question is answered correctly or not.
   * 
   * @return <code>true</code>, if this question is answered correctly, otherwise <code>false</code>.
   */
  public boolean isAnswered() {
    if (this.answer > 0) {
      this.answered = true;
    }

    return this.answered;
  }

  /**
   * Get the note of this question.
   * 
   * @return The note of this question.
   */
  public String getNote() {
    return this.note;
  }

  /**
   * Set the note of this question.
   * 
   * @param note The note to set.
   */
  public void setNote(String note) {
    this.note = note;
  }

  /**
   * Get the answer of this question.
   * 
   * @return The answer of this question.
   */
  public String getAnswer() {
    return this.criterion.getValue(String.valueOf(this.answer));
  }

  public float getAnswered() {

    int sumParams = getCriterion().getParams().size() - 1;
    int qAnswer = getAnswerInt() - 1;
    float prog = (((float)(sumParams - qAnswer) / sumParams));

    prog = prog * 100.0f;
    if (prog > 100.0f) prog = 100.0f;

    return prog;
  }

  /**
   * Get the answer of this question.
   * 
   * @return The answer of this question.
   */
  public int getAnswerInt() {
    return this.answer;
  }

  /**
   * Set the answer of this question.
   * 
   * @param a The answer of this question.
   */
  public void setAnswer(int a) {
    this.answer = a;
  }

  /**
   * Get the criterion of this question.
   * 
   * @return The criterion of this question.
   */
  public Criterion getCriterion() {
    return this.criterion;
  }

  @Override
  public String toString() {
    return this.name;
  }

  /**
   * Set the criterion of this question.
   * 
   * @param crit The criterion to set.
   */
  public void setCriterion(Criterion crit) {
    this.criterion = crit;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void removeDependency(Question tmpQuest) {
    for (int i = 0; i < this.dependencies.size(); i++) {
      if (this.dependencies.get(i) == tmpQuest) {
        this.dependencies.remove(i);
        this.answersNeeded.remove(i);
        break;
      }
    }
  }
}
