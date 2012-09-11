package de.assessmenttool.components;

import java.awt.Component;

import javax.swing.JScrollPane;

public class ATScrollPane extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8038428868079384967L;

	public ATScrollPane() {
		// TODO Auto-generated constructor stub
	}

	public ATScrollPane(Component view) {
		super(view);
		// TODO Auto-generated constructor stub
	}

	public ATScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
		// TODO Auto-generated constructor stub
	}

	public ATScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
		// TODO Auto-generated constructor stub
	}

}
