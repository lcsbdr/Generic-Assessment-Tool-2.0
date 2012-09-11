package de.assessmenttool.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Criterion.CriterionType;
import de.assessmenttool.assessments.Question;

public class LogicProgressTest {

  @Test
  public void testYesNoCrit() {
    // Test yes/no criterion
    Criterion crit_yesno = new Criterion(0, "Yes/No", CriterionType.YESNO);
    crit_yesno.addParameter("1", "Yes");
    crit_yesno.addParameter("2", "Yes");
    Question q1 = new Question("Test question 1", crit_yesno);
    Category c1 = new Category("Test category 1");
    c1.addQuestion(q1);

    assertEquals("Progress calculation wrong.", c1.getAnswered(), 0.0, 0.0);
    q1.setAnswer(1);
    assertEquals("Progress calculation wrong.", c1.getAnswered(), 100.0, 0.0);
    q1.setAnswer(2);
    assertEquals("Progress calculation wrong.", c1.getAnswered(), 0.0, 0.0);
  }

  @Test
  public void testNumCrit() {
    // Test num criterion
    Criterion crit_num = new Criterion(1, "Numeric 1 to 10 linear", CriterionType.NUMERIC);
    crit_num.addParameter("1", "1"); // 9/9 %
    crit_num.addParameter("2", "2"); // 8/9
    crit_num.addParameter("3", "3"); // 7/9
    crit_num.addParameter("4", "4"); // 6/9
    crit_num.addParameter("5", "5"); // 5/9
    crit_num.addParameter("6", "6"); // 4/9
    crit_num.addParameter("7", "7"); // 3/9
    crit_num.addParameter("8", "8"); // 2/9
    crit_num.addParameter("9", "9"); // 1/9 %
    crit_num.addParameter("10", "10"); // 0 %
    Question q1 = new Question("Test question 1", crit_num);
    Category c1 = new Category("Test category 1");
    c1.addQuestion(q1);

    assertEquals("Progress calculation wrong.", c1.getAnswered(), 0.0, 0.0);
    q1.setAnswer(1);
    assertEquals("Progress calculation wrong.", c1.getAnswered(), 100.0, 0.0);
    q1.setAnswer(10);
    assertEquals("Progress calculation wrong.", c1.getAnswered(), 0.0, 0.0);
    q1.setAnswer(5);
    assertEquals("Progress calculation wrong.", c1.getAnswered(), (5.0f / 9.0f) * 100.0f, 0.0);
  }

  @Test
  public void testAlphaNumCrit() {
    // Test num criterion
    Criterion crit_num = new Criterion(1, "Alphanumeric A to C linear", CriterionType.ALPHANUMERIC);
    crit_num.addParameter("1", "A"); // 2/2 %
    crit_num.addParameter("2", "B"); // 1/2 %
    crit_num.addParameter("3", "C"); // 0/2 %
    Question q1 = new Question("Test question 1", crit_num);
    Category c1 = new Category("Test category 1");
    c1.addQuestion(q1);

    assertEquals("Progress calculation wrong.", c1.getAnswered(), 0.0, 0.0);
    q1.setAnswer(1);
    assertEquals("Progress calculation wrong.", c1.getAnswered(), 100.0, 0.0);
    q1.setAnswer(2);
    assertEquals("Progress calculation wrong.", c1.getAnswered(), 50.0, 0.0);
    q1.setAnswer(3);
    assertEquals("Progress calculation wrong.", c1.getAnswered(), 0.0, 0.0);
  }
}
