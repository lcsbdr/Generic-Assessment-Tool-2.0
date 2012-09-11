package de.assessmenttool.gui.views;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Criterion.CriterionType;
import de.assessmenttool.components.ATLabel;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.components.ATTable;
import de.assessmenttool.kernel.Log;
import de.assessmenttool.kernel.LogicIF;

public class ViewCriteria extends ATPanel {

  private static final long serialVersionUID = -2946753438047532283L;

  private JTable critTable;

  private JTable critParamsTable;

  private final LogicIF logic;

  private final ViewIF view;

  private ATPanel panel;

  private String possibleCrits = "";

  private ArrayList<Criterion> crits = new ArrayList<Criterion>();

  private CritParamsTableModel critParamsTableModel;

  // generate default table model
  private final CritEditTableModel catTableModel = new CritEditTableModel();

  public ViewCriteria(LogicIF logic, ViewIF view) {
    this.logic = logic;
    this.view = view;

    for (CriterionType tmp : CriterionType.values()) {
      this.possibleCrits += tmp.toString() + " ";
    }
    this.crits = logic.getPossibleCriteria();

    initComponents();
  }

  private void initComponents() {
    this.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));

    this.panel = new ATPanel();
    this.add(this.panel, "grow");
    this.panel.setLayout(new MigLayout("", "[][][][]", "[][][][]"));

    ATLabel label = new ATLabel("Possible criteria: " + this.possibleCrits);
    this.panel.add(label, "cell 0 0");

    JScrollPane catScrollPane = new JScrollPane();
    this.panel.add(new ATLabel("Criteria:"), "cell 0 1");
    this.panel.add(catScrollPane, "cell 0 2");

    JScrollPane cat2ScrollPane = new JScrollPane();
    this.panel.add(new ATLabel("Criterion parameters: (lowest keys should be the best values)"), "cell 1 1");
    this.panel.add(cat2ScrollPane, "cell 1 2");

    // create the table for all categories
    this.critTable = new ATTable();
    catScrollPane.setViewportView(this.critTable);
    this.critTable.setOpaque(true);
    this.critTable.setRowHeight(20);

    this.critTable.setDefaultRenderer(JComboBox.class, new CriterionRenderer());
    this.critTable.setDefaultEditor(JComboBox.class, new CriterionEditor());

    this.critTable.setModel(this.catTableModel);

    this.critTable.addKeyListener(new KeyAdapter(){
      @Override
      public void keyPressed(KeyEvent e) {
        if (ViewCriteria.this.critTable.getSelectedRow() < ViewCriteria.this.crits.size()) {
          ViewCriteria.this.critParamsTableModel.fireTableDataChanged();
        }
      }
    });
    this.critTable.addMouseListener(new MouseAdapter(){
      @Override
      public void mousePressed(MouseEvent e) {
        if (ViewCriteria.this.critTable.getSelectedRow() < ViewCriteria.this.crits.size()) {
          ViewCriteria.this.critParamsTableModel.fireTableDataChanged();
        }
      }
    });

    this.critTable.addKeyListener(new KeyAdapter(){
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_DELETE) {
          if (ViewCriteria.this.critTable.getSelectedRow() >= 0) {
            int sel = ViewCriteria.this.critTable.getSelectedRow();
            ViewCriteria.this.logic.removeCriterion(ViewCriteria.this.crits.get(sel));
            ViewCriteria.this.catTableModel.removeRow(ViewCriteria.this.critTable.getSelectedRow());
            ViewCriteria.this.catTableModel.fireTableDataChanged();
            ViewCriteria.this.critTable.setRowSelectionInterval(0, 0);
            ViewCriteria.this.view.forceAssessmentRefresh();
          }
        }
      }
    });

    // create the table for all categories
    this.critParamsTable = new ATTable();
    cat2ScrollPane.setViewportView(this.critParamsTable);
    this.critParamsTable.setOpaque(true);
    this.critParamsTable.setRowHeight(20);

    // generate default table model
    this.critParamsTableModel = new CritParamsTableModel();
    this.critParamsTable.setModel(this.critParamsTableModel);

    this.repaint();
    this.validate();
  }

  class CritParamsTableModel extends DefaultTableModel {

    @Override
    public String getColumnName(int column) {
      switch (column) {
        case 0:
          return "Key";
        default:
          return "Value";
      }
    }

    @Override
    public void removeRow(int row) {
      if (row != ViewCriteria.this.crits.size()) {
        ViewCriteria.this.crits.get(row).removeParameter(String.valueOf(row));
      }
    }

    @Override
    public int getColumnCount() {
      return 2;
    }

    @Override
    public int getRowCount() {
      if (ViewCriteria.this.crits == null) return 0;
      if (ViewCriteria.this.critTable.getSelectedRow() < 0) return 0;
      if (ViewCriteria.this.critTable.getSelectedRow() >= ViewCriteria.this.crits.size()) return 1;
      return ViewCriteria.this.crits.get(ViewCriteria.this.critTable.getSelectedRow()).getParams().size() + 1;
    }

    @Override
    public Class getColumnClass(int column) {
      return String.class;
    }

    @Override
    public void setValueAt(Object obj, int row, int col) {
      if (ViewCriteria.this.crits.get(ViewCriteria.this.critTable.getSelectedRow()).getType() == CriterionType.NUMERIC) {
        try {
          Integer.valueOf((String)obj);
        } catch (java.lang.NumberFormatException e) {
          Log.out(obj + " is not numeric.", Log.STATUS_BAD, Log.DIRECTION_STATUS);
          return;
        }
      }
      if (ViewCriteria.this.crits.get(ViewCriteria.this.critTable.getSelectedRow()).getType() == CriterionType.YESNO) {
        if (row >= 2) {
          Log.out("CriterionType.YESNO can only have 2 values.", Log.STATUS_BAD, Log.DIRECTION_STATUS);
          return;
        }
      }

      if (row == ViewCriteria.this.crits.get(ViewCriteria.this.critTable.getSelectedRow()).getParams().size()) {
        String key = String.valueOf(row + 1);
        String value = String.valueOf(obj);
        ViewCriteria.this.crits.get(ViewCriteria.this.critTable.getSelectedRow()).addParameter(key, value);
      }

      ViewCriteria.this.crits.get(ViewCriteria.this.critTable.getSelectedRow()).addParameter(String.valueOf(row + 1),
                                                                                             (String)obj);
      ViewCriteria.this.logic.saveCriterion(ViewCriteria.this.crits.get(ViewCriteria.this.critTable.getSelectedRow()));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      if (column == 0) return false;
      return true;
    }

    @Override
    public Object getValueAt(int row, int col) {
      row++;
      if (ViewCriteria.this.critTable.getSelectedRow() >= ViewCriteria.this.crits.size()) {
        return "";
      }

      switch (col) {
        case 0:
          return row;
        default:
          return ViewCriteria.this.crits.get(ViewCriteria.this.critTable.getSelectedRow()).getParams()
            .get(String.valueOf(row));
      }
    }

    public CritParamsTableModel() {
      super();
    }
  }

  class CritEditTableModel extends DefaultTableModel {

    @Override
    public String getColumnName(int column) {
      switch (column) {
        case 0:
          return "Name";
        default:
          return "Type";
      }
    }

    @Override
    public void removeRow(int row) {
      if (row != ViewCriteria.this.crits.size()) {
        ViewCriteria.this.crits.remove(row);
      }
    }

    @Override
    public int getColumnCount() {
      return 2;
    }

    @Override
    public int getRowCount() {
      if (ViewCriteria.this.crits == null) return 0;
      return ViewCriteria.this.crits.size() + 1;
    }

    @Override
    public Class getColumnClass(int column) {
      if (column == 1) return JComboBox.class;
      return String.class;
    }

    @Override
    public void setValueAt(Object obj, int row, int col) {
      if (row == ViewCriteria.this.crits.size()) {
        String name = "";
        if (col == 0) {
          name = (String)obj;
        }
        Criterion temp = new Criterion(-1, name, CriterionType.YESNO);
        ViewCriteria.this.crits.add(temp);
        ViewCriteria.this.logic.saveCriterion(temp);
      }

      switch (col) {
        case 0:
          ViewCriteria.this.crits.get(row).setName((String)obj);
          break;
        default:
          ViewCriteria.this.crits.get(row).setCriterionType((Criterion.CriterionType)obj);
      }
      ViewCriteria.this.logic.saveCriterion(ViewCriteria.this.crits.get(row));
    }

    @Override
    public Object getValueAt(int row, int col) {
      if (row == ViewCriteria.this.crits.size()) {
        return "";
      }

      switch (col) {
        case 0:
          return ViewCriteria.this.crits.get(row).getName();
        default:
          return ViewCriteria.this.crits.get(row).getType();
      }
    }

    public CritEditTableModel() {
      super();
    }
  }

  class CriterionRenderer extends JComboBox implements TableCellRenderer {

    private final ATLabel label = new ATLabel("");

    public CriterionRenderer() {
      super();

      for (CriterionType tmp : CriterionType.values()) {
        addItem(tmp);
      }
    }

    @Override
    public Component getTableCellRendererComponent(JTable arg0, Object val, boolean arg2, boolean arg3, int row, int col) {
      if (row < ViewCriteria.this.crits.size()) {
        setSelectedItem(ViewCriteria.this.crits.get(row).getType());
        return this;
      }
      return this.label;
    }
  }

  class CriterionEditor extends AbstractCellEditor implements TableCellEditor {

    private final JComboBox crittypess = new JComboBox();

    private final ATLabel label = new ATLabel("");

    public CriterionEditor() {
      super();

      for (CriterionType tmp : CriterionType.values()) {
        this.crittypess.addItem(tmp);
      }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object val, boolean isSelected, int row, int column) {
      if (row >= ViewCriteria.this.crits.size()) return this.label;

      for (int i = 0; i < ViewCriteria.this.logic.getPossibleCriteria().size(); i++) {
        if (ViewCriteria.this.crits.get(row).getName().equals(val)) {
          this.crittypess.setSelectedIndex(i);
        }
      }
      return this.crittypess;
    }

    @Override
    public Object getCellEditorValue() {
      return this.crittypess.getSelectedItem();
    }
  }
}
