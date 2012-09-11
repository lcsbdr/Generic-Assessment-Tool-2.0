package de.assessmenttool.components;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;

public class ATLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ATLabel() {
		setFont();
	}

	public ATLabel(String text) {
		super(text);
		setFont();
	}

	public ATLabel(Icon image) {
		super(image);
		setFont();
	}

	public ATLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		setFont();
	}

	public ATLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		setFont();
	}

	public ATLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		setFont();
	}

	private void setFont() {
		Font currFont = this.getFont();
		this.setFont(new Font(currFont.getFontName(), currFont.getStyle(), 10));
	}

}
