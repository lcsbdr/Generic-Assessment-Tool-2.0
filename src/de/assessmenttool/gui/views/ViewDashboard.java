package de.assessmenttool.gui.views;

import de.assessmenttool.components.ATPanel;
import net.miginfocom.swing.MigLayout;
import de.assessmenttool.components.ATLabel;
import de.assessmenttool.components.ATButton;

public class ViewDashboard extends ATPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6295161147342284248L;

	public ATButton btnEdit;
	
	public ViewDashboard() {
		setLayout(new MigLayout("", "[grow]", "[][grow][]"));
		
		ATLabel lblWhatDoYou = new ATLabel();
		lblWhatDoYou.setText("<html><font size=\"8\"><b>What do you want to do now?</b></font></html>");
		add(lblWhatDoYou, "cell 0 0");
		
		ATPanel panel = new ATPanel();
		add(panel, "cell 0 1,grow");
		panel.setLayout(new MigLayout("", "[][][][][]", "[][][]"));
		
		ATLabel lblCreateANew = new ATLabel();
		lblCreateANew.setText("<html><font size=\"4\">Create a <br/>new assessment.</font></html>");
		panel.add(lblCreateANew, "cell 0 0");
		
		ATLabel lbleditAnexistingAssessment = new ATLabel();
		lbleditAnexistingAssessment.setText("<html><font size=\"4\">Edit an<br/>existing assessment.</font></html>");
		panel.add(lbleditAnexistingAssessment, "cell 1 0");
		
		ATLabel lblanswerAnexistingAssessment = new ATLabel();
		lblanswerAnexistingAssessment.setText("<html><font size=\"4\">Answer an<br/>existing assessment.</font></html>");
		panel.add(lblanswerAnexistingAssessment, "cell 2 0");
		
		ATLabel lblviewTheResultsof = new ATLabel();
		lblviewTheResultsof.setText("<html><font size=\"4\">View the results<br/>of an existing<br/>assessment.</font></html>");
		panel.add(lblviewTheResultsof, "cell 3 0");
		
		ATLabel lblopenLastUsed = new ATLabel();
		lblopenLastUsed.setText("<html><font size=\"4\">Open last used <br/>configuration.</font></html>");
		panel.add(lblopenLastUsed, "cell 4 0");
		
		ATButton btnCreate = new ATButton();
		btnCreate.setText("Create");
		panel.add(btnCreate, "cell 0 2");
		
		btnEdit = new ATButton();
		btnEdit.setText("Edit");
		panel.add(btnEdit, "cell 1 2");
		
		ATButton btnAnswer = new ATButton();
		btnAnswer.setText("Answer");
		panel.add(btnAnswer, "cell 2 2");
		
		ATButton btnView = new ATButton();
		btnView.setText("View");
		panel.add(btnView, "cell 3 2");
		
		ATButton btnOpen = new ATButton();
		btnOpen.setText("Open");
		panel.add(btnOpen, "cell 4 2");
	}

}
