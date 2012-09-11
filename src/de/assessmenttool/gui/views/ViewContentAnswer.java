package de.assessmenttool.gui.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Criterion.CriterionType;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.components.ATButtonGroup;
import de.assessmenttool.components.ATLabelCategory;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.components.ATProgressBar;
import de.assessmenttool.components.ATRadioButton;
import de.assessmenttool.components.ATTextField;
import de.assessmenttool.kernel.Log;
import de.assessmenttool.kernel.LogicIF;

public class ViewContentAnswer extends ATPanel {

  private final LogicIF logic;

  private final ViewIF view;

  private Assessment activeAssessment;

  private ArrayList<Assessment> assessments;

  private ArrayList<Question> questions;

  private final ATPanel mainPanel = new ATPanel();

  private Category activeCategory;

  private final ATProgressBar progBar = new ATProgressBar();

  private ATLabelCategory heading;

  private int i;

  private Question activeQuestion;

  private String questionText;

  private boolean assessmentActive = false;

  boolean repaintAnswer = false;

  public ViewContentAnswer(LogicIF logic, ViewIF view) {
    this.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
    this.logic = logic;
    this.view = view;
    this.assessments = this.logic.getAssessments();

    if (this.assessments.size() > 0) {
      actualize(this.assessments.get(0));
    } else {
      Log.out("No Assessments to load for ViewContentAnswer", Log.STATUS_NORMAL, Log.DIRECTION_LOG);
      return;
    }
  }

  private void refreshProgressBar() {
    if (this.assessmentActive && this.activeAssessment != null) {
      this.progBar.setValue((int)this.activeAssessment.getAnswered());
    } else if (!this.assessmentActive && this.activeCategory != null) {
      this.progBar.setValue((int)this.activeCategory.getAnswered());
    }
  }

  private void actualize(Object o) {
    this.i = 0;
    int maxColums = 75; // Anzahl der Buchstaben in textFieldQuestion

    this.removeAll();
    repaint();
    this.mainPanel.removeAll();
    this.setLayout(new MigLayout("", "[grow,fill]", "[50lp!,fill][grow,fill]"));
    this.mainPanel.setLayout(new MigLayout("", "[grow][200lp!]", "[][]"));

    if (o instanceof Assessment) {
      this.activeAssessment = (Assessment)o;
      this.questions = this.activeAssessment.getQuestions();
      this.heading = new ATLabelCategory(this.activeAssessment.getName());
      this.progBar.setValue((int)this.activeAssessment.getAnswered());
      this.assessmentActive = true;
    } else if (o instanceof Category) {
      this.activeCategory = (Category)o;
      this.questions = this.activeCategory.getQuestions();
      this.heading = new ATLabelCategory(this.activeCategory.getName());
      this.progBar.setValue((int)this.activeCategory.getAnswered());
      this.assessmentActive = false;
    }

    this.progBar.setStringPainted(true);

    // this.mainPanel.add(heading, "growx, wrap");
    this.add(this.progBar, "growx, wrap");

    for (this.i = 0; this.i < this.questions.size(); this.i++) {
      final Question tmpQuest = this.questions.get(this.i);
      ATTextField textFieldQuestion = new ATTextField();

      this.questionText = this.questions.get(this.i).getName();
      if (this.questionText.length() >= maxColums * 2) {
        this.questionText = this.questionText.substring(0, maxColums * 2 - 1) + " ...";
      }
      textFieldQuestion.setText(this.questionText);
      textFieldQuestion.setEditable(false);

      if (tmpQuest.isLocked()) {
        textFieldQuestion.setEnabled(false);
        this.repaintAnswer = true;
      } else {
        textFieldQuestion.setEnabled(true);
      }

      textFieldQuestion.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent arg0) {
          if (ViewContentAnswer.this.view != null)
            ViewContentAnswer.this.view.setActiveElement(tmpQuest);
          else
            Log.out("Error ViewContentAnswer view == null", Log.STATUS_BAD, Log.DIRECTION_LOG);
        }
      });
      this.mainPanel.add(textFieldQuestion, "cell 0 " + this.i + ",growx");

      Criterion crit = this.questions.get(this.i).getCriterion();
      CriterionType critType = this.questions.get(this.i).getCriterion().getType();

      // TODO: set Question.answer
      // NUMERISCH or ALPHANUMERISCH
      if (critType == CriterionType.NUMERIC || critType == CriterionType.ALPHANUMERIC) {
        HashMap<String, String> params = crit.getParams();

        String[] str = new String[params.size()];
        String selItem = null;
        for (int k = 1; k <= params.size(); k++) {
          str[k - 1] = params.get(String.valueOf(k));
          if (tmpQuest.isAnswered()) {
            selItem = tmpQuest.getAnswer();
          } else {
            if (k == params.size()) selItem = params.get(String.valueOf(k));
          }
        }

        final JComboBox comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(str));
        comboBox.setSelectedItem(selItem);
        comboBox.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent e) {
            ViewContentAnswer.this.logic.answerQuestion(tmpQuest, Integer.valueOf(tmpQuest.getCriterion()
              .getKey((String)comboBox.getSelectedItem())));
            view.setActiveElement(logic.getRelatedCategory(tmpQuest));
            view.setActiveElement(tmpQuest);
            refreshProgressBar();
            ViewContentAnswer.this.view.forceAssessmentRefresh();
          }
        });
        // comboBox.addMouseListener();

        if (tmpQuest.isLocked()) {
          comboBox.setEnabled(false);
          this.repaintAnswer = true;
        } else {
          comboBox.setEnabled(true);
        }

        this.mainPanel.add(comboBox, "cell 1 " + this.i + ",growx");
      }
      // YES / NO
      else if (critType == CriterionType.YESNO) {
        ATButtonGroup groupYesNo = new ATButtonGroup();
        ATRadioButton btnYes = new ATRadioButton(crit.getParams().get("1"));
        ATRadioButton btnNo = new ATRadioButton(crit.getParams().get("2"));

        ATPanel pnlGroup = new ATPanel();
        pnlGroup.setLayout(new MigLayout("", "[][]", "[]"));

        // check if the question was already answered
        if (tmpQuest.isAnswered()) {
          if (tmpQuest.getAnswerInt() == 1) btnYes.setSelected(true);
          if (tmpQuest.getAnswerInt() == 2) btnNo.setSelected(true);
        }

        groupYesNo.add(btnYes);
        groupYesNo.add(btnNo);
        btnYes.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent e) {
            ViewContentAnswer.this.logic.answerQuestion(tmpQuest, 1);
            view.setActiveElement(logic.getRelatedCategory(tmpQuest));
            view.setActiveElement(tmpQuest);
            refreshProgressBar();
          }
        });
        btnNo.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent e) {
            ViewContentAnswer.this.logic.answerQuestion(tmpQuest, 2);
            view.setActiveElement(logic.getRelatedCategory(tmpQuest));
            view.setActiveElement(tmpQuest);
            refreshProgressBar();
          }
        });

        if (tmpQuest.isLocked()) {
          btnYes.setEnabled(false);
          btnNo.setEnabled(false);
          this.repaintAnswer = true;
        } else {
          btnYes.setEnabled(true);
          btnNo.setEnabled(true);
        }

        // pnlGroup.addMouseListener(new myActionListener(tmpQuest, this.view));

        pnlGroup.add(btnYes);
        pnlGroup.add(btnNo);

        this.mainPanel.add(pnlGroup, "cell 1 " + this.i + ",growx");
      } else {
        Log.out("Criterion-Type not expected (ViewContentAnswer)", Log.STATUS_BAD, Log.DIRECTION_BOTH);
      }
    }

    JScrollPane scrollPane = new JScrollPane();
    this.add(scrollPane, "cell 0 1,grow");
    scrollPane.setViewportView(this.mainPanel);

    validate();
  }

  public void elementChanged(Object o) {
    if (o instanceof Question) {
      if (this.logic.getRelatedCategory((Question)o) != null) {
        actualize(this.logic.getRelatedCategory((Question)o));
      }
    } else {
      actualize(o);
    }
  }
}