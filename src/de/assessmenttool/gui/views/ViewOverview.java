package de.assessmenttool.gui.views;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.components.ATScrollPane;
import de.assessmenttool.components.ATTextPane;

public class ViewOverview extends ATPanel {

	private static final long serialVersionUID = -4070345573805371203L;
	private Question activeQuestion;
	private ATPanel panel = new ATPanel();
	
	public ViewOverview() {
		this.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
		panel.setLayout(new MigLayout("", "[grow,fill]", ""));
	}
	
	public void elementChanged(Object o) {
		this.removeAll();
		panel.removeAll();
		
		ATScrollPane scroll;
		ATTextPane txt;
		if(o instanceof Assessment) {
		}else if(o instanceof Category) {
		}else if(o instanceof Question) {
			this.activeQuestion = (Question)o;
			ArrayList<Question> dependencies = activeQuestion.getDependencies();
			ArrayList<Integer> answers = activeQuestion.getAnswersNeeded();
			
			for (int i = 0; i < dependencies.size(); i++) {
				txt = new ATTextPane();
				txt.setText(dependencies.get(i).getName() + "   " + answers.get(i));
				txt.setOpaque(false);
				txt.setBackground(new Color(0,0,0,0));
				panel.add(txt, "wrap, growx");
			}
			
			txt = new ATTextPane();
			txt.setText(activeQuestion.getNote());
			txt.setOpaque(false);
			txt.setBackground(new Color(0,0,0,0));
			panel.add(txt, "growx");
		}
		
		scroll = new ATScrollPane(panel);
		this.add(scroll);
		
		this.repaint();
		this.validate();
		panel.repaint();
		panel.validate();
	}
}
