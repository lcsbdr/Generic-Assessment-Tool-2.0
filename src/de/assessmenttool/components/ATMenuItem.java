package de.assessmenttool.components;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

public class ATMenuItem extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ATMenuItem() {
		// TODO Auto-generated constructor stub
	}

	public ATMenuItem(Icon icon) {
		super(icon);
		// TODO Auto-generated constructor stub
	}

	public ATMenuItem(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ATMenuItem(Action a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	public ATMenuItem(String text, Icon icon) {
		super(text, icon);
		// TODO Auto-generated constructor stub
	}

	public ATMenuItem(String text, int mnemonic) {
		super(text, mnemonic);
		// TODO Auto-generated constructor stub
	}

}
