package de.assessmenttool.components;

import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ATPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8475324060149467312L;

	public ATPanel() {
//		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public ATPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public ATPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public ATPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

}
