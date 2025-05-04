/*
 * Created by JFormDesigner on Sun Apr 27 01:49:14 KRAT 2025
 */

package ru.shift.task6.client.view.designer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import com.formdev.flatlaf.extras.*;
import ru.shift.commons.models.payload.UserInfo;
import ru.shift.task6.client.view.misc.Icons;
import ru.shift.task6.client.view.misc.UserCellRenderer;

/**
 * @author Lev Sokolov
 */
public class ChatWindow extends JFrame {

    public ChatWindow() {
        initComponents();
    }

    @SuppressWarnings("UnnecessaryUnicodeEscape")
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        var panel4 = new JPanel();
        var panel3 = new JPanel();
        var scrollPane1 = new JScrollPane();
        usersList = new JList<>();
        var panel1 = new JPanel();
        chatErrorLabel = new JLabel();
        var scrollPane3 = new JScrollPane();
        chatArea = new JTextPane();
        var panel2 = new JPanel();
        messageField = new JTextField();
        sendButton = new JButton();

        //======== this ========
        setTitle("\u0427\u0430\u0442");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        setMinimumSize(new Dimension(648, 312));
        setPreferredSize(new Dimension(648, 312));
        var contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        //======== panel4 ========
        {
          panel4.setBorder(new EmptyBorder(5, 5, 5, 5));
          panel4.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
          panel4.setLayout(new BorderLayout(2, 2));

          //======== panel3 ========
          {
            panel3.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            panel3.setBorder(new TitledBorder(new LineBorder(new Color(0x282a36), 1, true), "\u0410\u043a\u0442\u0438\u0432\u043d\u044b\u0435 \u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u0438", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
              new Font("JetBrains Mono", Font.PLAIN, 12), Color.gray));
            panel3.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
            panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));

            //======== scrollPane1 ========
            {
              scrollPane1.setMinimumSize(new Dimension(230, 16));
              scrollPane1.setPreferredSize(new Dimension(230, 162));
              scrollPane1.setBorder(new EmptyBorder(0, 5, 0, 5));
              scrollPane1.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));

              //---- usersList ----
              usersList.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
              usersList.setFocusable(false);
              usersList.setCellRenderer(new UserCellRenderer());
              usersList.setSelectionModel(new DefaultListSelectionModel() {
                  @Override
                  public void setSelectionInterval(int index0, int index1) {
                  }
              });
              usersList.setBackground(new Color(0x3c3f41));
              scrollPane1.setViewportView(usersList);
            }
            panel3.add(scrollPane1);
          }
          panel4.add(panel3, BorderLayout.LINE_START);

          //======== panel1 ========
          {
            panel1.setBorder(new TitledBorder(new LineBorder(new Color(0x282a36), 1, true), "\u0427\u0430\u0442", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, Color.gray));
            panel1.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
            panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

            //---- chatErrorLabel ----
            chatErrorLabel.setForeground(new Color(0xffff66));
            chatErrorLabel.setMinimumSize(new Dimension(3, 17));
            chatErrorLabel.setMaximumSize(new Dimension(3, 17));
            chatErrorLabel.setPreferredSize(new Dimension(3, 17));
            chatErrorLabel.setVisible(false);
            chatErrorLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
            panel1.add(chatErrorLabel);

            //======== scrollPane3 ========
            {

              //---- chatArea ----
              chatArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
              chatArea.setEditable(false);
              chatArea.setFocusable(false);
              StyledDocument doc = chatArea.getStyledDocument();
              Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

              Style regular = doc.addStyle("regular", defaultStyle);

              Style center = doc.addStyle("center", regular);
              StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
              StyleConstants.setForeground(center, new Color(0x80c080));

              Style time = doc.addStyle("time", regular);
              StyleConstants.setForeground(time, new Color(0x808080));
              scrollPane3.setViewportView(chatArea);
            }
            panel1.add(scrollPane3);

            //======== panel2 ========
            {
              panel2.setPreferredSize(new Dimension(91, 35));
              panel2.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
              panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));

              //---- messageField ----
              messageField.setPreferredSize(new Dimension(49, 31));
              messageField.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
              messageField.setMaximumSize(new Dimension(2147483647, 31));
              panel2.add(messageField);

              //---- sendButton ----
              sendButton.setPreferredSize(new Dimension(42, 31));
              sendButton.setMinimumSize(new Dimension(78, 42));
              sendButton.setMaximumSize(new Dimension(78, 42));
              sendButton.setIcon(new FlatSVGIcon(Icons.SEND.icon()));
              panel2.add(sendButton);
            }
            panel1.add(panel2);
          }
          panel4.add(panel1, BorderLayout.CENTER);
        }
        contentPane.add(panel4);
        setSize(875, 730);
        setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    protected JList<UserInfo> usersList;
    protected JLabel chatErrorLabel;
    protected JTextPane chatArea;
    protected JTextField messageField;
    protected JButton sendButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

}
