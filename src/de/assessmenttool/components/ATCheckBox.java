package de.assessmenttool.components;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

public class ATCheckBox extends JCheckBox {
	
	Object userObj;

	public ATCheckBox() {
		// TODO Auto-generated constructor stub
	}

	public ATCheckBox(Icon icon) {
		super(icon);
		// TODO Auto-generated constructor stub
	}

	public ATCheckBox(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ATCheckBox(Action a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	public ATCheckBox(Icon icon, boolean selected) {
		super(icon, selected);
		// TODO Auto-generated constructor stub
	}

	public ATCheckBox(String text, boolean selected) {
		super(text, selected);
		// TODO Auto-generated constructor stub
	}

	public ATCheckBox(String text, Icon icon) {
		super(text, icon);
		// TODO Auto-generated constructor stub
	}

	public ATCheckBox(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		// TODO Auto-generated constructor stub
	}
	
	public ATCheckBox(Object obj, boolean selected) {
		super();
		this.userObj = obj;
	}
	
	public Object getUserObj() {
		return this.userObj;
	}
}
