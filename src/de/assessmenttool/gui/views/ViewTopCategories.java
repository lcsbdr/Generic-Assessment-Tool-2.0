package de.assessmenttool.gui.views;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.components.ATButton;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.components.ATScrollPane;
import de.assessmenttool.components.ATTable;

public class ViewTopCategories extends ATPanel {

  /**
	 * 
	 */
  private static final long serialVersionUID = 6295161147342284248L;

  public ATTable projectsTable;

  private ATScrollPane scrollPane;

  private ATPanel actionsPanel;

  private ATButton newTopCat, editTopCat, deleteTopCat;

  private ArrayList<Category> topcats;

  public ViewTopCategories() {
    // Initiate the empty assessments list
    this.topcats = new ArrayList<Category>();

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
      this.actionsPanel.setLayout(new MigLayout("", "0lp![]0lp![]0lp![]0lp!", "0lp![]0lp!"));
      this.actionsPanel.add(getNewTopCat(), "cell 0 0");
      this.actionsPanel.add(getEditTopCat(), "cell 1 0");
      this.actionsPanel.add(getDeleteTopCat(), "cell 2 0");
    }
    return this.actionsPanel;
  }

  private ATButton getNewTopCat() {
    if (this.newTopCat == null) {
      this.newTopCat = new ATButton("New");
    }
    return this.newTopCat;
  }

  private ATButton getEditTopCat() {
    if (this.editTopCat == null) {
      this.editTopCat = new ATButton("Edit");
    }
    return this.editTopCat;
  }

  class AssessmentsTableModel extends DefaultTableModel {

    /**
		 * 
		 */
    private static final long serialVersionUID = 1L;

    @Override
    public boolean isCellEditable(int row, int column) {
      return true;
    }

    @Override
    public Object getValueAt(int row, int column) {
      Category currentTopCat = ViewTopCategories.this.topcats.get(row);
      return currentTopCat.getName();
    }

    @Override
    public String getColumnName(int column) {
      return "Name";
    }

    @Override
    public int getColumnCount() {
      return 1;
    }

    @Override
    public int getRowCount() {
      return ViewTopCategories.this.topcats.size();
    }

    @Override
    public void setValueAt(Object name, int row, int column) {
      ViewTopCategories.this.topcats.get(row).setName(String.valueOf(name));
    }
  }

  public void setTopCategories(ArrayList<Category> topcats) {
    this.topcats = topcats;
  }

  private ATButton getDeleteTopCat() {
    if (this.deleteTopCat == null) {
      this.deleteTopCat = new ATButton("Delete");
    }
    return this.deleteTopCat;
  }
}
