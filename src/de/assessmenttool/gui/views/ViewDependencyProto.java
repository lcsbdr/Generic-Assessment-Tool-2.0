package de.assessmenttool.gui.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.kernel.Log;
import de.assessmenttool.kernel.LogicIF;

public class ViewDependencyProto extends ATPanel {
	private Question activeQuestion;

	private final LogicIF logic;

	private final ViewIF view;

	private HashMap<Question, Integer> dependency = new HashMap<Question, Integer>();

	private Criterion crit;

	private Question selectedQuestion;

	private JComboBox comboBox = new JComboBox();

	private JPanel panel_1;

	public ViewDependencyProto(LogicIF logic, ViewIF view) {
		this.logic = logic;
		this.view = view;
		// actualize();
	}

	private void actualize() {
		this.removeAll();

		ArrayList<Assessment> assessments = this.logic.getAssessments();

		Object rootNodes[] = getCat(this.logic
				.getRelatedAssessment(this.activeQuestion));

		Vector rootVector = new NamedVector("Root", rootNodes);
		JTree tree = new JTree(rootVector);
		tree.setOpaque(false);

		tree.setCellRenderer(new CheckBoxNodeRenderer());
		setLayout(new MigLayout("", "[grow,fill][fill,32%!]", "[grow,fill]"));

		tree.setCellEditor(new CheckBoxNodeEditor(tree));
		tree.setEditable(true);

		// Tree Listener

		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				DefaultMutableTreeNode tempTreeNode = (DefaultMutableTreeNode) arg0
						.getPath().getLastPathComponent();
				NamedVector namedVector = null; // Category
				CheckBoxNode checkBoxNode = null; // Question
				try {
					namedVector = (NamedVector) tempTreeNode.getUserObject();
				} catch (Exception e) {
				}
				try {
					checkBoxNode = (CheckBoxNode) tempTreeNode.getUserObject();
				} catch (Exception e) {
				}

				// error
				if ((namedVector != null && checkBoxNode != null)
						|| (namedVector == null && checkBoxNode == null)) {
					Log.out("Selected icon is not a category nor a question",
							Log.STATUS_BAD, Log.DIRECTION_BOTH);
				}
				// clicked on a Category (no checkbox)
				else if (namedVector != null) {
					ViewDependency.this.selectedQuestion = null;
					Object ob = namedVector.getUserObj();
					System.out.println(ob.getClass());
				}
				// clicked on a Question (checkbox)
				else if (checkBoxNode != null) {
					Object ob = checkBoxNode.getUserObj();
					if (ob instanceof Question) {
						ViewDependency.this.selectedQuestion = (Question) ob;
						ViewDependency.this.comboBox
								.setModel(getComboBoxModel()); // TODO highlight
																// preselected
						ViewDependency.this.comboBox.setVisible(true);
						// TODO nächste Zeile funktioniert glaube ich noch nicht
						if(!(activeQuestion == selectedQuestion)) {
							if (!ViewDependency.this.dependency.containsKey(ViewDependency.this.selectedQuestion)) {
								ViewDependency.this.dependency.put((Question) ob, ViewDependency.this.comboBox .getSelectedIndex());
								System.out.println("added " + ob.getClass());
							}
						} else {
							Log.out("ERROR Not saved: " + selectedQuestion.getName() + " Self-Reference", Log.STATUS_BAD, Log.DIRECTION_BOTH);
						}
					} else {
						actualize();
						// TODO remove following line
						ViewDependency.this.dependency = null;
						ViewDependency.this.dependency = new HashMap<Question, Integer>();
						Log.out("ERROR", Log.STATUS_BAD, Log.DIRECTION_BOTH);
					}
				}
			}
		});

		JScrollPane scrollPaneRight = new JScrollPane(tree);
		add(scrollPaneRight, "cell 0 0,alignx left,aligny top");

		this.panel_1 = new JPanel();
		this.panel_1.setLayout(new MigLayout("", "[grow,fill]", "[][][][][]"));
		JScrollPane scrollPaneLeft = new JScrollPane(this.panel_1);
		add(scrollPaneLeft, "cell 1 0,alignx left,aligny top");

		// ComboBox
		this.comboBox = new JComboBox();
		this.panel_1.add(this.comboBox, "cell 0 4, alignx left, aligny top");
		this.comboBox.setVisible(false);
		this.comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ViewDependency.this.selectedQuestion != null) {
					changeDep(ViewDependency.this.selectedQuestion,
							ViewDependency.this.comboBox.getSelectedIndex());
				}
			}
		});

		// Button Save
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ViewDependency.this.activeQuestion
						.setDependencies(ViewDependency.this.dependency);
				System.out.println(ViewDependency.this.activeQuestion);
				Log.out("Save Dependency "
						+ ViewDependency.this.dependency.toString()
						+ " to Question \""
						+ ViewDependency.this.activeQuestion.getName()
						+ "\" ...", Log.STATUS_NORMAL, Log.DIRECTION_LOG);
				Log.out("Saved successfully", Log.STATUS_GOOD,
						Log.DIRECTION_STATUS);
				ViewDependency.this.view.hideViewDependency();
			}
		});
		this.panel_1.add(btnSave, "cell 0 0");

		// Button Remove
		JButton btnRemove = new JButton("Remove All");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// activeQuestion.removeDependencies();
				Log.out("Delete Dependency from Question \""
						+ ViewDependency.this.activeQuestion.getId() + "\" ...",
						Log.STATUS_NORMAL, Log.DIRECTION_BOTH);
				ViewDependency.this.view.hideViewDependency();
			}
		});
		this.panel_1.add(btnRemove, "cell 0 0");

		// Button Cancel
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ViewDependency.this.view.hideViewDependency();
			}
		});
		this.panel_1.add(btnCancel, "cell 0 0, wrap");

		// CheckBox
		// JCheckBox check1 = new JCheckBox("Lock Question", true);
		// panel_1.add(check1, "gaptop 10,cell 0 1,aligny top,wrap");
		// JCheckBox check2 = new JCheckBox("Disable in evaluation", true);
		// panel_1.add(check2, "cell 0 2,aligny top,wrap");

		JLabel label = new JLabel(
				"At least how good should be answered to unlock");
		this.panel_1.add(label, "gaptop 10, cell 0 3,aligny top,wrap");

		this.validate();
	}

	private void changeDep(Question question, int value) {
    if(this.dependency.containsKey(question)) {
    	this.dependency.remove(question);
    	this.dependency.put(question, value);
    }
  }

	private ComboBoxModel getComboBoxModel() {
		String string[] = null;

		if (this.selectedQuestion != null) {
			this.crit = this.selectedQuestion.getCriterion();
			HashMap<String, String> params = this.crit.getParams();

			string = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				string[i] = params.get(String.valueOf(i + 1));
			}
		}

		return new DefaultComboBoxModel(string);
	}

	private Object[] getCat(Object o) {

		// activeQuestion
		ArrayList<Question> depQuestions = this.activeQuestion
				.getDependencies();

		ArrayList<Category> categories = null;
		ArrayList<Question> questions = null;

		if (o instanceof Assessment) {
			categories = ((Assessment) o).getCategories();
			questions = ((Assessment) o).getQuestions();
		} else if (o instanceof Category) {
			categories = ((Category) o).getCategories();
			questions = ((Category) o).getQuestions();
		} else
			return null;

		int anzCategories = categories.size();
		int anzQuestions = questions.size();
		boolean decide = false;
		
		Iterator<Question> it = questions.iterator();
		int count = 0;
		while (it.hasNext()) {
			Question q = it.next();
			if (q.getId() == activeQuestion.getId()) {
				decide = true;
			}
			if(!decide){
				count++;
			}
		}
		
		if(decide) {
			if(count < questions.size() && count >= 0) {
				questions.remove(count);
				anzQuestions = questions.size();
			}
		}
		
		int i = 0;
		int z = 0;

		Object rootNodes[] = new Object[anzCategories + anzQuestions];

		Vector vector = null;
		for (i = 0; i < anzCategories; i++) {
			vector = new NamedVector(categories.get(i),
					getCat(categories.get(i)));
			rootNodes[z] = vector;
			z++;
		}
		CheckBoxNode check = null;
		for (i = 0; i < anzQuestions; i++) {

			if (depQuestions != null && depQuestions.contains(questions.get(i))) {
				check = new CheckBoxNode(questions.get(i), true);
			} else {
				check = new CheckBoxNode(questions.get(i), false);
			}

			rootNodes[z] = check;
			z++;
		}
		return rootNodes;
	}

	public void elementChanged(Question question) {
		this.activeQuestion = question;
		actualize();
	}

	public void elementChanged(Assessment assessment) {
		this.removeAll();
	}

	public void elementChanged(Category category) {
		this.removeAll();
	}
}

class CheckBoxNodeRenderer implements TreeCellRenderer {
	private final JCheckBox leafRenderer = new JCheckBox();

	private final DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

	Color selectionBorderColor, selectionForeground, selectionBackground,
			textForeground, textBackground;

	protected JCheckBox getLeafRenderer() {
		return this.leafRenderer;
	}

	public CheckBoxNodeRenderer() {
		Font fontValue;
		fontValue = UIManager.getFont("Tree.font");
		if (fontValue != null) {
			this.leafRenderer.setFont(fontValue);
		}
		Boolean booleanValue = (Boolean) UIManager
				.get("Tree.drawsFocusBorderAroundIcon");
		this.leafRenderer.setFocusPainted((booleanValue != null)
				&& (booleanValue.booleanValue()));

		this.selectionBorderColor = UIManager
				.getColor("Tree.selectionBorderColor");
		this.selectionForeground = UIManager
				.getColor("Tree.selectionForeground");
		this.selectionBackground = UIManager
				.getColor("Tree.selectionBackground");
		this.textForeground = UIManager.getColor("Tree.textForeground");
		this.textBackground = UIManager.getColor("Tree.textBackground");
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		Component returnValue;
		if (leaf) {

			String stringValue = tree.convertValueToText(value, selected,
					expanded, leaf, row, false);
			this.leafRenderer.setText(stringValue);
			this.leafRenderer.setSelected(false);

			this.leafRenderer.setEnabled(tree.isEnabled());

			if (selected) {
				this.leafRenderer.setForeground(this.selectionForeground);
				this.leafRenderer.setBackground(this.selectionBackground);
			} else {
				this.leafRenderer.setForeground(this.textForeground);
				this.leafRenderer.setBackground(this.textBackground);
			}

			if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
				Object userObject = ((DefaultMutableTreeNode) value)
						.getUserObject();
				if (userObject instanceof CheckBoxNode) {
					CheckBoxNode node = (CheckBoxNode) userObject;
					this.leafRenderer.setText(node.getName());
					this.leafRenderer.setSelected(node.isSelected());
				}
			}
			returnValue = this.leafRenderer;
		} else {
			returnValue = this.nonLeafRenderer.getTreeCellRendererComponent(
					tree, value, selected, expanded, leaf, row, hasFocus);
		}
		return returnValue;
	}
}

class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {

	CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();

	ChangeEvent changeEvent = null;

	JTree tree;

	public CheckBoxNodeEditor(JTree tree) {
		this.tree = tree;
	}

	@Override
	public Object getCellEditorValue() {
		JCheckBox checkbox = this.renderer.getLeafRenderer();
		CheckBoxNode checkBoxNode = new CheckBoxNode(checkbox.getText(),
				checkbox.isSelected());
		return checkBoxNode;
	}

	@Override
	public boolean isCellEditable(EventObject event) {
		boolean returnValue = false;
		if (event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) event;
			TreePath path = this.tree.getPathForLocation(mouseEvent.getX(),
					mouseEvent.getY());
			if (path != null) {
				Object node = path.getLastPathComponent();
				if ((node != null) && (node instanceof DefaultMutableTreeNode)) {
					DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
					Object userObject = treeNode.getUserObject();
					returnValue = ((treeNode.isLeaf()) && (userObject instanceof CheckBoxNode));
				}
			}
		}
		return returnValue;
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row) {

		Component editor = this.renderer.getTreeCellRendererComponent(tree,
				value, true, expanded, leaf, row, true);

		// editor always selected / focused
		ItemListener itemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				if (stopCellEditing()) {
					fireEditingStopped();
				}
			}
		};
		if (editor instanceof JCheckBox) {
			((JCheckBox) editor).addItemListener(itemListener);
		}

		return editor;
	}
}

class CheckBoxNode {
	String name;

	Object Userobject;

	boolean selected;

	public CheckBoxNode(Object object, boolean selected) {
		this.name = object.toString();
		this.Userobject = object;
		this.selected = selected;
	}

	public Object getUserObj() {
		return this.Userobject;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean newValue) {
		this.selected = newValue;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String newValue) {
		this.name = newValue;
	}

	@Override
	public String toString() {
		return "[" + this.name + "/" + this.selected + "]";
	}
}

class NamedVector extends Vector {
	String name;

	Object usObject;

	public NamedVector(String name) {
		this.name = name;
	}

	public NamedVector(Object object, Object elements[]) {
		this.name = object.toString();
		this.usObject = object;
		for (int i = 0, n = elements.length; i < n; i++) {
			add(elements[i]);
		}
	}

	@Override
	public String toString() {
		return "[" + this.name + "]";
	}

	public Object getUserObj() {
		return this.usObject;
	}
}