package de.assessmenttool.components;

import java.awt.Font;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;

public class ATRadioButton extends JRadioButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4585518922565419993L;

	public ATRadioButton() {
		setFont();
	}

	public ATRadioButton(Icon icon) {
		super(icon);
		setFont();
	}

	public ATRadioButton(Action a) {
		super(a);
		setFont();
	}

	public ATRadioButton(String text) {
		super(text);
		setFont();
	}

	public ATRadioButton(Icon icon, boolean selected) {
		super(icon, selected);
		setFont();
	}

	public ATRadioButton(String text, boolean selected) {
		super(text, selected);
		setFont();
	}

	public ATRadioButton(String text, Icon icon) {
		super(text, icon);
		setFont();
	}

	public ATRadioButton(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		setFont();
	}

	private void setFont() {
		Font currFont = this.getFont();
		this.setFont(new Font(currFont.getFontName(), currFont.getStyle(), 10));
	}

}
