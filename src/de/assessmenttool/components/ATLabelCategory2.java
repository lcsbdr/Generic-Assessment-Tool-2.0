package de.assessmenttool.components;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;

public class ATLabelCategory2 extends ATLabel {

	public ATLabelCategory2() {
		setFont();
	}

	public ATLabelCategory2(String text) {
		super(text);
		setFont();
	}

	public ATLabelCategory2(Icon image) {
		super(image);
		setFont();
	}

	public ATLabelCategory2(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		setFont();
	}

	public ATLabelCategory2(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		setFont();
	}

	public ATLabelCategory2(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		setFont();
	}

	private void setFont() {
		
		Font currFont = this.getFont();
		this.setFont(new Font(currFont.getFontName(), 1/*currFont.getStyle()*/, 14));
	}
}
