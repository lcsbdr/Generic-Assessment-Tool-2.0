package de.assessmenttool.gui.views;

import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.kernel.Model;

public class ViewKiviat extends ATPanel{
	
	private DefaultCategoryDataset dataset;
	private ArrayList<Question> questions;

	public ViewKiviat(){

		questions = new ArrayList<Question>();
		dataset = new DefaultCategoryDataset();
		Model data = new Model(); 
		
		questions = data.getAssessments().get(0).getAllQuestions();
	}
	public void addQuestion(Question qu){
		
		dataset.addValue(qu.getWeight(), "", qu.getName());
	}
	public void formKiviat(){
		
		for(Question qu: questions){
			
			addQuestion(qu);
		}
		SpiderWebPlot plot = new SpiderWebPlot(dataset);

		plot.setStartAngle(90);

		plot.setInteriorGap(0.30);

		plot.setToolTipGenerator(new StandardCategoryToolTipGenerator());

		JFreeChart chart = new JFreeChart("Kiviat Diagram", TextTitle.DEFAULT_FONT, plot, false);

		ChartUtilities.applyCurrentTheme(chart);
	    // we put the chart into a panel
	    ChartPanel chartPanel = new ChartPanel(chart);
	    // default size
	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	    // add it to our application
	    this.add(chartPanel);	
	}
	public static void main(String args[]){
		
		ViewKiviat testKiviat = new ViewKiviat(); 
		
		JFrame testFrame = new JFrame(); 
		testFrame.setContentPane(testKiviat);
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setVisible(true);
	}
}
