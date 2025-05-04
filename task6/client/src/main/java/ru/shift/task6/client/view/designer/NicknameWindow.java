/*
 * Created by JFormDesigner on Tue Apr 29 17:07:21 KRAT 2025
 */

package ru.shift.task6.client.view.designer;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author Lev Sokolov
 */
public class NicknameWindow extends JFrame {
  public NicknameWindow() {
    initComponents();
  }

  @SuppressWarnings("UnnecessaryUnicodeEscape")
  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
    var dialogPane = new JPanel();
    var contentPanel = new JPanel();
    var panel2 = new JPanel();
    nicknameField = new JTextField(20);
    var label2 = new JLabel();
    nicknameErrorLabel = new JLabel();
    statusNicknameLabel = new JLabel();
    var buttonBar = new JPanel();
    okButton = new JButton();

    //======== this ========
    setTitle("\u0412\u0445\u043e\u0434 \u0432 \u0447\u0430\u0442");
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    setMinimumSize(new Dimension(340, 180));
    setPreferredSize(new Dimension(340, 180));
    setMaximumSize(new Dimension(340, 180));
    setLocationByPlatform(true);
    setResizable(false);
    var contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== dialogPane ========
    {
      dialogPane.setLayout(new BorderLayout());

      //======== contentPanel ========
      {
        contentPanel.setLayout(new MigLayout(
          "fillx,insets dialog,hidemode 3,align center center",
          // columns
          "[fill]",
          // rows
          "[]" +
          "[]" +
          "[]"));

        //======== panel2 ========
        {
          panel2.setLayout(new BorderLayout(2, 2));

          //---- nicknameField ----
          nicknameField.setToolTipText("\u0414\u043b\u0438\u043d\u0430 \u0438\u043c\u0435\u043d\u0438 \u043e\u0442 3 \u0434\u043e 20 \u0441\u0438\u043c\u0432\u043e\u043b\u043e\u0432");
          panel2.add(nicknameField, BorderLayout.CENTER);

          //---- label2 ----
          label2.setText("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0438\u043c\u044f \u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u044f");
          label2.setLabelFor(nicknameField);
          panel2.add(label2, BorderLayout.NORTH);

          //---- nicknameErrorLabel ----
          nicknameErrorLabel.setText("text");
          nicknameErrorLabel.setForeground(new Color(0xff6666));
          nicknameErrorLabel.setFocusable(false);
          nicknameErrorLabel.setVisible(false);
          panel2.add(nicknameErrorLabel, BorderLayout.SOUTH);

          //---- statusNicknameLabel ----
          statusNicknameLabel.setVisible(false);
          panel2.add(statusNicknameLabel, BorderLayout.WEST);
        }
        contentPanel.add(panel2, "cell 0 1");
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
        buttonBar.add(okButton, "cell 0 0");
      }
      dialogPane.add(buttonBar, BorderLayout.SOUTH);
    }
    contentPane.add(dialogPane, BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
  protected JTextField nicknameField;
  protected JLabel nicknameErrorLabel;
  protected JLabel statusNicknameLabel;
  protected JButton okButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
