/*
 * Created by JFormDesigner on Sun Apr 27 00:57:31 KRAT 2025
 */

package ru.shift.task6.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

@Slf4j
@SuppressWarnings({"UnnecessaryUnicodeEscape", "DuplicatedCode"})
public class NicknameWindow extends JDialog {
  public NicknameWindow(Window owner) {
    super(owner);
    initComponents();
  }

  private void ok(ActionEvent e) {
    log.info("Trying to connect with nickname: {}", nicknameField.getText());
//    this.dispose();
  }

  @SuppressWarnings("Convert2MethodRef")
  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
    var dialogPane = new JPanel();
    var contentPanel = new JPanel();
    var label1 = new JLabel();
    nicknameField = new JTextField();
    errorLabel = new JLabel();
    var buttonBar = new JPanel();
    okButton = new JButton();

    //======== this ========
    setTitle("\u0412\u0445\u043e\u0434 \u0432 \u0447\u0430\u0442");
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    var contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== dialogPane ========
    {
      dialogPane.setLayout(new BorderLayout());

      //======== contentPanel ========
      {
        contentPanel.setLayout(new MigLayout(
          "fillx,insets dialog,hidemode 2,align center center",
          // columns
          "[fill]",
          // rows
          "[]" +
          "[]" +
          "[]" +
          "[]" +
          "[]"));

        //---- label1 ----
        label1.setText("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u0438\u043a\u043d\u0435\u0439\u043c \u0447\u0442\u043e\u0431\u044b \u0432\u043e\u0439\u0442\u0438 \u0432 \u0441\u0438\u0441\u0442\u0435\u043c\u0443");
        contentPanel.add(label1, "cell 0 1");

        //---- nicknameField ----
        nicknameField.setToolTipText("\u0414\u043b\u0438\u043d\u0430 \u0438\u043c\u0435\u043d\u0438 \u043e\u0442 3 \u0434\u043e 20 \u0441\u0438\u043c\u0432\u043e\u043b\u043e\u0432");
        contentPanel.add(nicknameField, "cell 0 2");

        //---- errorLabel ----
        errorLabel.setText("text");
        errorLabel.setForeground(new Color(0xff6666));
        errorLabel.setVisible(false);
        contentPanel.add(errorLabel, "cell 0 3");
      }
      dialogPane.add(contentPanel, BorderLayout.CENTER);

      //======== buttonBar ========
      {
        buttonBar.setLayout(new MigLayout(
          "insets dialog,alignx right",
          // columns
          "[button,fill]",
          // rows
          null));

        //---- okButton ----
        okButton.setText("OK");
        okButton.setEnabled(false);
        okButton.addActionListener(e -> ok(e));
        buttonBar.add(okButton, "cell 0 0");
      }
      dialogPane.add(buttonBar, BorderLayout.SOUTH);
    }
    contentPane.add(dialogPane, BorderLayout.CENTER);
    setSize(400, 185);
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on

    nicknameField.getDocument().addDocumentListener(createNicknameFieldDocumentListener());
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
  private JTextField nicknameField;
  private JLabel errorLabel;
  private JButton okButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on


  private DocumentListener createNicknameFieldDocumentListener() {
    return new DocumentListener() {
      private void validate() {
        String nickname = nicknameField.getText().strip();
        if (nickname.length() < 3) {
          return;
        }
        var isValid = nickname.length() <= 20 && !nickname.isBlank();

        okButton.setEnabled(isValid);
        errorLabel.setVisible(!isValid);
        errorLabel.setText(isValid ? "" : "Длина имени от 3 до 20 символов");
      }

      @Override
      public void insertUpdate(javax.swing.event.DocumentEvent e) {
        validate();
      }

      @Override
      public void removeUpdate(javax.swing.event.DocumentEvent e) {
        validate();
      }

      @Override
      public void changedUpdate(javax.swing.event.DocumentEvent e) {
        validate();
      }
    };
  }

}
