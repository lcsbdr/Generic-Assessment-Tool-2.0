package de.assessmenttool.components;

import java.awt.Font;

import javax.swing.JPasswordField;
import javax.swing.text.Document;

public class ATPasswordField extends JPasswordField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1350465261246822438L;

	public ATPasswordField() {
		setFont();
	}

	public ATPasswordField(String text) {
		super(text);
		setFont();
	}

	public ATPasswordField(int columns) {
		super(columns);
		setFont();
	}

	public ATPasswordField(String text, int columns) {
		super(text, columns);
		setFont();
	}

	public ATPasswordField(Document doc, String txt, int columns) {
		super(doc, txt, columns);
		setFont();
	}

	private void setFont() {
		Font currFont = this.getFont();
		this.setFont(new Font(currFont.getFontName(), currFont.getStyle(), 10));
	}

}
