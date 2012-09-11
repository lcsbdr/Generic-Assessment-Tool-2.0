package de.assessmenttool.kernel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.gui.View;

public class Presenter {

  private final View view;

  private final Model model;

  public Presenter(View view, Model model) {
    // set the current used view and model
    this.view = view;
    this.model = model;

    // Add the view actions
    // addDatabaseActions();

    // Set the assessment data for the assessment view
    // setAssessments();

    // Add the assessment view specific actions
    addAssessmentViewActions();

    // Add the top categories view specific actions
    // addTopCategoriesViewActions();

    // addDashboardActions();
  }

  // private void addDashboardActions() {
  // // add action to database connect button
  // this.view.getViewDashboard().btnEdit
  // .addActionListener(new ActionListener() {
  // @Override
  // public void actionPerformed(ActionEvent e) {
  // view.restoreDashboard();
  // }
  // });
  // }

  // private void addDatabaseActions() {
  // // add action to database connect button
  // this.view.getViewDatabase().getBtnConnect().addActionListener(new ActionListener(){
  // @Override
  // public void actionPerformed(ActionEvent e) {
  // if (!Presenter.this.model.databaseIsConnected()) {
  // // if (model.databaseConnect()) {
  // Presenter.this.view.getViewDatabase().setConnected(true);
  // // TODO only for prototype
  // Presenter.this.view.getViewUserLogin().setLoggedin("Administrator");
  // Presenter.this.view.getViewAssessments().repaint();
  // // }
  // } else {
  // if (Presenter.this.model.databaseDisconnect()) {
  // Presenter.this.view.getViewDatabase().setConnected(false);
  // }
  // }
  // }
  // });
  // }

  // private void addTopCategoriesViewActions() {
  // // TODO
  // // this.view.getViewTopCategories().projectsTable.addMouseListener(new MouseAdapter(){
  // @Override
  // public void mouseClicked(MouseEvent e) {
  // // On change load data for the specific assessment
  // int selRow = Presenter.this.view.getViewTopCategories().projectsTable.getSelectedRow();
  // if (selRow != Presenter.this.view.getViewContent().selectedTopCat) {
  // // Update assessment specific views
  // Presenter.this.view.getViewContent().selectedTopCat = selRow;
  // Presenter.this.view.getViewContent().myFireTableDataChanged();
  // }
  // }
  // });
  // }

  private void addAssessmentViewActions() {
    // TODO
    this.view.getViewAssessments().projectsTable.addMouseListener(new MouseAdapter(){
      @Override
      public void mouseClicked(MouseEvent e) {
        // On change load data for the specific assessment
        int selRow = Presenter.this.view.getViewAssessments().projectsTable.getSelectedRow();
        if (selRow != Presenter.this.view.getViewAssessments().selectedAssessment) {
          // Update assessment specific views
          Presenter.this.view.getViewAssessments().selectedAssessment = selRow;

          // Add the edit (top) categories tab
          // view.addAssessmentCreationView();

          // Set top categories for the top categories view
          // view.setTopCategories(curr.getTopCategories());

          // Set top categories for the content view
          // Presenter.this.view.getViewContent().setTopCats(curr.getAxis(Axis.X).getTopCategories());
        }
      }
    });
  }

  private void updateProjectOverview(Assessment curr) {
    // this.view.getViewOverview().getAssessmentName().setText(curr.getName());
    // this.view.getViewOverview().getAssessment3D().setText(String.valueOf(curr.is3D()));
    // this.view.getViewOverview().getAssessmentCreation().setText(curr.getCreation().toString());
    // this.view.getViewOverview().getAssessmentAuthor().setText(curr.getCreator());
    // this.view.getViewOverview().getAssessmentLastMod().setText(curr.getLastModificationDate().toString());
    // this.view.getViewOverview().getAssessmentLastModifier().setText(curr.getLastModifier());
    // this.view.getViewOverview().getAssessmentCompletion().setText(String.valueOf(curr.getCompletion()) + " %");
  }
}
