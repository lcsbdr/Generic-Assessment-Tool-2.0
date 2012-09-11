package de.assessmenttool.gui.views;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.components.ATPopupMenuAssessmentTree;
import de.assessmenttool.components.ATTree;
import de.assessmenttool.kernel.LogicIF;

/**
 * Shows tree of project-hierarchy
 * 
 * @author vietzd
 */
public class ViewAssessmentTree extends ATPanel {

  LogicIF logic;

  private final ViewIF view;

  ArrayList<Assessment> assessments;

  ArrayList<Category> categories;

//   ArrayList<Question> questions;

  DefaultMutableTreeNode root;

  private final JTree tree;

  private final JScrollPane treeView;

  /**
   * Constructor
   * 
   * @param logic
   *          Logic-Interface
   * @param view
   *          View-Interface
   */
  public ViewAssessmentTree(LogicIF logic, ViewIF view) {
    this.view = view;
    this.logic = logic;

    actualize();

    this.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
    this.tree = new ATTree(this.root);
    this.tree.setRootVisible(false);
    this.tree.setOpaque(false);
    addTreeSelectionListener(this.tree, logic);
    this.treeView = new JScrollPane(this.tree);
    this.add(this.treeView, "growy");
    DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)this.tree.getCellRenderer();
    renderer.setClosedIcon(null);
    renderer.setOpenIcon(null);
    renderer.setLeafIcon(null);
    // renderer.setLeafIcon(new ImageIcon("images/buddy.gif"));

    // TODO icons ändern
    // JLabel label = (JLabel)tree.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);
    // if(((DefaultMutableTreeNode)value).isRoot()) label.setIcon(new ImageIcon("save.gif"));
  }

  public void actualize() {
    this.assessments = this.logic.getAssessments();

    if (this.tree != null) {
      this.tree.removeAll();
    }

    this.root = new DefaultMutableTreeNode("hallo");
    int j, i;
    for (j = 0; j < this.assessments.size(); j++) {
      DefaultMutableTreeNode node = new DefaultMutableTreeNode(this.assessments.get(j));
      this.categories = this.assessments.get(j).getCategories();
//       this.questions = this.assessments.get(j).getQuestions();
      for (i = 0; i < this.categories.size(); i++) {
        DefaultMutableTreeNode node_1 = new DefaultMutableTreeNode();
        node_1.setUserObject(this.categories.get(i));
        createSons(node_1, this.categories.get(i));
        node.add(node_1);
      }
//       for (i = 0; i < this.questions.size(); i++) {
//       DefaultMutableTreeNode node_2 = new DefaultMutableTreeNode();
//       node_2.setUserObject(this.questions.get(i));
//       node.add(node_2);
//       }
      this.root.add(node);
    }

    if (this.tree != null) {
      this.tree.setModel(new DefaultTreeModel(this.root));
    }
  }

  // private void expandTree(JTree tree) {
  // for (int i = 0; i < tree.getRowCount(); i++) {
  // alles aufklappen
  // tree.expandRow(i);

  // bis zu den ersten Fragen(ohne) aufklappen
  // TreePath path = tree.getPathForRow(i);
  // DefaultMutableTreeNode nodeForPath = (DefaultMutableTreeNode) path.getLastPathComponent();
  //
  // Object object = nodeForPath.getUserObject();
  // if (object instanceof Assessment) {
  // Assessment as = (Assessment) object;
  // tree.expandRow(i);
  // } else if (object instanceof Category) {
  // Category cat = (Category) object;
  // if (cat.getQuestions().size() > 0) {
  // tree.collapseRow(i);
  // } else {
  // tree.expandRow(i);
  // }
  // }
  // }
  // }

  /**
   * Adds TreeSelectionListener and MouseListener to Assessment-Tree
   * 
   * @param tree
   *          JTree that gets the ActionListener
   * @param logic
   *          Logic-Interface
   */
  private void addTreeSelectionListener(final JTree tree, final LogicIF logic) {

    /**
     * Handles an valueChanged Event
     */
    TreeSelectionListener tsl = new TreeSelectionListener(){

      private DefaultMutableTreeNode tempTreeNode;

      @Override
      public void valueChanged(TreeSelectionEvent arg0) {

        this.tempTreeNode = (DefaultMutableTreeNode)arg0.getPath().getLastPathComponent();
        ViewAssessmentTree.this.view.setActiveElement(this.tempTreeNode.getUserObject());
        this.tempTreeNode.getUserObject();
      }
    };

    /**
     * Creates PopupMenu on right-click
     */
    MouseListener ml = new MouseAdapter(){

      private ATPopupMenuAssessmentTree popup;

      @Override
      public void mousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON3) {
          this.popup = new ATPopupMenuAssessmentTree(tree, e, logic, ViewAssessmentTree.this.view);
        }
      }
    };
    tree.addTreeSelectionListener(tsl);
    tree.addMouseListener(ml);
  }

  /**
   * Recursive method to add sons to a node
   * 
   * @param node
   *          Node that gets new entries
   * @param category
   *          complement to node (UserObject)
   */
  private void createSons(DefaultMutableTreeNode node, Category category) {

    ArrayList<Category> categories = category.getCategories();
//     ArrayList<Question> questions = category.getQuestions();
    int i;
    for (i = 0; i < categories.size(); i++) {
      DefaultMutableTreeNode node_1 = new DefaultMutableTreeNode();
      node_1.setUserObject(categories.get(i));
      createSons(node_1, categories.get(i));
      node.add(node_1);
    }
//     for (i = 0; i < questions.size(); i++) {
//     DefaultMutableTreeNode node_1 = new DefaultMutableTreeNode();
//     node_1.setUserObject(questions.get(i));
//     node.add(node_1);
//     }
  }

  /**
   * @param assessment
   *          Assessment
   * @see de.assessmenttool.gui.View.setActiveElement
   */
  public void elementChanged(Object o) {
    // TODO setSelectedRow
  }
}
