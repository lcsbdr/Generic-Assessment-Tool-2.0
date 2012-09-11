package de.assessmenttool.gui.views;

import java.awt.Color;
import java.awt.GradientPaint;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.kernel.LogicIF;

public class ViewContentBar extends ATPanel {

  private CategoryDataset dataset;

  private JFreeChart chart;

  private ChartPanel chartPanel;

  private Assessment usedAssessment;

  private final LogicIF logic;

  public ViewContentBar(LogicIF logic, ViewIF view) {
    setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
    this.logic = logic;
  }

  /**
   * Returns a sample dataset.
   * 
   * @return The dataset.
   */
  private CategoryDataset createDataset() {
    // row keys...
    final String series1 = "First";
    final String series2 = "Second";
    final String series3 = "Third";

    // column keys...
    final String category1 = "Category 1";
    final String category2 = "Category 2";
    final String category3 = "Category 3";
    final String category4 = "Category 4";
    final String category5 = "Category 5";

    // create the dataset...
    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    for (Category tmp : this.usedAssessment.getCategories()) {
      dataset.addValue(tmp.getAnswered(), "Category", tmp.getName());
    }

    for (Question tmp : this.usedAssessment.getQuestions()) {
      int sumParams = tmp.getCriterion().getParams().size() - 1;
      int qAnswer = tmp.getAnswerInt() - 1;
      float prog = (((float)(sumParams - qAnswer) / sumParams));
      dataset.addValue(prog * 100.0f, "Question", tmp.getName());
    }

    // dataset.addValue(1.0, series1, category1);
    // dataset.addValue(4.0, series1, category2);
    // dataset.addValue(3.0, series1, category3);
    // dataset.addValue(5.0, series1, category4);
    // dataset.addValue(5.0, series1, category5);
    //
    // dataset.addValue(5.0, series2, category1);
    // dataset.addValue(7.0, series2, category2);
    // dataset.addValue(6.0, series2, category3);
    // dataset.addValue(8.0, series2, category4);
    // dataset.addValue(4.0, series2, category5);
    //
    // dataset.addValue(4.0, series3, category1);
    // dataset.addValue(3.0, series3, category2);
    // dataset.addValue(2.0, series3, category3);
    // dataset.addValue(3.0, series3, category4);
    // dataset.addValue(6.0, series3, category5);

    return dataset;

  }

  /**
   * Creates a sample chart.
   * 
   * @param dataset the dataset.
   * @return The chart.
   */
  private JFreeChart createChart(final CategoryDataset dataset) {

    // create the chart...
    final JFreeChart chart = ChartFactory.createBarChart(this.usedAssessment.getName(), // chart title
                                                         "Categories & Questions", // domain axis label
                                                         "Progress", // range axis label
                                                         dataset, // data
                                                         PlotOrientation.VERTICAL, // orientation
                                                         false, // include legend
                                                         true, // tooltips?
                                                         false // URLs?
      );

    // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

    // set the background color for the chart...
    chart.setBackgroundPaint(Color.white);

    // get a reference to the plot for further customisation...
    final CategoryPlot plot = chart.getCategoryPlot();
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);

    // set the range axis to display integers only...
    final NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

    // disable bar outlines...
    final BarRenderer renderer = (BarRenderer)plot.getRenderer();
    renderer.setDrawBarOutline(false);

    // set up gradient paints for series...
    final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f, 0.0f, Color.lightGray);
    final GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f, 0.0f, Color.lightGray);
    final GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f, 0.0f, Color.lightGray);
    renderer.setSeriesPaint(0, gp0);
    renderer.setSeriesPaint(1, gp1);
    renderer.setSeriesPaint(2, gp2);

    final CategoryAxis domainAxis = plot.getDomainAxis();
    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
    // OPTIONAL CUSTOMISATION COMPLETED.

    return chart;

  }

  public void repaintChart() {
    if (this.usedAssessment != null) {
      removeAll();
      this.dataset = createDataset();
      this.chart = createChart(this.dataset);
      this.chartPanel = new ChartPanel(this.chart);

      add(this.chartPanel);
      validate();
      repaint();
    }
  }

  public void elementChanged(Assessment assessment) {
    if (assessment != null) {
      this.usedAssessment = assessment;
      repaintChart();
    }
  }

  public void elementChanged(Category category) {
    this.usedAssessment = this.logic.getRelatedAssessment(category);
    repaintChart();
  }

  public void elementChanged(Question question) {
    this.usedAssessment = this.logic.getRelatedAssessment(question);
    repaintChart();
  }
}
