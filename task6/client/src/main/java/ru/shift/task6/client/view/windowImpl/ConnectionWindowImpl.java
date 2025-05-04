package ru.shift.task6.client.view.windowImpl;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.client.view.designer.ConnectionWindow;
import ru.shift.task6.client.view.misc.Icons;

@Slf4j
public class ConnectionWindowImpl extends ConnectionWindow {

    private static final Pattern PARTIAL_IP_PATTERN = Pattern.compile(
            "^\\d{0,3}(\\.\\d{0,3}){0,3}(:\\d{0,5})?$"
    );

    {
        addressField.getDocument().addDocumentListener(createAddressFieldDocumentListener());
    }

    public ConnectionWindowImpl() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setOkButtonListener(Consumer<String> onAddressEntered) {
        okButton.addActionListener(e -> {
            log.info("Подключаемся к серверу {}", addressField.getText());
            errorAddressLabel.setText("");
            statusAddressLabel.setVisible(true);
            statusAddressLabel.setIcon(new FlatSVGIcon(Icons.SUCCESS.icon()));
            onAddressEntered.accept(addressField.getText());
        });
    }

    private DocumentListener createAddressFieldDocumentListener() {
        return new DocumentListener() {
            private void validate() {
                String text = addressField.getText();
                if (isPartialValidIPPort(text)) {
                    errorAddressLabel.setText("");
                    errorAddressLabel.setVisible(false);
                    okButton.setEnabled(isFullyValidIPPort(text));
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

    public void showAddressError(String message) {
        errorAddressLabel.setText(message);
        errorAddressLabel.setVisible(true);
        statusAddressLabel.setIcon(new FlatSVGIcon(Icons.ERROR.icon()));
    }

    public void onAddressSuccess() {
        statusAddressLabel.setIcon(new FlatSVGIcon(Icons.SUCCESS.icon()));
    }

    public void setOnCloseAction(Runnable action) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
