/*
 * Created by JFormDesigner on Sun May 04 22:28:02 KRAT 2025
 */

package ru.shift.task6.client.view.designer;

import java.awt.*;
import javax.swing.*;
import com.formdev.flatlaf.extras.*;
import net.miginfocom.swing.*;

/**
 * @author Lev Sokolov
 */
public class ErrorDialog extends JDialog {
  public ErrorDialog(Window owner) {
    super(owner);
    initComponents();
  }

  @SuppressWarnings("UnnecessaryUnicodeEscape")
  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
    dialogPane = new JPanel();
    contentPanel = new JPanel();
    panel1 = new JPanel();
    errorIcon = new JLabel();
    scrollPane1 = new JScrollPane();
    errorText = new JTextPane();
    buttonBar = new JPanel();
    okButton = new JButton();

    //======== this ========
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
    setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    setMaximumSize(null);
    setPreferredSize(new Dimension(400, 250));
    setResizable(false);
    setTitle("\u041e\u0448\u0431\u0438\u043a\u0430");
    setMinimumSize(null);
    var contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== dialogPane ========
    {
      dialogPane.setLayout(new BorderLayout());

      //======== contentPanel ========
      {
        contentPanel.setLayout(new MigLayout(
          "fill,insets dialog,hidemode 2,align center center",
          // columns
          "[fill]",
          // rows
          "[]" +
          "[]" +
          "[]"));

        //======== panel1 ========
        {
          panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

          //---- errorIcon ----
          errorIcon.setIcon(new FlatSVGIcon("icons/error.svg"));
          panel1.add(errorIcon);

          //======== scrollPane1 ========
          {
            scrollPane1.setViewportView(errorText);
          }
          panel1.add(scrollPane1);
        }
        contentPanel.add(panel1, "cell 0 1");
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
  protected JPanel dialogPane;
  protected JPanel contentPanel;
  protected JPanel panel1;
  protected JLabel errorIcon;
  protected JScrollPane scrollPane1;
  protected JTextPane errorText;
  protected JPanel buttonBar;
  protected JButton okButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
