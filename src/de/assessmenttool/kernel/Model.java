package de.assessmenttool.kernel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Criterion.CriterionType;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.database.SQLConnection;
import de.assessmenttool.fileIO.PDF_Generator;
import de.assessmenttool.fileIO.XLS_Generator;
import de.assessmenttool.fileIO.XML_Generator;
import de.assessmenttool.fileIO.XML_Parser;

public class Model implements LogicIF {

  /** The database connection. */
  private final SQLConnection db = new SQLConnection();

  private boolean dbAlive = false;

  /** Contains all loaded assessments. */
  private ArrayList<Assessment> assessments;

  public Model() {
    // initiate the default lists
    this.assessments = new ArrayList<Assessment>();

    // initial connect to the internal database
    if (dbConnectIntern()) {
      initialDbConnect();
    }

    // loadAssessments();
    // if (this.assessments.size() > 0) {
    // new XLS_Generator(this.assessments.get(0), "workbook");
    // new XML_Generator(this.assessments.get(0), "workbook", this);
    // new PDF_Generator(this.assessments.get(0), "workbook");
    // }
  }

  private void initialDbConnect() {
    // check if all possible criteria are in the database
    for (CriterionType tmp : CriterionType.values()) {
      if (this.db.saveCriterionType(tmp)) {
        // set status
        Log.out("All criteria adjusted.", Log.STATUS_GOOD, Log.DIRECTION_STATUS);
      } else {
        // set status
        Log.out("Can NOT adjust the criteria.", Log.STATUS_BAD, Log.DIRECTION_STATUS);
      }
    }
  }

  // TODO: Dummy
  private ArrayList<Assessment> loadAssessments() {
    // TODO load from DB
    // Test criteria
    Criterion crit_numeric_1_10 = new Criterion(1, "Numerisch 1 - 10", CriterionType.NUMERIC);
    crit_numeric_1_10.addParameter("1", "1");
    crit_numeric_1_10.addParameter("2", "2");
    crit_numeric_1_10.addParameter("3", "3");
    crit_numeric_1_10.addParameter("4", "4");
    crit_numeric_1_10.addParameter("5", "5");
    crit_numeric_1_10.addParameter("6", "6");
    crit_numeric_1_10.addParameter("7", "7");
    crit_numeric_1_10.addParameter("8", "8");
    crit_numeric_1_10.addParameter("9", "9");
    crit_numeric_1_10.addParameter("10", "10");

    Criterion crit_alphanumeric_A_F = new Criterion(2, "Alphanumerisch A - F", CriterionType.ALPHANUMERIC);
    crit_alphanumeric_A_F.addParameter("1", "A");
    crit_alphanumeric_A_F.addParameter("2", "B");
    crit_alphanumeric_A_F.addParameter("3", "C");
    crit_alphanumeric_A_F.addParameter("4", "D");
    crit_alphanumeric_A_F.addParameter("5", "E");
    crit_alphanumeric_A_F.addParameter("6", "F");

    Criterion crit_alphanumeric_A_F_mix = new Criterion(3, "Alphanumerisch A - F (mix)", CriterionType.ALPHANUMERIC);
    crit_alphanumeric_A_F_mix.addParameter("1", "F");
    crit_alphanumeric_A_F_mix.addParameter("2", "C");
    crit_alphanumeric_A_F_mix.addParameter("3", "D");
    crit_alphanumeric_A_F_mix.addParameter("4", "E");
    crit_alphanumeric_A_F_mix.addParameter("5", "A");
    crit_alphanumeric_A_F_mix.addParameter("6", "B");

    Criterion crit_yesno = new Criterion(4, "Yes/No default", CriterionType.YESNO);
    crit_yesno.addParameter("1", "Yes");
    crit_yesno.addParameter("2", "No");

    // TODO load from DB
    // Test assessment #1
    Assessment test = new Assessment(-1, "TPI Scoring");
    test.set3D(false);
    // test.setCompletion(0.0f);
    // test.setLastModificationDate(new Date());
    test.setCreation(new Date());
    test.setCreator("Mustermann");
    test.setLastModifier("Meier");

    // TOPCAT Test strategy
    Category c_testStrat = new Category("Test strategy");
    test.addCategory(c_testStrat);

    Question q_testStratTop1 = new Question("Do u like this testquestion?",
                                            "This is a very important note about this question",
                                            crit_yesno);
    test.addQuestion(q_testStratTop1);

    Question q_testStratTop2 = new Question("This is another test question",
                                            "1. Risk based test strategy - what are the risks, how are the risks classified, is "
                                                    + "test group aware of product risks 2. Test cases trace to requirements and risks (requirements "
                                                    + "coverage and risk coverage) 3. Single high level test defines its own strategy 4. Requirements "
                                                    + "based testing � test approach ensures that the product meets customer�s needs 5. Risk "
                                                    + "assessment forms the basis of test strategy, necessary to optimize the test effort (An "
                                                    + "analysis is made of what, where, and how much is to be tested to find the optimal balance "
                                                    + "between the desired quality and the amount of time/money required. 6. An optimization "
                                                    + "takes place to distribute resources among the test activities.",
                                            crit_alphanumeric_A_F_mix);

    test.addQuestion(q_testStratTop2);

    // Test strategy level A
    Category c_testStratA = new Category(c_testStrat.getName() + " Level A - Strategy for single high level test",
                                         "The customer of a test expects certain qualities of the system to be delivered which "
                                                 + "are very different for each system. It is of great importance that you can communicate "
                                                 + "with the customer about this, and, depending on the customer's demands, translate them "
                                                 + "into a test approach. A risk assessment forms the basis for the test strategy, because "
                                                 + "it is important for optimizing the test effort (that is, test coverage). In determining "
                                                 + "the strategy an analysis is made of what, where, and how much is to be tested to find the "
                                                 + "optimal balance between the desired quality and the amount of time/money required. "
                                                 + "An optimization takes place to distribute resources among the test activities.");
    c_testStrat.addCategory(c_testStratA);

    // Additional categories for more depth
    Category c_testStratA_1 = new Category("SubcategoryTest1");

    // DEPENDENCY
    // q_testStratTop1.addDependency(q_testStratTop2);

    Question q_testStratA_1_1 = new Question("subquestion test 1", crit_yesno);
    c_testStratA_1.addQuestion(q_testStratA_1_1);
    Category c_testStratA_2 = new Category("SubcategoryTest2");
    Question q_testStratA_2_1 = new Question("subquestion test 2", crit_alphanumeric_A_F);
    c_testStratA_2.addQuestion(q_testStratA_2_1);
    c_testStratA.addCategory(c_testStratA_1);
    c_testStratA.addCategory(c_testStratA_2);

    Question q_testStrat1 = new Question("A motivated consideration of the product risks takes place, for which knowledge of the "
                                                 + "system, its use and its operational management is required.",
                                         "1. Risk based test strategy - what are the risks, how are the risks classified, is "
                                                 + "test group aware of product risks 2. Test cases trace to requirements and risks (requirements "
                                                 + "coverage and risk coverage) 3. Single high level test defines its own strategy 4. Requirements "
                                                 + "based testing � test approach ensures that the product meets customer�s needs 5. Risk "
                                                 + "assessment forms the basis of test strategy, necessary to optimize the test effort (An "
                                                 + "analysis is made of what, where, and how much is to be tested to find the optimal balance "
                                                 + "between the desired quality and the amount of time/money required. 6. An optimization "
                                                 + "takes place to distribute resources among the test activities.",
                                         crit_alphanumeric_A_F_mix);
    c_testStratA.addQuestion(q_testStrat1);
    Question q_testStrat2 = new Question("There is a differentiation in test depth depending on the risks and, if present, on "
                                                 + "the acceptance criteria.  As a result, not all subsystems are tested equally thoroughly "
                                                 + "and not all quality characteristics are tested (equally thoroughly).",
                                         "1. How effective are the system tests?  How many test escapes? 2. Are other stakeholders "
                                                 + "involved in creating the test strategy? 3. Relative importance is given to subsytem and "
                                                 + "quality characterisitics.  This importance is translated into lighter or heavier test4. "
                                                 + "Do you wait to test until all subsystems are coded",
                                         crit_yesno);
    c_testStratA.addQuestion(q_testStrat2);
    Question q_testStrat3 = new Question("One or more test specification techniques are selected to meet the required depth "
                                                 + "of the test.",
                                         crit_alphanumeric_A_F);
    c_testStratA.addQuestion(q_testStrat3);
    Question q_testStrat4 = new Question("For retest also, a (simple) strategy determination takes place, in which a motivated choice "
                                                 + "of variations  between 'test solutions only' and 'full re-test'  is made.",
                                         "1. Is full regression testing done? 2. Is �thin� retest done (per defect/function/subsystem) "
                                                 + "3. Regression test suite is maintained to test new releases 4. Regression tests are automated",
                                         crit_numeric_1_10);
    c_testStratA.addQuestion(q_testStrat4);

    // TOPCAT Test specification techniques
    Category c_testSpecTec = new Category("Test specification techniques");
    test.addCategory(c_testSpecTec);

    // Test specification techniques
    Category c_testSpecA = new Category(c_testSpecTec.getName() + " Level A - Informal techniques",
                                        "Test case author has lot of freedom in inventing the test cases. Therefore the quality of "
                                                + "the test cases is depending on the knowledge of the person developing the test cases. Test "
                                                + "coverage as compared to test basis is not clear. Yet this is better than having no documented "
                                                + "test cases at all. Documenting the expected results is very important because it will be "
                                                + "difficult to make difficult calculations when being under time pressure when executing the tests.");
    c_testSpecTec.addCategory(c_testSpecA);
    Question q_testSpecTec1 = new Question("The test cases are defined according to a documented technique (Test case creation process)",
                                           "Does this person write/execute test cases? Does this group has a standard/template for "
                                                   + "writing test cases?",
                                           crit_alphanumeric_A_F_mix);
    c_testSpecA.addQuestion(q_testSpecTec1);

    // DEPENDENCY
    // q_testStratTop1.addDependency(q_testSpecTec1);

    Question q_testSpecTec2 = new Question("The technique at least consists of: a) start situation, b) change process = test actions "
                                                   + "to be performed, c) expected end result.",
                                           "1. Find components of test cases 2. Is starting situation, pre-conditions, setup etc "
                                                   + "documented? 3. Is test procedure documented? 4. Are expected results documented? 5. "
                                                   + "If you are not the author, how easy it is for you to follow directions? 6. If you execute "
                                                   + "tests written by somebody else, do you have any problems running the tests? 7. Does it give "
                                                   + "you enough flexibility to deviate from the norm?",
                                           crit_numeric_1_10);
    c_testSpecA.addQuestion(q_testSpecTec2);

    this.assessments = new ArrayList<Assessment>();
    this.assessments.add(test);
    return this.assessments;
  }

  /**
   * Get the logic interface.
   * 
   * @return The used logic interface.
   */
  public LogicIF getLogicIF() {
    return this;
  }

  @Override
  public ArrayList<Assessment> getAssessments() {
    // TODO load the dummy assessment
    ArrayList<Assessment> assessments = new ArrayList<Assessment>(); // loadAssessments();

    // load all available assessments
    if (this.dbAlive) {
      ArrayList<HashMap<String, Object>> assis = this.db.getAssessments();
      for (HashMap<String, Object> tmp : assis) {
        Assessment assi = new Assessment((Integer)tmp.get("id"), (String)tmp.get("name"));
        assessments.add(assi);

        assi.setCategories(getCategoriesFromAssessment(assi));
        for (Category tmp1 : assi.getCategories()) {
          tmp1.setCategories(getCategoriesFromCategory(tmp1));
          loadSubCategories(tmp1);
        }

        assi.setQuestions(getQuestsFromAssessment(assi));
        for (Category tmp1 : assi.getCategories()) {
          tmp1.setQuestions(getQuestsFromCategory(tmp1));
          loadSubQuestions(tmp1);
        }
      }
    } else {
      Log.out("No database connection available.", Log.STATUS_BAD, Log.DIRECTION_BOTH);
    }
    this.assessments = assessments;

    // load the dependencies
    for (Assessment assi : assessments) {
      loadDependencies(assi);
    }

    return assessments;
  }

  private void loadDependencies(Assessment assi) {
    ArrayList<HashMap<String, Object>> deps = this.db.getDependencies();

    for (HashMap<String, Object> tmp : deps) {
      int quest = (Integer)tmp.get("quest");
      int depQuest = (Integer)tmp.get("depQuest");
      int completionNeeded = (Integer)tmp.get("completionNeeded");

      Question tmpQ = getQuestionFromId(quest);
      if (tmpQ == null) break;
      Question depQ = getQuestionFromId(depQuest);
      if (depQ == null) break;
      tmpQ.addDependency(depQ, completionNeeded);
    }
  }

  private Question getQuestionFromId(int qId) {
    for (Assessment tmpA : this.assessments) {
      for (Question tmpQ : tmpA.getAllQuestions()) {
        if (tmpQ.getId() == qId) {
          return tmpQ;
        }
      }
    }
    return null;
  }

  private ArrayList<Criterion> getCriteria() {
    ArrayList<Criterion> criteria = new ArrayList<Criterion>();

    // load all available criteria
    if (this.dbAlive) {
      ArrayList<HashMap<String, Object>> crits = this.db.getCriteria();

      for (HashMap<String, Object> tmp : crits) {
        Criterion c = new Criterion((Integer)tmp.get("id"), (String)tmp.get("name"), (String)tmp.get("critType"));
        ArrayList<HashMap<String, Object>> critParams = this.db.getCriterionParameters(c.getId());
        for (HashMap<String, Object> tmpParam : critParams) {
          c.addParameter((String)tmpParam.get("key"), (String)tmpParam.get("value"));
        }
        criteria.add(c);
      }
    } else {
      Log.out("No database connection available.", Log.STATUS_BAD, Log.DIRECTION_BOTH);
    }

    return criteria;
  }

  private void loadSubQuestions(Category cat) {
    cat.setQuestions(getQuestsFromCategory(cat));
    for (Category tmp : cat.getCategories()) {
      loadSubQuestions(tmp);
    }
  }

  private void loadSubCategories(Category cat) {
    cat.setCategories(getCategoriesFromCategory(cat));
    for (Category tmp : cat.getCategories()) {
      loadSubCategories(tmp);
    }
  }

  private ArrayList<Category> getCategoriesFromAssessment(Assessment a) {
    ArrayList<Category> cats = new ArrayList<Category>();

    ArrayList<HashMap<String, Object>> catData = this.db.getCategoriesFromAssessment(a.getId());
    for (HashMap<String, Object> tmp : catData) {
      String note = "";
      if (!((String)tmp.get("note")).equals("null")) note = (String)tmp.get("note");
      Category cat = new Category((String)tmp.get("name"), note, (Float)tmp.get("weight"));
      cat.setId((Integer)tmp.get("id"));
      cats.add(cat);
    }

    return cats;
  }

  private ArrayList<Question> getQuestsFromCategory(Category cat) {
    ArrayList<Question> quests = new ArrayList<Question>();

    ArrayList<HashMap<String, Object>> questData = this.db.getQuestionsFromCategory(cat.getId());
    for (HashMap<String, Object> tmp : questData) {
      String note = "";
      if (!((String)tmp.get("note")).equals("null")) note = (String)tmp.get("note");

      Criterion theCrit = null;
      ArrayList<Criterion> crits = getPossibleCriteria();
      for (Criterion tmpCrit : crits) {
        if (tmpCrit.getId() == (Integer)tmp.get("criterion")) {
          theCrit = tmpCrit;
        }
      }

      Question quest = new Question((String)tmp.get("text"), note, theCrit);
      quest.setWeight((Float)tmp.get("weight"));
      quest.setId((Integer)tmp.get("id"));
      quest.setAnswer((Integer)tmp.get("answer"));

      quests.add(quest);
    }

    return quests;
  }

  private ArrayList<Question> getQuestsFromAssessment(Assessment a) {
    ArrayList<Question> quests = new ArrayList<Question>();

    ArrayList<HashMap<String, Object>> questData = this.db.getQuestionsFromAssessment(a.getId());
    for (HashMap<String, Object> tmp : questData) {
      String note = "";
      if (!((String)tmp.get("note")).equals("null")) note = (String)tmp.get("note");

      Criterion theCrit = null;
      ArrayList<Criterion> crits = getPossibleCriteria();
      for (Criterion tmpCrit : crits) {
        if (tmpCrit.getId() == (Integer)tmp.get("criterion")) {
          theCrit = tmpCrit;
        }
      }

      Question quest = new Question((String)tmp.get("text"), note, theCrit);
      quest.setWeight((Float)tmp.get("weight"));
      quest.setId((Integer)tmp.get("id"));
      quest.setAnswer((Integer)tmp.get("answer"));

      quests.add(quest);
    }

    return quests;
  }

  private ArrayList<Category> getCategoriesFromCategory(Category cat) {
    ArrayList<Category> cats = new ArrayList<Category>();
    ArrayList<HashMap<String, Object>> catData = this.db.getCategories(cat.getId());
    for (HashMap<String, Object> tmp : catData) {
      String note = "";
      if (!((String)tmp.get("note")).equals("null")) note = (String)tmp.get("note");
      Category cat1 = new Category((String)tmp.get("name"), note, (Float)tmp.get("weight"));
      cat1.setId((Integer)tmp.get("id"));
      cats.add(cat1);
    }

    return cats;
  }

  @Override
  public ArrayList<Criterion> getPossibleCriteria() {
    // get all possible criteria
    return getCriteria();
  }

  @Override
  public boolean dbConnectIntern() {
    if (this.db.dbConnectIntern()) {
      // set status
      Log.out("Database connection successful.", Log.STATUS_GOOD, Log.DIRECTION_BOTH);
      this.dbAlive = true;
      return true;
    } else {
      // set status
      Log.out("Database connection NOT successful.", Log.STATUS_BAD, Log.DIRECTION_BOTH);
      return false;
    }
  }

  @Override
  public boolean dbConnectExtern(String host, String port, String db, String user, char[] pwd) {
    if (this.db.dbConnectExtern(host, port, db, user, String.valueOf(pwd))) {
      // set status
      Log.out("Database connection successful.", Log.STATUS_GOOD, Log.DIRECTION_BOTH);
      this.dbAlive = true;
      return true;
    } else {
      // set status
      Log.out("Database connection NOT successful.", Log.STATUS_BAD, Log.DIRECTION_BOTH);
      return false;
    }
  }

  @Override
  public boolean dbDisconnect() {
    try {
      if (this.db.disconnect()) {
        this.dbAlive = false;
        return true;
      } else {
        return false;
      }
    } catch (SQLException e) {
      // set status
      Log.out("Database NOT successful disconnected.", Log.STATUS_BAD, Log.DIRECTION_BOTH);
      return false;
    }
  }

  @Override
  public void saveQuestion(Question question, Object topElement) {
    ArrayList<HashMap<String, Object>> questions = new ArrayList<HashMap<String, Object>>();

    HashMap<String, Object> quest2save = new HashMap<String, Object>();
    if (question.getId() == -1) {
      quest2save.put("id", null);
    } else {
      quest2save.put("id", question.getId());
    }
    quest2save.put("text", question.getName());
    quest2save.put("weight", question.getWeight());
    quest2save.put("answered", 0);
    quest2save.put("note", question.getNote());
    quest2save.put("criterion", question.getCriterion().getId());
    if (topElement instanceof Category) {
      quest2save.put("category", ((Category)topElement).getId());
      quest2save.put("assessment", null);
    } else {
      quest2save.put("assessment", ((Assessment)topElement).getId());
      quest2save.put("category", null);
    }
    quest2save.put("answer", question.getAnswerInt());
    questions.add(quest2save);

    if (this.db.saveQuestions(questions)) {
      // set status
      Log.out("New question created.", Log.STATUS_GOOD, Log.DIRECTION_STATUS);
    } else {
      // set status
      Log.out("Can NOT create a new question.", Log.STATUS_BAD, Log.DIRECTION_STATUS);
    }
  }

  @Override
  public void newAssessment(String name) {
    ArrayList<HashMap<String, Object>> ass2save = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> assessmentData = new HashMap<String, Object>();

    assessmentData.put("id", null);
    assessmentData.put("name", name);
    assessmentData.put("depth3d", false);
    assessmentData.put("creation", new Date());
    assessmentData.put("author", 0);
    ass2save.add(assessmentData);

    if (this.db.saveAssessments(ass2save)) {
      // set status
      Log.out("New assessment created.", Log.STATUS_GOOD, Log.DIRECTION_STATUS);
    } else {
      // set status
      Log.out("Can NOT create a new assessment.", Log.STATUS_BAD, Log.DIRECTION_STATUS);
    }
  }

  @Override
  public void saveCategory(Category category, Object topElement) {
    ArrayList<HashMap<String, Object>> cats = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> cat2save = new HashMap<String, Object>();
    if (category.getId() == -1) {
      cat2save.put("id", null);
    } else {
      cat2save.put("id", category.getId());
    }
    cat2save.put("name", category.getName());
    cat2save.put("weight", category.getWeight());
    cat2save.put("note", category.getNote());
    if (topElement instanceof Category) {
      cat2save.put("category", ((Category)topElement).getId());
      cat2save.put("assessment", null);
    } else {
      cat2save.put("assessment", ((Assessment)topElement).getId());
      cat2save.put("category", null);
    }
    cats.add(cat2save);

    if (this.db.saveCategories(cats)) {
      // set status
      Log.out("New category created.", Log.STATUS_GOOD, Log.DIRECTION_STATUS);
    } else {
      // set status
      Log.out("Can NOT create a new category.", Log.STATUS_BAD, Log.DIRECTION_STATUS);
    }
  }

  @Override
  public void removeCategory(Category category) {
    for (Question tmp : category.getAllQuestions(category)) {
      removeQuestion(tmp);
    }
    deleteSubCategories(category);

    if (this.db.deleteCategory(category.getId())) {
      // set status
      Log.out("Category deleted.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
    } else {
      // set status
      Log.out("Can NOT delete the category.", Log.STATUS_BAD, Log.DIRECTION_STATUS);
    }
  }

  @Override
  public void saveCriterion(Criterion crit) {
    if (this.db.saveCriterion(crit)) {
      // save all criterion parameters
      if (this.db.saveCriterionParameters(crit)) {
        // set status
        Log.out("Criterion parameters saved.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
      } else {
        // set status
        Log.out("Can NOT save the criterion parameters.", Log.STATUS_BAD, Log.DIRECTION_STATUS);
      }
      // set status
      Log.out("Criterion saved.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
    } else {
      // set status
      Log.out("Can NOT save the criterion.", Log.STATUS_BAD, Log.DIRECTION_STATUS);
    }
  }

  @Override
  public void removeQuestion(Question q) {
    for (Question tmp : q.getDependencies()) {
      this.db.removeDependency(q.getId(), tmp.getId());
    }

    if (this.db.deleteQuestion(q.getId())) {
      // set status
      Log.out("Question deleted.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
    } else {
      // set status
      Log.out("Can NOT delete the question.", Log.STATUS_BAD, Log.DIRECTION_STATUS);
    }
  }

  private Assessment findCategoryInAssessment(Assessment tmp, Category cat) {
    Assessment result = null;

    for (Category tmpCat : tmp.getCategories()) {
      if (tmpCat.getId() == cat.getId()) {
        result = tmp;
      } else {
        result = findCategoryInAssessment(tmp, tmpCat, cat);
      }
      if (result != null) return result;
    }

    return result;
  }

  private Assessment findCategoryInAssessment(Assessment tmp, Category search, Category cat2find) {
    Assessment result = null;

    for (Category tmpCat : search.getCategories()) {
      if (tmpCat.getId() == cat2find.getId()) {
        result = tmp;
      } else {
        result = findCategoryInAssessment(tmp, tmpCat, cat2find);
      }
      if (result != null) return result;
    }

    return result;
  }

  @Override
  public Assessment getRelatedAssessment(Object o) {
    if (o instanceof Assessment) {
      return (Assessment)o;
    } else if (o instanceof Category) {
      Assessment usedAss = null;
      Category usedCat = (Category)o;
      for (Assessment tmp : this.assessments) {
        usedAss = findCategoryInAssessment(tmp, usedCat);
        if (usedAss != null) return usedAss;
      }
      return usedAss;
    } else if (o instanceof Question) {
      Question question = (Question)o;
      for (Assessment tmp : this.assessments) {
        for (Question tmpQuest : tmp.getAllQuestions()) {
          if (tmpQuest.getId() == question.getId()) {
            return tmp;
          }
        }
      }
    } else {
      Log.out("ERROR: getRelatedAssessment(Object o)\n\to should be type of Assessment/Category/Question",
              Log.STATUS_BAD,
              Log.DIRECTION_BOTH);
    }
    return this.assessments.get(0);
  }

  @Override
  public void answerQuestion(Question question, int a) {
    for (Question tmp : getRelatedAssessment(question).getAllQuestions()) {
      if (tmp.getId() == question.getId()) {
        question.setAnswer(a);
        tmp.setAnswer(a);
        break;
      }
    }
    if (this.db.answerQuestion(question.getId(), a)) {
      // set status
      Log.out("Question answered.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
    } else {
      // set status
      Log.out("Database error: can not answer the question.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
    }
  }

  @Override
  public Category getRelatedCategory(Question question) {
    Category usedCat = null;

    for (Assessment tmp : this.assessments) {
      for (Category tmpCat : tmp.getCategories()) {
        for (Question quest : tmpCat.getQuestions()) {
          if (quest == question) {
            return tmpCat;
          } else
            usedCat = getRelCatFromCat(tmpCat, question);
        }
      }
    }

    return usedCat;
  }

  private Category getRelCatFromCat(Category cat, Question question) {
    Category usedCat = null;

    for (Category tmpCat : cat.getCategories()) {
      for (Question quest : tmpCat.getQuestions()) {
        if (quest == question) {
          return tmpCat;
        } else
          usedCat = getRelCatFromCat(tmpCat, question);
      }
    }

    return usedCat;
  }

  @Override
  public void removeCriterion(Criterion criterion) {
    if (this.db.removeCriterion(criterion.getId())) {
      // set status
      Log.out("Criterion deleted.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
    } else {
      // set status
      Log.out("Database error: can not delete the criterion.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
    }
  }

  @Override
  public boolean dbConnected() {
    return this.dbAlive;
  }

  @Override
  public void removeAssessment(Assessment assessment) {
    for (Question tmp : assessment.getAllQuestions()) {
      removeQuestion(tmp);
    }

    for (Category tmp : assessment.getCategories()) {
      deleteSubCategories(tmp);
      removeCategory(tmp);
    }

    if (this.db.removeAssessment(assessment.getId())) {
      // set status
      Log.out("Assessment deleted.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
    } else {
      // set status
      Log.out("Database error: can not delete the assessment.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
    }
  }

  private void deleteSubCategories(Category cat) {
    for (Category tmp : cat.getCategories()) {
      deleteSubCategories(tmp);
      removeCategory(tmp);
    }
  }

  @Override
  public void quit() {
    if (this.dbAlive) {
      try {
        this.db.disconnect();
      } catch (SQLException e) {
        // set status
        Log.out("Database error: disconnect failed.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
      }
    }

    System.exit(0);
  }

  @Override
  public void generateXML(Assessment relatedAssessment, String path) {
    new XML_Generator(relatedAssessment, path, this);
  }

  @Override
  public void setDependency(Question activeQuestion, Question tmpQuest, Integer needtmp) {
    activeQuestion.addDependency(tmpQuest, needtmp);

    HashMap<String, Object> assessmentData = new HashMap<String, Object>();

    assessmentData.put("id", null);
    assessmentData.put("quest", activeQuestion.getId());
    assessmentData.put("depQuest", tmpQuest.getId());
    assessmentData.put("completionNeeded", needtmp);

    if (this.db.saveDependency(assessmentData)) {
      // set status
      Log.out("New dependency created.", Log.STATUS_GOOD, Log.DIRECTION_STATUS);
    } else {
      // set status
      Log.out("Can NOT create a new dependency.", Log.STATUS_BAD, Log.DIRECTION_STATUS);
    }
  }

  @Override
  public void removeDependency(Question activeQuestion, Question tmpQuest) {
    activeQuestion.removeDependency(tmpQuest);
    if (this.db.removeDependency(activeQuestion.getId(), tmpQuest.getId())) {
      // set status
      Log.out("Dependency deleted.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
    } else {
      // set status
      Log.out("Database error: can not delete the dependency.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
    }
  }

  @Override
  public void generateXLS(Assessment relatedAssessment, String path) {
    new XLS_Generator(relatedAssessment, path);
  }

  @Override
  public void generatePDF(Assessment relatedAssessment, String path) {
    new PDF_Generator(relatedAssessment, path);
  }

  @Override
  public void saveAssessment(Assessment assi) {
    newAssessment(assi.getName());

    for (Question tmp : assi.getQuestions()) {
      saveQuestion(tmp, assi);
    }

    for (Category tmpCat : assi.getCategories()) {
      saveCategory(tmpCat, assi);
      saveCats(tmpCat);
    }
  }

  private void saveCats(Category cat) {
    for (Category tmp : cat.getCategories()) {
      saveCategory(tmp, cat);
      for (Question tmpQuest : tmp.getQuestions()) {
        saveQuestion(tmpQuest, tmp);
      }
      saveCats(tmp);
    }
  }

  @Override
  public Assessment importXML(String path) {
    XML_Parser imp = new XML_Parser(path, this);
    return imp.getAssessment();
  }
}
