package ru.shift.task6.view.windows;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.*;

/*
 * Created by JFormDesigner on Sat Apr 26 22:00:10 KRAT 2025
 */
@SuppressWarnings({"UnnecessaryUnicodeEscape", "DuplicatedCode"})
@Slf4j
public class ConnectWindow extends JDialog {

    public ConnectWindow() {
        initComponents();
    }

    private void ok(ActionEvent e) {
        log.info("Connecting to server at {}", addressField.getText());
        this.dispose();
    }

    @SuppressWarnings("Convert2MethodRef")
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        var dialogPane = new JPanel();
        var contentPanel = new JPanel();
        var label1 = new JLabel();
        addressField = new JTextField(21);
        errorLabel = new JLabel();
        var buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setTitle("\u041f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
          dialogPane.setLayout(new BorderLayout());

          //======== contentPanel ========
          {
            contentPanel.setLayout(new MigLayout(
              "fillx,insets dialog,hidemode 3,alignx center",
              // columns
              "[fill]",
              // rows
              "[]" +
              "[]" +
              "[]"));

            //---- label1 ----
            label1.setText("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0430\u0434\u0440\u0435\u0441 \u0441\u0435\u0440\u0432\u0435\u0440\u0430");
            contentPanel.add(label1, "cell 0 0,growx");

            //---- addressField ----
            addressField.setToolTipText("0.0.0.0:0 - 255.255.255.255:65535");
            contentPanel.add(addressField, "cell 0 1");

            //---- errorLabel ----
            errorLabel.setText("text");
            errorLabel.setForeground(new Color(0xff6666));
            errorLabel.setFocusable(false);
            errorLabel.setVisible(false);
            contentPanel.add(errorLabel, "cell 0 2");
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

        addressField.getDocument().addDocumentListener(createAddressFieldDocumentListener());
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JTextField addressField;
    private JLabel errorLabel;
    private JButton okButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private static final Pattern PARTIAL_IP_PATTERN = Pattern.compile(
            "^\\d{0,3}(\\.\\d{0,3}){0,3}(:\\d{0,5})?$"
    );

    private DocumentListener createAddressFieldDocumentListener() {
        return new DocumentListener() {
            private void validate() {
                String text = addressField.getText();
                if (isPartialValidIPPort(text)) {
                    errorLabel.setText("");
                    errorLabel.setVisible(false);
                    okButton.setEnabled(isFullyValidIPPort(text));
                } else {
                    errorLabel.setText("Некорректный адрес");
                    errorLabel.setVisible(true);
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                validate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validate();
            }
        };
    }

    private static boolean isPartialValidIPPort(String text) {
        if (text.isEmpty()) {
            return true;
        }
        if (text.contains("..") || !PARTIAL_IP_PATTERN.matcher(text).matches()) {
            return false;
        }

        String[] parts = text.split(":");
        String ipPart = parts[0];

        String[] numbers = ipPart.split("\\.");
        for (String num : numbers) {
            if (!num.isEmpty() && !validateNum(num, 0, 255)) {
                return false;
            }
        }

        if (parts.length > 1 && !parts[1].isEmpty()) {
            return validateNum(parts[1], 0, 65535);
        }

        return true;
    }

    private static boolean isFullyValidIPPort(String text) {
        if (text.isEmpty()) {
            return false;
        }
        if (!text.contains(":")) {
            return false;
        }

        String[] parts = text.split(":");
        if (parts.length != 2) {
            return false;
        }

        String ip = parts[0];
        String portStr = parts[1];

        String[] octets = ip.split("\\.");
        if (octets.length != 4) {
            return false;
        }

        for (String octet : octets) {
            if (octet.isEmpty()) {
                return false;
            }
            try {
                int val = Integer.parseInt(octet);
                if (val < 0 || val > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        try {
            int port = Integer.parseInt(portStr);
            if (port < 0 || port > 65535) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    private static boolean validateNum(String num, int min, int max) {
        try {
            int val = Integer.parseInt(num);
            return val >= min && val <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
