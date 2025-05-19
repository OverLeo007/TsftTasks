/*
 * Created by JFormDesigner on Tue Apr 29 19:39:42 KRAT 2025
 */

package ru.shift.task6.client.view.designer;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author Lev Sokolov
 */
public class ConnectionWindow extends JFrame {
  public ConnectionWindow() {
    initComponents();
  }

  @SuppressWarnings("UnnecessaryUnicodeEscape")
  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
    var buttonBar = new JPanel();
    okButton = new JButton();
    var dialogPane = new JPanel();
    contentPanel2 = new JPanel();
    var panel1 = new JPanel();
    addressField = new JTextField(21);
    var label1 = new JLabel();
    errorAddressLabel = new JLabel();
    statusAddressLabel = new JLabel();

    //======== this ========
    setTitle("\u041f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435");
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(340, 180));
    setPreferredSize(new Dimension(340, 180));
    setMaximumSize(new Dimension(340, 180));
    setResizable(false);
    var contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

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
    contentPane.add(buttonBar, BorderLayout.SOUTH);

    //======== dialogPane ========
    {
      dialogPane.setLayout(new BorderLayout());

      //======== contentPanel2 ========
      {
        contentPanel2.setLayout(new MigLayout(
          "fillx,insets dialog,hidemode 3,align center center",
          // columns
          "[fill]",
          // rows
          "[]" +
          "[]" +
          "[]"));

        //======== panel1 ========
        {
          panel1.setLayout(new BorderLayout(2, 2));

          //---- addressField ----
          addressField.setToolTipText("0.0.0.0:0 - 255.255.255.255:65535");
          panel1.add(addressField, BorderLayout.CENTER);

          //---- label1 ----
          label1.setText("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0430\u0434\u0440\u0435\u0441 \u0441\u0435\u0440\u0432\u0435\u0440\u0430");
          label1.setLabelFor(addressField);
          panel1.add(label1, BorderLayout.NORTH);

          //---- errorAddressLabel ----
          errorAddressLabel.setText("text");
          errorAddressLabel.setForeground(new Color(0xff6666));
          errorAddressLabel.setFocusable(false);
          errorAddressLabel.setVisible(false);
          panel1.add(errorAddressLabel, BorderLayout.SOUTH);

          //---- statusAddressLabel ----
          statusAddressLabel.setVisible(false);
          panel1.add(statusAddressLabel, BorderLayout.WEST);
        }
        contentPanel2.add(panel1, "cell 0 1");
      }
      dialogPane.add(contentPanel2, BorderLayout.CENTER);
    }
    contentPane.add(dialogPane, BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
  protected JButton okButton;
  protected JPanel contentPanel2;
  protected JTextField addressField;
  protected JLabel errorAddressLabel;
  protected JLabel statusAddressLabel;
  // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
