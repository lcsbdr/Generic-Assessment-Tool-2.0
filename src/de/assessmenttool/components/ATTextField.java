package de.assessmenttool.components;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class ATTextField extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ATTextField() {
		setFont();
	}

	public ATTextField(String text) {
		super(text);
		setFont();
	}

	public ATTextField(int columns) {
		super(columns);
		setFont();
	}

	public ATTextField(String text, int columns) {
		super(text, columns);
		setFont();
	}

	public ATTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		setFont();
	}

	private void setFont() {
		Font currFont = this.getFont();
		this.setFont(new Font(currFont.getFontName(), currFont.getStyle(), 10));
	}

}
