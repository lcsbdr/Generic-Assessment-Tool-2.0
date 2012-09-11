package de.assessmenttool.gui.views;

import java.util.ArrayList;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.kernel.LogicIF;
import de.assessmenttool.kernel.Model;

public class ViewContentKiviat extends ATPanel{
	
    private CategoryDataset dataset;

    private JFreeChart chart;

    private ChartPanel chartPanel;
	
	private Assessment usedAssessment;

	private final LogicIF logic;

	public ViewContentKiviat(LogicIF logic, ViewIF view){
		
	    setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
	    this.logic = logic;
	}
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

	    
//	    dataset.addValue(1.0, series1, category1);
//	    dataset.addValue(4.0, series1, category2);
//	    dataset.addValue(3.0, series1, category3);
//	    dataset.addValue(5.0, series1, category4);
//	    dataset.addValue(5.0, series1, category5);
//	
//	    dataset.addValue(5.0, series2, category1);
//	    dataset.addValue(7.0, series2, category2);
//	    dataset.addValue(6.0, series2, category3);
//	    dataset.addValue(8.0, series2, category4);
//	    dataset.addValue(4.0, series2, category5);
//	
//	    dataset.addValue(4.0, series3, category1);
//	    dataset.addValue(3.0, series3, category2);
//	    dataset.addValue(2.0, series3, category3);
//	    dataset.addValue(3.0, series3, category4);
//	    dataset.addValue(6.0, series3, category5);
	
	    return dataset;
	
	}
	
	public JFreeChart createChart(final CategoryDataset dataset){

		SpiderWebPlot plot = new SpiderWebPlot(dataset);

		plot.setStartAngle(90);
		plot.setInteriorGap(0.30);
		plot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
		
		final JFreeChart chart = new JFreeChart(this.usedAssessment.getName(), // chart title
				TextTitle.DEFAULT_FONT, 
				plot, 
				false
);

		ChartUtilities.applyCurrentTheme(chart);
//	    // we put the chart into a panel
//	    ChartPanel chartPanel = new ChartPanel(chart);
//	    // default size
//	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//	    // add it to our application
//	    this.add(chartPanel);	
	    
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
	public void elementChanged(Question question){
	    this.usedAssessment = this.logic.getRelatedAssessment(question);
	    repaintChart();		
	}
}
