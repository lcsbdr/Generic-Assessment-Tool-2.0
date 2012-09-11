package de.assessmenttool.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.gui.views.ViewAssessmentTree;
import de.assessmenttool.gui.views.ViewIF;
import de.assessmenttool.kernel.Log;
import de.assessmenttool.kernel.LogicIF;

public class ATPopupMenuAssessmentTree extends JPopupMenu {

  JMenuItem item1;

  JMenuItem item2;

  ViewAssessmentTree tree2;

  public ATPopupMenuAssessmentTree(final JTree tree, final MouseEvent e, final LogicIF logic, final ViewIF view) {
    super();

    this.tree2 = this.tree2;

    this.item1 = new JMenuItem("New Assessment");
    this.item1.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent arg0) {
        final ATNewEditStringFrame inputName = new ATNewEditStringFrame("Enter new name",
                                                                        "Please enter the name of the new assessment:");
        inputName.getOkButton().addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent e) {
            if (inputName.getInputText().trim().equals("")) {
              // set status
              Log.out("Please enter valid name for the new assessment.", Log.STATUS_NORMAL, Log.DIRECTION_STATUS);
            } else {
              logic.newAssessment(inputName.getInputText());
            }
            inputName.dispose();
            view.forceAssessmentRefresh();
          }
        });
      }
    });

    this.item2 = new JMenuItem("Delete selected Item");
    this.item2.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent arg0) {
        System.out.println(tree.getSelectionModel().getSelectionPaths().getClass());
        TreePath[] paths = tree.getSelectionPaths();
        for (int i = 0; i < paths.length; i++) {
          Object rem = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
          if (rem instanceof Assessment) {
            int n = JOptionPane.showOptionDialog(null,
                                                 "Do you really want to delete the Assessment "
                                                         + ((Assessment)rem).getName()
                                                         + "? All categories and questions will be deleted also!",
                                                 "Delete assessment",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.QUESTION_MESSAGE,
                                                 null,
                                                 null,
                                                 null);
            if (n == JOptionPane.YES_OPTION) {
              logic.removeAssessment((Assessment)rem);
            } else {
              return;
            }
            Log.out("Deleting Assessment " + ((Assessment)rem).getName() + "...", Log.STATUS_GOOD, Log.DIRECTION_BOTH);
          } else if (rem instanceof Category) {
            int n = JOptionPane.showOptionDialog(null,
                                                 "Do you really want to delete the Category "
                                                         + ((Category)rem).getName()
                                                         + "? All subcategories and questions will be deleted also!",
                                                 "Delete category",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.QUESTION_MESSAGE,
                                                 null,
                                                 null,
                                                 null);
            if (n == JOptionPane.YES_OPTION) {
              logic.removeCategory((Category)rem);
            } else {
              return;
            }
            Log.out("Deleting Category " + ((Category)rem).getName() + "...", Log.STATUS_GOOD, Log.DIRECTION_BOTH);

          } else {
            Log
              .out("Delete-Error: Type-dismatch. Not a Assessment nor a Category.", Log.STATUS_BAD, Log.DIRECTION_BOTH);
          }
        }
        // TODO funktioniert noch nicht
        view.forceAssessmentRefresh();
      }
    });

    this.add(this.item1);
    this.add(new JSeparator());
    this.add(this.item2);

    this.show(tree, e.getX(), e.getY());
  }

  public ATPopupMenuAssessmentTree(String label) {
    super(label);
  }

}
