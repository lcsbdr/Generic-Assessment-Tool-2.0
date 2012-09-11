/*
 * (c) 2010 DKWEBSOLUTIONS
 * All rights reserved
 * Project "Surveillance"
 */
package de.assessmenttool.gui.status;

import java.awt.Color;

import de.assessmenttool.kernel.Log;

/**
 * inits the database settings.
 * 
 * @date 21.10.2010
 * @time 13:05:36
 * @author dkWebSolutions GbR
 */
public class StatusHideThread extends Thread {

	private boolean light = true;

	private static long time1;

	private static int mode;

	private static boolean runs = false;

	private long lastaction;

	private StatusPanel statusLabel;

	public StatusHideThread(StatusPanel dksp) {
		statusLabel = dksp;
		this.start();
	}

	public static void go(int m) {
		time1 = System.currentTimeMillis();
		mode = m;
		runs = true;
	}

	@Override
	public void run() {
		while (true) {
			if (runs) {
				lastaction = System.currentTimeMillis();
				if (light) {
					switch (mode) {
					case Log.STATUS_NORMAL:
						statusLabel.setForeground(new Color(0, 0, 0));
						break;
					case Log.STATUS_BAD:
						statusLabel.setForeground(new Color(145, 0, 0));
						break;
					case Log.STATUS_GOOD:
						statusLabel.setForeground(new Color(0, 145, 0));
						break;
					}
					light = false;
				} else {
					switch (mode) {
					case Log.STATUS_NORMAL:
						statusLabel.setForeground(new Color(100, 100, 100));
						break;
					case Log.STATUS_BAD:
						statusLabel.setForeground(new Color(225, 0, 0));
						break;
					case Log.STATUS_GOOD:
						statusLabel.setForeground(new Color(0, 225, 0));
						break;
					}
					light = true;
				}

				if ((System.currentTimeMillis() - time1) > 4000) {
					switch (mode) {
					case Log.STATUS_NORMAL:
						statusLabel.setForeground(new Color(0, 0, 0));
						break;
					case Log.STATUS_BAD:
						statusLabel.setForeground(new Color(145, 0, 0));
						break;
					case Log.STATUS_GOOD:
						statusLabel.setForeground(new Color(0, 145, 0));
						break;
					}
					runs = false;
				}
			}

			if ((System.currentTimeMillis() - lastaction) > 15000) {
				lastaction = System.currentTimeMillis();
				statusLabel.setForeground(new Color(0, 0, 0));
				statusLabel.setText(" ");
			}

			try {
				Thread.sleep(200);
			} catch (InterruptedException ex) {
				Log.out("Error while trying to interrupt the status thread.", Log.STATUS_BAD, Log.DIRECTION_LOG);
			}
		}
	}
}
