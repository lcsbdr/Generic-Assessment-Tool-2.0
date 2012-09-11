package de.assessmenttool.kernel;

import java.awt.EventQueue;

import javax.swing.UIManager;

import de.assessmenttool.gui.View;

public class GenericAssessmentTool {

  private Presenter presenter;

  private View view;

  private Model model;

  public GenericAssessmentTool() {
    // set the default look and feel
    setLAF();

    EventQueue.invokeLater(new Runnable(){
      @Override
      public void run() {
        // set the model
        setModel(new Model());

        // generate the view and set the logic interface
        setView(new View(GenericAssessmentTool.this.model.getLogicIF()));

        // generate the presenter
        GenericAssessmentTool.this.presenter = new Presenter(GenericAssessmentTool.this.view, GenericAssessmentTool.this.model);
      }
    });

    // set status
    Log.out("Generic Assessment Tool started.", Log.STATUS_NORMAL, Log.DIRECTION_BOTH);
  }

  /**
   * Set the look and feel of the program.
   */
  private void setLAF() {
    EventQueue.invokeLater(new Runnable(){
      @Override
      public void run() {
        // set the look and feel to "Nimbus"
        try {
          UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    // EventQueue.invokeLater(new Runnable() {
    // public void run() {
    // try {
    // UIManager.setLookAndFeel(new InfoNodeLookAndFeel());
    // } catch (UnsupportedLookAndFeelException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // });
  }

  public Model getModel() {
    return this.model;
  }

  public void setModel(Model model) {
    this.model = model;
  }

  public View getView() {
    return this.view;
  }

  public void setView(View view) {
    this.view = view;
  }

  public Presenter getPresenter() {
    return this.presenter;
  }

  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  /**
   * @param args The program arguments.
   */
  public static void main(String[] args) {
    new GenericAssessmentTool();
  }

}
