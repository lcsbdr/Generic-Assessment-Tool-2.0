package de.assessmenttool.gui.views;


public interface ViewIF {
  /** version number */
  public static final String VER = "$Revision$";

  public void setActiveElement(Object activeElement);

  public void forceAssessmentRefresh();

  void showViewDependency();

  void hideViewDependency();

  public void repaintBarChart();

  public void repaintKiviatChart();
}
