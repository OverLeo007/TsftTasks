package ru.shift.task6.view.connection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.formdev.flatlaf.extras.*;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import ru.shift.task6.presenter.ConnectionPresenter;
import ru.shift.task6.view.components.Icons;

/*
 * Created by JFormDesigner on Sat Apr 26 22:00:10 KRAT 2025
 */
@SuppressWarnings({"UnnecessaryUnicodeEscape", "DuplicatedCode"})
@Slf4j
public class ConnectWindow extends JDialog {
    private final ConnectionPresenter presenter;

    public ConnectWindow(ConnectionPresenter presenter) {
        this.presenter = presenter;
        initComponents();
    }

    private void addressOk(ActionEvent e) {
        log.info("Подключаемся к серверу {}", addressField.getText());
        statusAddressLabel.setIcon(new FlatSVGIcon("icons/pending.svg"));
        statusAddressLabel.setVisible(true);
        presenter.setAddress(addressField.getText());
    }

    private void nicknameOk(ActionEvent e) {
        log.info("Пытаемся войти в чат {}", addressField.getText());
        statusNicknameLabel.setIcon(new FlatSVGIcon("icons/pending.svg"));
        statusNicknameLabel.setVisible(true);
        presenter.setNickname(nicknameField.getText());

    }

    private void finish(ActionEvent e) {
      presenter.finish();
    }

    @SuppressWarnings("Convert2MethodRef")
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        var buttonBar = new JPanel();
        finishButton = new JButton();
        var dialogPane = new JPanel();
        var contentPanel = new JPanel();
        var panel1 = new JPanel();
        addressField = new JTextField(21);
        var label1 = new JLabel();
        errorAddressLabel = new JLabel();
        addressOkButton = new JButton();
        statusAddressLabel = new JLabel();
        var panel2 = new JPanel();
        nicknameField = new JTextField(20);
        var label2 = new JLabel();
        nicknameErrorLabel = new JLabel();
        nicknameOkButton = new JButton();
        statusNicknameLabel = new JLabel();

        //======== this ========
        setTitle("\u041f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(470, 200));
        setPreferredSize(new Dimension(470, 200));
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

          //---- finishButton ----
          finishButton.setText("Finish");
          finishButton.setEnabled(false);
          finishButton.addActionListener(e -> finish(e));
          buttonBar.add(finishButton, "cell 0 0");
        }
        contentPane.add(buttonBar, BorderLayout.SOUTH);

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

              //---- addressOkButton ----
              addressOkButton.setText("Connect");
              addressOkButton.setEnabled(false);
              addressOkButton.addActionListener(e -> addressOk(e));
              panel1.add(addressOkButton, BorderLayout.EAST);

              //---- statusAddressLabel ----
              statusAddressLabel.setVisible(false);
              panel1.add(statusAddressLabel, BorderLayout.WEST);
            }
            contentPanel.add(panel1, "cell 0 1");

            //======== panel2 ========
            {
              panel2.setLayout(new BorderLayout(2, 2));

              //---- nicknameField ----
              nicknameField.setToolTipText("\u0414\u043b\u0438\u043d\u0430 \u0438\u043c\u0435\u043d\u0438 \u043e\u0442 3 \u0434\u043e 20 \u0441\u0438\u043c\u0432\u043e\u043b\u043e\u0432");
              nicknameField.setEnabled(false);
              panel2.add(nicknameField, BorderLayout.CENTER);

              //---- label2 ----
              label2.setText("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0430\u0434\u0440\u0435\u0441 \u0441\u0435\u0440\u0432\u0435\u0440\u0430");
              label2.setLabelFor(nicknameField);
              panel2.add(label2, BorderLayout.NORTH);

              //---- nicknameErrorLabel ----
              nicknameErrorLabel.setText("text");
              nicknameErrorLabel.setForeground(new Color(0xff6666));
              nicknameErrorLabel.setFocusable(false);
              nicknameErrorLabel.setVisible(false);
              panel2.add(nicknameErrorLabel, BorderLayout.SOUTH);

              //---- nicknameOkButton ----
              nicknameOkButton.setText("Auth");
              nicknameOkButton.setEnabled(false);
              nicknameOkButton.addActionListener(e -> nicknameOk(e));
              panel2.add(nicknameOkButton, BorderLayout.EAST);

              //---- statusNicknameLabel ----
              statusNicknameLabel.setVisible(false);
              panel2.add(statusNicknameLabel, BorderLayout.WEST);
            }
            contentPanel.add(panel2, "cell 0 2");
          }
          dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(525, 225);
        setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on

        addressField.getDocument().addDocumentListener(createAddressFieldDocumentListener());
        nicknameField.getDocument().addDocumentListener(createNicknameFieldDocumentListener());
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JButton finishButton;
    private JTextField addressField;
    private JLabel errorAddressLabel;
    private JButton addressOkButton;
    private JLabel statusAddressLabel;
    private JTextField nicknameField;
    private JLabel nicknameErrorLabel;
    private JButton nicknameOkButton;
    private JLabel statusNicknameLabel;
  // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private static final Pattern PARTIAL_IP_PATTERN = Pattern.compile(
            "^\\d{0,3}(\\.\\d{0,3}){0,3}(:\\d{0,5})?$"
    );

    private DocumentListener createAddressFieldDocumentListener() {
        return new DocumentListener() {
            private void validate() {
                String text = addressField.getText();
                if (isPartialValidIPPort(text)) {
                    errorAddressLabel.setText("");
                    errorAddressLabel.setVisible(false);
                    addressOkButton.setEnabled(isFullyValidIPPort(text));
                } else {
                    errorAddressLabel.setText("Некорректный адрес");
                    errorAddressLabel.setVisible(true);
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

    private DocumentListener createNicknameFieldDocumentListener() {
        return new DocumentListener() {
            private void validate() {
                String nickname = nicknameField.getText().strip();
                if (nickname.length() < 3) {
                    return;
                }
                var isValid = nickname.length() <= 20 && !nickname.isBlank();

                nicknameOkButton.setEnabled(isValid);
                nicknameErrorLabel.setVisible(!isValid);
                nicknameErrorLabel.setText(isValid ? "" : "Длина имени от 3 до 20 символов");
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

    public void showAddressError(String message) {
        errorAddressLabel.setText(message);
        errorAddressLabel.setVisible(true);
        statusAddressLabel.setIcon(Icons.ERROR.icon());
    }

    public void showNicknameError(String message) {
        nicknameErrorLabel.setText(message);
        nicknameErrorLabel.setVisible(true);
        statusNicknameLabel.setIcon(Icons.ERROR.icon());
    }

    public void onAddressSuccess() {
        statusAddressLabel.setIcon(Icons.SUCCESS.icon());
        addressOkButton.setEnabled(false);
        nicknameField.setEnabled(true);
        nicknameField.requestFocus();
    }

    public void onNicknameSuccess() {
        statusNicknameLabel.setIcon(Icons.SUCCESS.icon());
        statusNicknameLabel.setVisible(true);
        nicknameOkButton.setEnabled(false);
        finishButton.setEnabled(true);
        finishButton.requestFocus();
    }
}
