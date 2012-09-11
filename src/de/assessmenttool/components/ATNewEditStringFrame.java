package de.assessmenttool.components;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ATNewEditStringFrame extends JFrame {
  /** version number */
  @SuppressWarnings("hiding")
  public static final String VER = "$Revision$";

  private final JTextField textField;

  private final ATButton btnOk;

  public ATNewEditStringFrame(String title, String labelTitle) {
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    getContentPane().setLayout(new MigLayout("", "[][grow][][]", "[][][][]"));

    JLabel label = new JLabel("New label");
    getContentPane().add(label, "cell 0 0 4 1");

    this.textField = new JTextField();
    getContentPane().add(this.textField, "cell 0 1,growx, spanx4");
    this.textField.setColumns(10);

    this.btnOk = new ATButton("OK");
    getContentPane().add(this.btnOk, "cell 3 3");

    setTitle(title);
    setSize(400, 160);
    label.setText(labelTitle);

    setVisible(true);
  }

  public ATButton getOkButton() {
    return this.btnOk;
  }

  public String getInputText() {
    return this.textField.getText();
  }

}
