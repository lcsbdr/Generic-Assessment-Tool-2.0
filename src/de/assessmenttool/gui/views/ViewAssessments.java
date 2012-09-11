package de.assessmenttool.gui.views;

import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.components.ATButton;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.components.ATScrollPane;
import de.assessmenttool.components.ATTable;

public class ViewAssessments extends ATPanel {

  /**
	 * 
	 */
  private static final long serialVersionUID = 6295161147342284248L;

  public ATTable projectsTable;

  private ATScrollPane scrollPane;

  private ATPanel actionsPanel;

  private ATButton newAssessment, editAssessment;

  public int selectedAssessment = -1;

  private ArrayList<Assessment> assessments;

  public ViewAssessments() {
    // Initiate the empty assessments list
    this.assessments = new ArrayList<Assessment>();

    setLayout(new MigLayout("", "0lp![grow,fill]0lp!", "0lp![]0lp![grow,fill]0lp!"));
    add(getActionsPanel(), "cell 0 0");
    add(getProjectsScrollPane(), "cell 0 1");

    setSize(400, 250);
    setVisible(true);
  }

  private ATScrollPane getProjectsScrollPane() {
    if (this.projectsTable == null) {
      AssessmentsTableModel tableModel = new AssessmentsTableModel();
      this.projectsTable = new ATTable(tableModel);
      this.projectsTable.setRowSelectionAllowed(true);
      this.scrollPane = new ATScrollPane(this.projectsTable);
      this.projectsTable.setFillsViewportHeight(true);
      this.projectsTable.setRowHeight(25);
    }
    return this.scrollPane;
  }

  private ATPanel getActionsPanel() {
    if (this.actionsPanel == null) {
      this.actionsPanel = new ATPanel();
      this.actionsPanel.setLayout(new MigLayout("", "0lp![]0lp![]0lp!", "0lp![]0lp!"));
      this.actionsPanel.add(getNewAssessment(), "cell 0 0");
      this.actionsPanel.add(getEditAssessment(), "cell 1 0");
    }
    return this.actionsPanel;
  }

  private ATButton getNewAssessment() {
    if (this.newAssessment == null) {
      this.newAssessment = new ATButton("New");
    }
    return this.newAssessment;
  }

  private ATButton getEditAssessment() {
    if (this.editAssessment == null) {
      this.editAssessment = new ATButton("Edit");
    }
    return this.editAssessment;
  }

  class AssessmentsTableModel extends DefaultTableModel {

    /**
		 * 
		 */
    private static final long serialVersionUID = 1L;

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
      Assessment currentAss = ViewAssessments.this.assessments.get(row);
      switch (column) {
        case 0:
          return currentAss.getName();
        case 1:
          Calendar cal = Calendar.getInstance();
          cal.setTime(currentAss.getLastModificationDate());
          return ((cal.get(Calendar.DAY_OF_MONTH) < 10) ? "0" + cal.get(Calendar.DAY_OF_MONTH) : cal
            .get(Calendar.DAY_OF_MONTH))
                 + "."
                 + (cal.get(Calendar.MONTH) + 1)
                 + "."
                 + (cal.get(Calendar.YEAR))
                 + " "
                 + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
        default:
          return currentAss.getAnswered() + " %";
      }
    }

    @Override
    public String getColumnName(int column) {
      switch (column) {
        case 0:
          return "Name";
        case 1:
          return "Last modification";
        default:
          return "Completion";
      }
    }

    @Override
    public int getColumnCount() {
      return 3;
    }

    @Override
    public int getRowCount() {
      return ViewAssessments.this.assessments.size();
    }

  }

  public void setAssessments(ArrayList<Assessment> assessments) {
    this.assessments = assessments;
  }

}
