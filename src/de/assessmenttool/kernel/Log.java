/*
 * (c) 2010 DKWEBSOLUTIONS
 * All rights reserved
 * Project "Surveillance"
 */
package de.assessmenttool.kernel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;

import de.assessmenttool.constants.GeneralConstants;
import de.assessmenttool.gui.status.StatusPanel;

/**
 * contains several log functions to provide log generation.
 * 
 * @date 21.10.2010
 * @time 13:05:36
 * @author dkWebSolutions GbR
 */
public class Log {

	/** indicates that the output is "normal info text" */
	public static final int STATUS_NORMAL = 0;

	/** indicates that the output is "bad error text" */
	public static final int STATUS_BAD = 1;

	/** indicates that the output is "good info text" */
	public static final int STATUS_GOOD = 2;

	/** indicates that the text should be written to log file */
	public static final int DIRECTION_LOG = 0;

	/** indicates that the text should be written to status field */
	public static final int DIRECTION_STATUS = 1;

	/**
	 * indicates that the text should be written to both - file and status field
	 */
	public static final int DIRECTION_BOTH = 2;

	/** if in debug mode, text will also be written to console */
	public static final boolean DEBUG = true;

	private static boolean SINGLE_LOGFILE = true;

	/** indicates when the log file was created */
	static long creation_time = -1;

	/**
	 * writes text to the specific log.
	 * 
	 * @param text
	 *            the text to write.
	 * @param mode
	 *            the mode.
	 * @param outputDirection
	 *            the direction where the text should be written.
	 * @param debug
	 *            if debug in console is on or not.
	 */
	public static void out(String text, int mode, int outputDirection) {
		// switch through the different direction types
		if (outputDirection == DIRECTION_STATUS || outputDirection == DIRECTION_BOTH) {
			// if in correct mode, weite text to satus field
			StatusPanel.getInstance().setStatus(text, mode);
		}
		if (outputDirection == DIRECTION_LOG || outputDirection == DIRECTION_BOTH) {
			// tmp strings for the old log text
			String oldLog = "", input = "";
			BufferedReader in;

			try {
				// load the old log file text
				in = new BufferedReader(new FileReader(GeneralConstants.LOG_FILE_PATH + "log_" + creation_time + ".html"));
				try {
					// read all lines and save them
					while ((input = in.readLine()) != null) {
						oldLog += input;
					}
					saveLogEntry(oldLog, text, mode);
				} catch (IOException e) {
					// error while reading file
					e.printStackTrace();
				}
			} catch (FileNotFoundException ex) {
				// file does not exist, so create it
				if (SINGLE_LOGFILE) {
					creation_time = 1;
				} else {
					creation_time = System.currentTimeMillis();
				}
				createLogFile();
				// and write the text
				out(text, mode, outputDirection);
			}
		}

		// if in debug mode, write text to console
		if (DEBUG) {
			System.out.println(text);
		}

	}

	/**
	 * inits the log file. (writes the start entry)
	 */
	private static void createLogFile() {
		Writer fw = null;
		try {
			// write the start entry
			fw = new FileWriter(GeneralConstants.LOG_FILE_PATH + "log_" + creation_time + ".html");
			fw.write(startEntry());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// in the end close the writer
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * generates the start entry and returns its content.
	 * 
	 * @return the start entry's content.
	 */
	private static String startEntry() {
		Calendar cal = Calendar.getInstance();
		String txt = "<html><head><meta http-equiv=\"refresh\" content=\"5; URL=log_" + creation_time + ".html\"/></head><body>";
		txt += "<font style=\"font-size:12pt;\"><center>Logeintrag vom <b>" + cal.get(Calendar.DAY_OF_MONTH) + ".";
		txt += cal.get(Calendar.MONTH) + 1 + "." + cal.get(Calendar.YEAR) + "</b></center></font><br/><br/>";
		txt += "<table style=\"font-size:8pt;\" border=\"0\"><tr><td><b>Uhrzeit</b></td><td><b>Ereignis</b></td></tr>";

		return txt;
	}

	/**
	 * formates and writes an log entry to the log file.
	 * 
	 * @param oldLog
	 *            the old log text of the file.
	 * @param text
	 *            the new log entry.
	 * @param mode
	 *            the mode how the log entry will be written.
	 */
	private static void saveLogEntry(String oldLog, String text, int mode) {
		Calendar cal = Calendar.getInstance();
		String tmptxt = "<tr><td><b>[" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":";
		tmptxt += cal.get(Calendar.SECOND) + "]</b></td><td><font color=\"" + getColor(mode) + "\" style=\"size:8pt;\">";
		tmptxt += text + "</font></td></tr>";

		Writer fw = null;
		try {
			// write the text
			fw = new FileWriter(GeneralConstants.LOG_FILE_PATH + "log_" + creation_time + ".html");
			fw.write(deleteEndEntry(oldLog) + tmptxt + endEntry());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// in the end close the writer
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * generates html color for the specific mode.
	 * 
	 * @param mode
	 *            the mode.
	 * @return the specific html color.
	 */
	private static String getColor(int mode) {
		String col = "#808080";
		switch (mode) {
		case STATUS_BAD:
			col = "darkred";
			break;
		case STATUS_GOOD:
			col = "darkgreen";
			break;
		}
		return col;
	}

	/**
	 * returns the end entry for the log file.
	 * 
	 * @return the end log text.
	 */
	private static String endEntry() {
		return "</table></body></html>";
	}

	/**
	 * extracts and deletes the end entry.
	 * 
	 * @param text
	 *            the text where the end entry should be deleted.
	 * @return the new text.
	 */
	private static String deleteEndEntry(String text) {
		text = text.replaceAll(endEntry(), "");
		return text;
	}
}
