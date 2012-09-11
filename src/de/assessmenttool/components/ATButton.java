package de.assessmenttool.components;

import java.awt.Font;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class ATButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9071936270794017554L;

	public ATButton() {
		setFont();
	}

	public ATButton(Icon icon) {
		super(icon);
		setFont();
	}

	public ATButton(String text) {
		super(text);
		setFont();
	}

	public ATButton(Action a) {
		super(a);
		setFont();
	}

	public ATButton(String text, Icon icon) {
		super(text, icon);
		setFont();
	}

	private void setFont() {
		Font currFont = this.getFont();
		this.setFont(new Font(currFont.getFontName(), currFont.getStyle(), 10));
	}
}
