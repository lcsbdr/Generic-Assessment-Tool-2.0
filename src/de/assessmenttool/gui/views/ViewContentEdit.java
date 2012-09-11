package de.assessmenttool.gui.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Criterion.CriterionType;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.components.ATLabel;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.components.ATTable;
import de.assessmenttool.components.ATTextPane;
import de.assessmenttool.kernel.Log;
import de.assessmenttool.kernel.LogicIF;

public class ViewContentEdit extends ATPanel {

  private static final long serialVersionUID = -2946753438047532283L;

  private JTable questTable;

  private JTable catTable;

  private final LogicIF logic;

  private final ViewIF view;

  private ATTextPane textField;

  private Category usedCategory = null;

  private Assessment usedAssessment = null;

  private ATPanel panel_1;

  private ATPanel panel;

  private Question selectedQuestion;

  public ViewContentEdit(LogicIF logic, ViewIF view) {
    this.logic = logic;
    this.view = view;

    if (logic.getAssessments().size() > 0 && logic.getAssessments().get(0).getCategories().size() > 0) {
      elementChanged(logic.getAssessments().get(0).getCategories().get(0));
    }
  }

  public void elementChanged(Assessment assessment) {
    this.usedAssessment = assessment;
    this.usedCategory = null;
    initComponents();
  }

  public void elementChanged(Category category) {
    this.usedAssessment = null;
    this.usedCategory = category;
    initComponents();
  }

  public void addCategoryLinks() {
    if (this.panel_1 == null) {
      this.panel_1 = new ATPanel();
    } else {
      this.panel.remove(this.panel_1);
      this.panel_1.removeAll();
    }
    this.panel_1.setLayout(new MigLayout("", "0lp![grow]0lp!", "0lp![][][][]10lp!"));

    ArrayList<Category> categories;
    if (this.usedCategory != null) {
      categories = this.usedCategory.getCategories();
    } else {
      categories = this.usedAssessment.getCategories();
    }
    for (int i = 0; i < categories.size(); i++) {
      this.textField = new ATTextPane();
      this.textField.setText(categories.get(i).getName());
      this.textField.setForeground(Color.BLUE);
      this.textField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLUE));
      this.textField.setOpaque(false);
      this.textField.setBackground(new Color(0, 0, 0, 0));
      this.panel_1.add(this.textField);
      final Category category_1 = categories.get(i);
      this.textField.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e) {
          ViewContentEdit.this.view.setActiveElement(category_1);
        }
      });
    }

    this.panel.add(this.panel_1, "growx, wrap, cell 0 0");
    this.panel.repaint();
    this.panel.validate();
  }

  private void initComponents() {
    this.removeAll();
    this.setLayout(new MigLayout("", "[grow,fill]"));

    this.panel = new ATPanel();
    this.add(this.panel, "grow");
    this.panel.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));

    addCategoryLinks();

    JScrollPane questScrollPane = new JScrollPane();
    this.panel.add(new ATLabel("Questions:"), "cell 0 3");
    this.panel.add(questScrollPane, "cell 0 4");

    // create the table for all questions
    this.questTable = new ATTable();
    questScrollPane.setViewportView(this.questTable);
    this.questTable.setOpaque(true);
    this.questTable.setRowHeight(20);
    this.questTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.questTable.setRowSelectionAllowed(true);
    this.questTable.setColumnSelectionAllowed(false);

    // add the default renderer and editor for criterion column
    final ArrayList<Question> quests;
    if (this.usedCategory != null) {
      quests = this.usedCategory.getQuestions();
    } else {
      quests = this.usedAssessment.getQuestions();
    }
    this.questTable.setDefaultRenderer(JComboBox.class, new CriterionRenderer(quests));
    this.questTable.setDefaultEditor(JComboBox.class, new CriterionEditor());
    // generate default table model
    final QuestEditTableModel questTableModel = new QuestEditTableModel(quests);
    this.questTable.setModel(questTableModel);

    this.questTable.addKeyListener(new KeyAdapter(){
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_DELETE) {
          if (ViewContentEdit.this.questTable.getSelectedRow() >= 0) {
            int sel = ViewContentEdit.this.questTable.getSelectedRow();
            ViewContentEdit.this.logic.removeQuestion(quests.get(sel));
            questTableModel.removeRow(ViewContentEdit.this.questTable.getSelectedRow());
            questTableModel.fireTableDataChanged();
            ViewContentEdit.this.questTable.setRowSelectionInterval(0, 0);
            ViewContentEdit.this.view.forceAssessmentRefresh();
          }
        }
      }
    });

    SelectionListener listener = new SelectionListener(this.questTable);
    this.questTable.getSelectionModel().addListSelectionListener(listener);
    this.questTable.getColumnModel().getSelectionModel().addListSelectionListener(listener);

    JScrollPane catScrollPane = new JScrollPane();
    this.panel.add(new ATLabel("Categories:"), "cell 0 1");
    this.panel.add(catScrollPane, "cell 0 2");

    // create the table for all categories
    this.catTable = new ATTable();
    catScrollPane.setViewportView(this.catTable);
    this.catTable.setOpaque(true);
    this.catTable.setRowHeight(20);
    this.catTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // add the default renderer and editor for criterion column
    final ArrayList<Category> categories;
    if (this.usedCategory != null) {
      categories = this.usedCategory.getCategories();
    } else {
      categories = this.usedAssessment.getCategories();
    }
    this.catTable.setDefaultRenderer(JComboBox.class, new CriterionRenderer(quests));
    this.catTable.setDefaultEditor(JComboBox.class, new CriterionEditor());
    // generate default table model
    final CatEditTableModel catTableModel = new CatEditTableModel(categories);
    this.catTable.setModel(catTableModel);

    this.catTable.addKeyListener(new KeyAdapter(){
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_DELETE) {
          if (ViewContentEdit.this.catTable.getSelectedRow() >= 0 && ViewContentEdit.this.catTable.getRowCount() > 1) {
            int sel = ViewContentEdit.this.catTable.getSelectedRow();
            ViewContentEdit.this.logic.removeCategory(categories.get(sel));
            catTableModel.removeRow(sel);
            catTableModel.fireTableDataChanged();
            ViewContentEdit.this.catTable.setRowSelectionInterval(0, 0);
            ViewContentEdit.this.view.forceAssessmentRefresh();
          }
        }
      }
    });

    this.repaint();
    this.validate();
  }

  public void elementChanged(Question question) {
    // elementChanged(logic.getRelatedCategory(question));
  }

  public class SelectionListener implements ListSelectionListener {
    private final JTable table;

    private int selIndex = 0;

    // It is necessary to keep the table since it is not possible
    // to determine the table from the event's source
    SelectionListener(JTable table) {
      this.table = table;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
      // If cell selection is enabled, both row and column change events are fired
      this.selIndex = this.table.getSelectedRow();
      if (this.selIndex < 0) return;

      ViewContentEdit.this.selectedQuestion = ((QuestEditTableModel)ViewContentEdit.this.questTable.getModel())
        .getSelectedQuestion(this.selIndex);
      ViewContentEdit.this.view.setActiveElement(ViewContentEdit.this.selectedQuestion);
    }
  }

  class QuestEditTableModel extends DefaultTableModel {

    private ArrayList<Question> quests = new ArrayList<Question>();

    @Override
    public String getColumnName(int column) {
      switch (column) {
        case 0:
          return "Text";
        case 1:
          return "Note";
        case 2:
          return "Criterion";
        default:
          return "Weight";
      }
    }

    @Override
    public void removeRow(int row) {
      if (row != this.quests.size()) {
        this.quests.remove(row);
      }
    }

    @Override
    public int getColumnCount() {
      return 4;
    }

    @Override
    public int getRowCount() {
      if (this.quests == null) return 0;
      return this.quests.size() + 1;
    }

    @Override
    public Class getColumnClass(int column) {
      if (column == 2) {
        return JComboBox.class;
      }
      return String.class;
    }

    @Override
    public void setValueAt(Object obj, int row, int col) {
      if (row == this.quests.size()) {
        if (col == 2) {
          return;
        }

        Criterion crit = null;
        try {
          crit = this.quests.get(this.quests.size() - 1).getCriterion();
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
          try {
            crit = ViewContentEdit.this.logic.getPossibleCriteria().get(0);
          } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            crit = new Criterion(-1, "Default", CriterionType.YESNO);
          }
        }

        Question temp = new Question((String)obj, crit);
        this.quests.add(temp);
      }

      switch (col) {
        case 0:
          this.quests.get(row).setName((String)obj);
          break;
        case 1:
          this.quests.get(row).setNote((String)obj);
          break;
        case 2:
          this.quests.get(row).setCriterion((Criterion)obj);
          break;
        default:
          try {
            this.quests.get(row).setWeight(Float.valueOf((String)obj));
          } catch (java.lang.NumberFormatException e) {
            // set status
            Log.out(obj.toString() + " is not a float value. ", Log.STATUS_BAD, Log.DIRECTION_STATUS);
            Log.out(obj.toString() + " is not a float value. <br/>" + e.toString(), Log.STATUS_BAD, Log.DIRECTION_LOG);
          }
      }
      if (ViewContentEdit.this.usedCategory != null) {
        ViewContentEdit.this.logic.saveQuestion(this.quests.get(row), ViewContentEdit.this.usedCategory);
      } else {
        ViewContentEdit.this.logic.saveQuestion(this.quests.get(row), ViewContentEdit.this.usedAssessment);
      }
      ViewContentEdit.this.view.forceAssessmentRefresh();
    }

    @Override
    public Object getValueAt(int row, int col) {
      if (row == this.quests.size()) {
        return "";
      }

      switch (col) {
        case 0:
          return this.quests.get(row).getName();
        case 1:
          return this.quests.get(row).getNote();
        case 2:
          return this.quests.get(row).getCriterion();
        default:
          return this.quests.get(row).getWeight();
      }
    }

    public Question getSelectedQuestion(int index) {
      if (index >= 0 && index < this.quests.size()) {
        return this.quests.get(index);
      }
      return this.quests.get(0);
    }

    public QuestEditTableModel(ArrayList<Question> quests) {
      super();
      this.quests = quests;
    }
  }

  class CatEditTableModel extends DefaultTableModel {

    private ArrayList<Category> cats = new ArrayList<Category>();

    @Override
    public String getColumnName(int column) {
      switch (column) {
        case 0:
          return "Text";
        case 1:
          return "Note";
        default:
          return "Weight";
      }
    }

    @Override
    public void removeRow(int row) {
      if (row != this.cats.size()) {
        this.cats.remove(row);
      }
    }

    @Override
    public int getColumnCount() {
      return 3;
    }

    @Override
    public int getRowCount() {
      if (this.cats == null) return 0;
      return this.cats.size() + 1;
    }

    @Override
    public Class getColumnClass(int column) {
      return String.class;
    }

    @Override
    public void setValueAt(Object obj, int row, int col) {
      if (row == this.cats.size()) {
        Category temp = new Category((String)obj);
        this.cats.add(temp);
      }

      switch (col) {
        case 0:
          this.cats.get(row).setName((String)obj);
          break;
        case 1:
          this.cats.get(row).setNote((String)obj);
          break;
        default:
          try {
            this.cats.get(row).setWeight(Float.valueOf((String)obj));
          } catch (java.lang.NumberFormatException e) {
            // set status
            Log.out(obj.toString() + " is not a float value. ", Log.STATUS_BAD, Log.DIRECTION_STATUS);
            Log.out(obj.toString() + " is not a float value. <br/>" + e.toString(), Log.STATUS_BAD, Log.DIRECTION_LOG);
          }
      }
      if (ViewContentEdit.this.usedCategory != null) {
        ViewContentEdit.this.logic.saveCategory(this.cats.get(row), ViewContentEdit.this.usedCategory);
      } else {
        ViewContentEdit.this.logic.saveCategory(this.cats.get(row), ViewContentEdit.this.usedAssessment);
      }
      ViewContentEdit.this.view.forceAssessmentRefresh();
    }

    @Override
    public Object getValueAt(int row, int col) {
      if (row == this.cats.size()) {
        return "";
      }

      switch (col) {
        case 0:
          return this.cats.get(row).getName();
        case 1:
          return this.cats.get(row).getNote();
        default:
          return this.cats.get(row).getWeight();
      }
    }

    public CatEditTableModel(ArrayList<Category> cats) {
      super();
      this.cats = cats;
    }
  }

  class CriterionRenderer extends JComboBox implements TableCellRenderer {

    private ArrayList<Question> quests = new ArrayList<Question>();

    private final ATLabel label = new ATLabel();

    public CriterionRenderer(ArrayList<Question> quests) {
      super();

      this.quests = quests;
      for (Criterion tmp : ViewContentEdit.this.logic.getPossibleCriteria()) {
        addItem(tmp);
      }
    }

    @Override
    public Component getTableCellRendererComponent(JTable arg0, Object val, boolean arg2, boolean arg3, int row, int col) {
      if (val instanceof String) {
        return this.label;
      }

      for (int i = 0; i < ViewContentEdit.this.logic.getPossibleCriteria().size(); i++) {
        if (((Criterion)val).getName().equals(ViewContentEdit.this.logic.getPossibleCriteria().get(i).getName())) {
          setSelectedIndex(i);
        }
      }
      return this;
    }
  }

  class CriterionEditor extends AbstractCellEditor implements TableCellEditor {

    private final JComboBox crits = new JComboBox();

    private final ATLabel label = new ATLabel();

    public CriterionEditor() {
      super();

      for (Criterion tmp : ViewContentEdit.this.logic.getPossibleCriteria()) {
        this.crits.addItem(tmp);
      }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object val, boolean isSelected, int row, int column) {
      if (val instanceof String) {
        return this.label;
      }

      for (int i = 0; i < ViewContentEdit.this.logic.getPossibleCriteria().size(); i++) {
        if (((Criterion)val).getName().equals(ViewContentEdit.this.logic.getPossibleCriteria().get(i).getName())) {
          this.crits.setSelectedIndex(i);
        }
      }
      return this.crits;
    }

    @Override
    public Object getCellEditorValue() {
      return this.crits.getSelectedItem();
    }
  }
}
