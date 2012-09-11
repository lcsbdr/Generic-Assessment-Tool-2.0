package de.assessmenttool.gui.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.components.ATButton;
import de.assessmenttool.components.ATButtonGroup;
import de.assessmenttool.components.ATLabel;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.components.ATPasswordField;
import de.assessmenttool.components.ATRadioButton;
import de.assessmenttool.components.ATTextField;
import de.assessmenttool.gui.View;
import de.assessmenttool.kernel.LogicIF;

public class ViewDatabase extends ATPanel {

  /**
	 * 
	 */
  private static final long serialVersionUID = 8660219544591707164L;

  private ATButton btnConnect;

  private final LogicIF logic;

  private final View view;

  private ATTextField fieldHost;

  private ATTextField fieldDatabase;

  private ATTextField fieldUser;

  private ATPasswordField fieldPassword;

  private ATButtonGroup groupSource;

  private ATRadioButton fieldSourceInternal;

  private ATRadioButton fieldSourceExternal;

  private ATPanel panelSource;

  private ATLabel status;

  private final ATPanel panelExternalData;

  private JLabel lblPort;

  private ATTextField fieldPort;

  public ViewDatabase(LogicIF logic, View view) {
    this.logic = logic;
    this.view = view;
    setLayout(new MigLayout("", "[43px][6px][76px][183px][80px]", "[33px][][100px][13px][23px]"));

    ATLabel lblSource = new ATLabel();
    lblSource.setText("Source:");
    add(lblSource, "cell 0 0,alignx left,aligny center");

    add(getFieldSource(), "cell 2 0 3 1,growx,aligny top");

    ATLabel lblStatus = new ATLabel();
    lblStatus.setText("Status:");
    add(lblStatus, "cell 0 1,alignx left,aligny top");

    add(getStatus(), "cell 2 1,alignx left,aligny top");

    this.panelExternalData = new ATPanel();
    this.panelExternalData.setBorder(new LineBorder(Color.black, 1));
    this.panelExternalData.setLayout(new MigLayout("", "[][grow]", "[][][][][]"));
    this.panelExternalData.setVisible(false);

    ATLabel lblDatenbanktreiber = new ATLabel();
    lblDatenbanktreiber.setText("Host:");
    this.panelExternalData.add(lblDatenbanktreiber, "cell 0 0,alignx trailing");

    this.panelExternalData.add(getFieldHost(), "cell 1 0,growx");
    this.panelExternalData.add(getLblPort(), "cell 0 1,alignx trailing");
    this.panelExternalData.add(getFieldPort(), "cell 1 1,growx");

    ATLabel lblDatenbank = new ATLabel();
    lblDatenbank.setText("Database:");
    this.panelExternalData.add(lblDatenbank, "cell 0 2,alignx trailing");

    this.panelExternalData.add(getFieldDatabase(), "cell 1 2,growx");

    ATLabel lblBenutzername = new ATLabel();
    lblBenutzername.setText("Username:");
    this.panelExternalData.add(lblBenutzername, "cell 0 3,alignx trailing");

    this.panelExternalData.add(getFieldUser(), "cell 1 3,growx");

    ATLabel lblPasswort = new ATLabel();
    lblPasswort.setText("Password:");
    this.panelExternalData.add(lblPasswort, "cell 0 4,alignx trailing");

    this.panelExternalData.add(getFieldPassword(), "cell 1 4,growx");

    add(this.panelExternalData, "cell 0 2 5 1,growx,aligny top");

    setSize(400, 249);
    setConnected(true);
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

  private ATPanel getFieldSource() {
    if (this.panelSource == null) {
      ActionListener sourceAction = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
          if (ViewDatabase.this.fieldSourceExternal.isSelected()) {
            ViewDatabase.this.panelExternalData.setVisible(true);
          } else {
            ViewDatabase.this.panelExternalData.setVisible(false);
          }
        }
      };

      this.panelSource = new ATPanel();
      this.fieldSourceInternal = new ATRadioButton("Internal");
      this.fieldSourceInternal.setSelected(true);
      this.fieldSourceExternal = new ATRadioButton("External");
      this.fieldSourceExternal.addActionListener(sourceAction);
      this.fieldSourceInternal.addActionListener(sourceAction);

      this.groupSource = new ATButtonGroup();
      this.groupSource.add(this.fieldSourceInternal);
      this.groupSource.add(this.fieldSourceExternal);
      this.panelSource.setLayout(new MigLayout("", "[]25lp![][]", "[]"));
      this.panelSource.add(this.fieldSourceInternal, "cell 0 0");
      this.panelSource.add(this.fieldSourceExternal, "cell 1 0");
      this.panelSource.add(getBtnConnect(), "cell 2 0");
    }
    return this.panelSource;
  }

  private ATTextField getFieldHost() {
    if (this.fieldHost == null) {
      this.fieldHost = new ATTextField();
    }
    return this.fieldHost;
  }

  private ATTextField getFieldDatabase() {
    if (this.fieldDatabase == null) {
      this.fieldDatabase = new ATTextField();
    }
    return this.fieldDatabase;
  }

  private ATTextField getFieldUser() {
    if (this.fieldUser == null) {
      this.fieldUser = new ATTextField();
    }
    return this.fieldUser;
  }

  private ATPasswordField getFieldPassword() {
    if (this.fieldPassword == null) {
      this.fieldPassword = new ATPasswordField();
    }
    return this.fieldPassword;
  }

  private ATButton getBtnConnect() {
    if (this.btnConnect == null) {
      this.btnConnect = new ATButton();
      this.btnConnect.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
          if (getStatus().getText().equals("Connected")) {
            if (ViewDatabase.this.logic.dbDisconnect()) {
              setConnected(false);
            }
          } else {
            if (ViewDatabase.this.fieldSourceInternal.isSelected()) {
              if (ViewDatabase.this.logic.dbConnectIntern()) {
                setConnected(true);
              } else {
                setConnected(false);
              }
            } else {
              if (ViewDatabase.this.logic.dbConnectExtern(ViewDatabase.this.fieldHost.getText(),
                                                          ViewDatabase.this.fieldPort.getText(),
                                                          ViewDatabase.this.fieldDatabase.getText(),
                                                          ViewDatabase.this.fieldUser.getText(),
                                                          ViewDatabase.this.fieldPassword.getPassword())) {
                setConnected(true);
              } else {
                setConnected(false);
              }
            }
          }
          ViewDatabase.this.view.forceAssessmentRefresh();
          ViewDatabase.this.view.closeAllViews();
        }
      });
      this.btnConnect.setText("Connect");
    }
    return this.btnConnect;
  }

  private void setConnected(boolean connected) {
    if (connected) {
      getFieldHost().setEnabled(false);
      getFieldPort().setEnabled(false);
      getFieldDatabase().setEnabled(false);
      getFieldUser().setEnabled(false);
      getFieldPassword().setEnabled(false);
      getStatus().setText("Connected");
      getStatus().setForeground(new Color(0, 180, 0));
      getBtnConnect().setText("Disconnect");
      this.fieldSourceExternal.setEnabled(false);
      this.fieldSourceInternal.setEnabled(false);

    } else {
      getFieldHost().setEnabled(true);
      getFieldDatabase().setEnabled(true);
      getFieldPort().setEnabled(true);
      getFieldUser().setEnabled(true);
      getFieldPassword().setEnabled(true);
      getStatus().setText("Disconnected");
      getStatus().setForeground(new Color(180, 0, 0));
      getBtnConnect().setText("Connect");
      this.fieldSourceExternal.setEnabled(true);
      this.fieldSourceInternal.setEnabled(true);
    }
  }

  private JLabel getLblPort() {
    if (this.lblPort == null) {
      this.lblPort = new JLabel("Port:");
      this.lblPort.setFont(new Font("Tahoma", Font.PLAIN, 10));
    }
    return this.lblPort;
  }

  private ATTextField getFieldPort() {
    if (this.fieldPort == null) {
      this.fieldPort = new ATTextField();
    }
    return this.fieldPort;
  }
}
