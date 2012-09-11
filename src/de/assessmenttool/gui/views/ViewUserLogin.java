package de.assessmenttool.gui.views;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.components.ATButton;
import de.assessmenttool.components.ATButtonGroup;
import de.assessmenttool.components.ATLabel;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.components.ATPasswordField;
import de.assessmenttool.components.ATRadioButton;
import de.assessmenttool.components.ATTextField;

public class ViewUserLogin extends ATPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3322390181418233890L;
	private ATButton btnConnect;
	ATTextField fieldDriver;
	ATTextField fieldDatabase;
	ATTextField fieldUser;
	ATPasswordField fieldPassword;
	ATButtonGroup groupSource;
	ATRadioButton fieldSourceInternal;
	ATRadioButton fieldSourceExternal;
	ATPanel panelSource;
	ATLabel status, role;
	ATPanel panelExternalData;

	public ViewUserLogin() {
		setLayout(new MigLayout("", "[][grow]", "[][][][][]"));
		ATLabel lblDatenbanktreiber = new ATLabel();
		lblDatenbanktreiber.setText("Username:");
		add(lblDatenbanktreiber, "cell 0 0,alignx trailing");

		add(getFieldUsername(), "cell 1 0,growx");

		ATLabel lblPasswort = new ATLabel();
		lblPasswort.setText("Password:");
		add(lblPasswort, "cell 0 1,alignx trailing");

		add(getFieldPassword(), "cell 1 1,growx");

		ATLabel lblStatus = new ATLabel();
		lblStatus.setText("Status:");
		add(lblStatus, "cell 0 2,alignx right");

		add(getStatus(), "cell 1 2");		
		
		ATLabel lblRole = new ATLabel();
		lblRole.setText("Role:");
		add(lblRole, "cell 0 3,alignx right");

		add(getRole(), "cell 1 3");

		// add connect button
		add(getBtnConnect(), "cell 1 4,alignx right");

		setSize(400, 250);
		setVisible(true);
	}

	private ATLabel getStatus() {
		if (this.status == null) {
			this.status = new ATLabel();
			this.status.setText("Disconnected");
			this.status.setForeground(new Color(180, 0, 0));
		}
		return this.status;
	}
	
	private ATLabel getRole() {
		if (this.role == null) {
			this.role = new ATLabel();
			this.role.setText("");
			this.role.setForeground(new Color(180, 0, 0));
		}
		return this.role;
	}	

	private ATTextField getFieldUsername() {
		if (this.fieldDriver == null) {
			this.fieldDriver = new ATTextField();
		}
		return this.fieldDriver;
	}

	private ATPasswordField getFieldPassword() {
		if (this.fieldPassword == null) {
			this.fieldPassword = new ATPasswordField();
		}
		return this.fieldPassword;
	}

	public ATButton getBtnConnect() {
		if (this.btnConnect == null) {
			this.btnConnect = new ATButton();
			this.btnConnect.setText("Login");
		}
		return this.btnConnect;
	}

	public void setLoggedin(String role) {
		this.role.setText(role);
		this.status.setText("Logged in");
		this.status.setForeground(new Color(0, 180, 0));
		getFieldUsername().setEnabled(false);
		getFieldPassword().setEnabled(false);
	}

}
