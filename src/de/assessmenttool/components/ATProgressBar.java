package de.assessmenttool.components;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;

public class ATProgressBar extends JProgressBar {

	public ATProgressBar() {
		// TODO Auto-generated constructor stub
	}

	public ATProgressBar(int orient) {
		super(orient);
		// TODO Auto-generated constructor stub
	}

	public ATProgressBar(BoundedRangeModel newModel) {
		super(newModel);
		// TODO Auto-generated constructor stub
	}

	public ATProgressBar(int min, int max) {
		super(min, max);
		// TODO Auto-generated constructor stub
	}

	public ATProgressBar(int orient, int min, int max) {
		super(orient, min, max);
		// TODO Auto-generated constructor stub
	}

}
