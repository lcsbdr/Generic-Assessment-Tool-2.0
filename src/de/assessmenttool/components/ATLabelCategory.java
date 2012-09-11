package de.assessmenttool.components;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;

public class ATLabelCategory extends ATLabel {

	public ATLabelCategory() {
		setFont();
	}

	public ATLabelCategory(String text) {
		super(text);
		setFont();
	}

	public ATLabelCategory(Icon image) {
		super(image);
		setFont();
	}

	public ATLabelCategory(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		setFont();
	}

	public ATLabelCategory(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		setFont();
	}

	public ATLabelCategory(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		setFont();
	}

	private void setFont() {
		Font currFont = this.getFont();
		this.setFont(new Font(currFont.getFontName(), currFont.getStyle(), 18));
	}
}
