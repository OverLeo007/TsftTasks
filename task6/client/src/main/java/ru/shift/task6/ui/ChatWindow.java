/*
 * Created by JFormDesigner on Sun Apr 27 01:49:14 KRAT 2025
 */

package ru.shift.task6.ui;

import java.awt.*;
import javax.swing.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author leva
 */
public class ChatWindow extends JFrame {
  public ChatWindow() {
    initComponents();
  }

  private void createUIComponents() {
    // TODO: add custom component creation code here
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
    var scrollPane2 = new JScrollPane();
    chatArea = new JTextArea();
    var panel2 = new JPanel();
    messageField = new JTextField();
    sendButton = new JButton();
    var panel3 = new JPanel();
    var label1 = new JLabel();
    var scrollPane1 = new JScrollPane();
    usersList = new JList();

    //======== this ========
    setTitle("\u0427\u0430\u0442");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
    var contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout(2, 2));

    //======== scrollPane2 ========
    {
      scrollPane2.setMinimumSize(new Dimension(122, 56));
      scrollPane2.setPreferredSize(new Dimension(122, 56));

      //---- chatArea ----
      chatArea.setPreferredSize(new Dimension(120, 56));
      chatArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
      chatArea.setLineWrap(true);
      chatArea.setEditable(false);
      chatArea.setFont(new Font("JetBrains Mono", chatArea.getFont().getStyle(), chatArea.getFont().getSize()));
      chatArea.setFocusable(false);
      scrollPane2.setViewportView(chatArea);
    }
    contentPane.add(scrollPane2, BorderLayout.CENTER);

    //======== panel2 ========
    {
      panel2.setPreferredSize(new Dimension(91, 35));
      panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));

      //---- messageField ----
      messageField.setPreferredSize(new Dimension(49, 31));
      messageField.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
      panel2.add(messageField);

      //---- sendButton ----
      sendButton.setPreferredSize(new Dimension(42, 31));
      sendButton.setIcon(new FlatSVGIcon("send.svg"));
      sendButton.setMinimumSize(new Dimension(78, 42));
      sendButton.setMaximumSize(new Dimension(78, 42));
      panel2.add(sendButton);
    }
    contentPane.add(panel2, BorderLayout.SOUTH);

    //======== panel3 ========
    {
      panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));

      //---- label1 ----
      label1.setText("\u0410\u043a\u0442\u0438\u0432\u043d\u044b\u0435 \u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u0438");
      label1.setLabelFor(usersList);
      label1.setHorizontalAlignment(SwingConstants.CENTER);
      label1.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
      panel3.add(label1);

      //======== scrollPane1 ========
      {
        scrollPane1.setMinimumSize(new Dimension(32, 16));
        scrollPane1.setPreferredSize(new Dimension(150, 162));

        //---- usersList ----
        usersList.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        usersList.setFocusable(false);
        scrollPane1.setViewportView(usersList);
      }
      panel3.add(scrollPane1);
    }
    contentPane.add(panel3, BorderLayout.WEST);
    setSize(615, 730);
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
  private JTextArea chatArea;
  private JTextField messageField;
  private JButton sendButton;
  private JList usersList;
  // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
