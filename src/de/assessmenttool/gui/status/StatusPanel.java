/*
 * (c) 2010 DKWEBSOLUTIONS
 * All rights reserved
 * Project "Surveillance"
 */
package de.assessmenttool.gui.status;

import de.assessmenttool.components.ATLabel;

/**
 * inits the surveillance frame.
 * 
 * @date 21.10.2010
 * @time 13:05:36
 * @author dkWebSolutions GbR
 */
public class StatusPanel extends ATLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* the isntance for the status panel (singleton) */
	private static StatusPanel instance = null;

	/**
	 * inits the status panel.
	 */
	private StatusPanel() {
		new StatusHideThread(this);
	}

	/**
	 * returns the instance of the statuspanel or generates it, if it does not
	 * exist.
	 * 
	 * @return the status panel.
	 */
	public static synchronized StatusPanel getInstance() {
		if (instance == null) {
			instance = new StatusPanel();
		}
		return instance;
	}

	/**
	 * sets the status and starts the hide thread for the status panel.
	 * 
	 * @param text
	 *            the text to set.
	 * @param mode
	 *            the mode in which the hidethread should work.
	 */
	public void setStatus(String text, int mode) {
		setText(text);
		StatusHideThread.go(mode);
	}

}