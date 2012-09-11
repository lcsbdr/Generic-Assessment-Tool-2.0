package de.assessmenttool.assessments;

import java.util.HashMap;

public class Criterion {

  /** The database id. */
  private final int id;

  /** The name of this criterion. */
  private String name;

  /** All parameters of this criterion. */
  private final HashMap<String, String> params;

  /** The criterion type. */
  private CriterionType critType;

  /**
   * All possible criterion types.
   */
  public enum CriterionType {
    NUMERIC, ALPHANUMERIC, YESNO
  }

  /**
   * Creates a new criterion with the given parameters.
   * 
   * @param id The id.
   * @param name The name.
   * @param critType The criterion type.
   */
  public Criterion(int id, String name, CriterionType critType) {
    this.params = new HashMap<String, String>();
    this.id = id;
    this.name = name;
    this.critType = critType;
  }

  public Criterion(int id, String name, String critType) {
    this.params = new HashMap<String, String>();
    this.id = id;
    this.name = name;

    for (CriterionType tmp : CriterionType.values()) {
      if (tmp.toString().equals(critType)) {
        this.critType = tmp;
      }
    }
  }

  /**
   * Adds a new parameter to this criterion.
   * 
   * @param key The key to add.
   * @param val The value to add.
   */
  public void addParameter(String key, String val) {
    this.params.put(key, val);
  }

  /**
   * Get the name of this criterion.
   * 
   * @return The name of this criterion.
   */
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get a HashMap<String, String> of all parameters.
   * 
   * @return A Map of all parameters.
   */
  public HashMap<String, String> getParams() {
    return this.params;
  }

  /**
   * Get the criterion type of this criterion.
   * 
   * @return The criterion type.
   */
  public CriterionType getType() {
    return this.critType;
  }

  /**
   * Get the value for a given parameter.
   * 
   * @param key The key for the given parameter.
   * @return The value to the given key.
   */
  public String getValue(String key) {
    return this.params.get(key);
  }

  /**
   * Get the key for a given value.
   * 
   * @param val The given value.
   * @return The key to the given value.
   */
  public String getKey(String val) {
    for (int i = 1; i <= this.params.size(); i++) {
      if (this.params.get(String.valueOf(i)).equals(val)) return String.valueOf(i);
    }
    return "";
  }

  /**
   * Get the database id.
   * 
   * @return The database id.
   */
  public int getId() {
    return this.id;
  }

  @Override
  public String toString() {
    return this.name;
  }

  public void setCriterionType(CriterionType obj) {
    this.critType = obj;
  }

  public void removeParameter(String param) {
    this.params.remove(param);
  }
}
