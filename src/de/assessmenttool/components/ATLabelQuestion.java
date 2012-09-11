package de.assessmenttool.components;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;

public class ATLabelQuestion extends ATLabel {

	public ATLabelQuestion() {
		setFont();
	}

	public ATLabelQuestion(String text) {
		super(text);
		setFont();
	}

	public ATLabelQuestion(Icon image) {
		super(image);
		setFont();
	}

	public ATLabelQuestion(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		setFont();
	}

	public ATLabelQuestion(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		setFont();
	}

	public ATLabelQuestion(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		setFont();
	}
	
	private void setFont() {
		Font currFont = this.getFont();
		this.setFont(new Font(currFont.getFontName(), currFont.getStyle(), 12));
	}
}
