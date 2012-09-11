package de.assessmenttool.components;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class ATTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894642014245713235L;

	public ATTable() {
		setFont();
		getTableHeader().setFont(getFont());
	}

	public ATTable(TableModel dm) {
		super(dm);
		setFont();
		getTableHeader().setFont(getFont());
	}

	public ATTable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
		setFont();
		getTableHeader().setFont(getFont());
	}

	public ATTable(int numRows, int numColumns) {
		super(numRows, numColumns);
		setFont();
		getTableHeader().setFont(getFont());
	}

	public ATTable(Vector<?> rowData, Vector<?> columnNames) {
		super(rowData, columnNames);
		setFont();
		getTableHeader().setFont(getFont());
	}

	public ATTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
		setFont();
		getTableHeader().setFont(getFont());
	}

	public ATTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
		setFont();
		getTableHeader().setFont(getFont());
	}

	private void setFont() {
		Font currFont = this.getFont();
		this.setFont(new Font(currFont.getFontName(), currFont.getStyle(), 10));
	}

}
