package de.assessmenttool.gui.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.kernel.LogicIF;

public class ViewDependency extends ATPanel {

  private final LogicIF logic;

  private final ViewIF view;

  private Question activeQuestion;

  private Assessment activeAssessment;

  private final HashMap<Question, Integer> activeDependencyQuestions = new HashMap<Question, Integer>();

  // private HashMap<Question, Integer> dependency = new HashMap<Question, Integer>();
  ATPanel panel = new ATPanel();

  JScrollPane scroll;

  private Integer needtmp;

  public ViewDependency(LogicIF logic, ViewIF view) {
    this.logic = logic;
    this.view = view;
  }

  public void elementChanged(Question question) {
    System.out.println(question.getId() + question.toString());
    this.activeQuestion = question;
    this.activeAssessment = this.logic.getRelatedAssessment(question);
    for (int i = 0; i < question.getDependencies().size(); i++) {
      this.activeDependencyQuestions.put(this.activeQuestion.getDependencies().get(i), this.activeQuestion
        .getAnswersNeeded().get(i));
    }
    System.out.println(this.activeQuestion.getDependencies());

    this.removeAll();
    this.panel.removeAll();
    this.setLayout(new MigLayout("", "[grow,fill]", "[]"));
    this.panel.setLayout(new MigLayout("", "", ""));

    paintElements(this.activeAssessment);
    this.scroll = new JScrollPane(this.panel);
    this.add(this.scroll);

    this.repaint();
  }

  private void paintElements(Object o) {
    ArrayList<Question> questions = null;
    ArrayList<Category> categories = null;

    if (o instanceof Assessment) {
      questions = ((Assessment)o).getQuestions();
      categories = ((Assessment)o).getCategories();
    } else if (o instanceof Category) {
      questions = ((Category)o).getQuestions();
      categories = ((Category)o).getCategories();
    }
    for (int i = 0; i < questions.size(); i++) {
      final Question tmpQuest = questions.get(i);
      if (tmpQuest.getId() != this.activeQuestion.getId() && !contains(tmpQuest.getDependencies(), activeQuestion)) {
        JCheckBox check = null;
        if (contains(this.activeQuestion.getDependencies(), (tmpQuest))) {
          check = new JCheckBox(tmpQuest.getName(), true);
        } else {
          check = new JCheckBox(tmpQuest.getName(), false);
        }
        if (check != null) {
          check.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
              AbstractButton abstractButton = (AbstractButton)e.getSource();
              boolean selected = abstractButton.getModel().isSelected();
              if (selected) {
                Criterion crit = tmpQuest.getCriterion();
                HashMap<String, String> params = crit.getParams();

                JPopupMenu pop = new JPopupMenu();

                for (int i = 1; i <= params.size(); i++) {
                  Integer z = i;
                  JMenuItem item = new JMenuItem(params.get(String.valueOf(i)));
                  item.setName(z.toString());
                  pop.add(item);
                  item.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent arg) {
                      ViewDependency.this.needtmp = Integer.parseInt(((JMenuItem)arg.getSource()).getName());
                      ViewDependency.this.logic.setDependency(ViewDependency.this.activeQuestion,
                                                              tmpQuest,
                                                              ViewDependency.this.needtmp);
//                      view.forceAssessmentRefresh();
                      view.setActiveElement(activeQuestion);
                    }
                  });
                }
                pop.setInvoker(pop);
                pop.setVisible(true);

                if (ViewDependency.this.needtmp == null) abstractButton.setSelected(false);

                ViewDependency.this.needtmp = null;
              } else {
                ViewDependency.this.logic.removeDependency(ViewDependency.this.activeQuestion, tmpQuest);
                view.setActiveElement(activeQuestion);
              }
            }
          });
          this.panel.add(check, "growx,wrap");
        }
      }
    }
    for (int i = 0; i < categories.size(); i++) {
      paintElements(categories.get(i));
    }
  }

  private boolean contains(ArrayList<Question> dependencies, Question question) {
	boolean bu = false;
	  for (int i = 0 ; i < dependencies.size(); i++) {
		if(dependencies.get(i).getId() == question.getId()) {
			bu = true;
		}
	}
	return bu;
}

public void elementChanged(Object o) {
    // this.removeAll();
    // this.repaint();
  }
}