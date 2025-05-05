package ru.shift.task6.client.view.windowImpl;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;
import javax.swing.event.DocumentListener;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.client.view.misc.Icons;
import ru.shift.task6.client.view.designer.NicknameWindow;

@Slf4j
public class NicknameWindowImpl extends NicknameWindow {

    {
        nicknameField.getDocument().addDocumentListener(createFieldDocumentListener());
    }

    public void setOkButtonListener(Consumer<String> onNicknameEntered) {
        ActionListener okAction = e -> {
            log.info("Пытаемся войти в чат ");
            statusNicknameLabel.setIcon(new FlatSVGIcon(Icons.PENDING.icon()));
            statusNicknameLabel.setVisible(true);
            onNicknameEntered.accept(nicknameField.getText());
        };

        okButton.addActionListener(okAction);
        nicknameField.addActionListener(okAction);
    }

    private DocumentListener createFieldDocumentListener() {
        return new DocumentListener() {
            private void validate() {
                String nickname = nicknameField.getText().strip();
                if (nickname.length() < 3) {
                    return;
                }
                var isValid = nickname.length() <= 20 && !nickname.isBlank();
                okButton.setEnabled(isValid);
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

    public void showNicknameError(String message) {
        nicknameErrorLabel.setText(message);
        nicknameErrorLabel.setVisible(true);
        statusNicknameLabel.setIcon(new FlatSVGIcon(Icons.ERROR.icon()));
    }

    public void onNicknameSuccess() {
        statusNicknameLabel.setIcon(new FlatSVGIcon(Icons.SUCCESS.icon()));
        statusNicknameLabel.setVisible(true);
    }

    public void setOnCloseAction(Runnable action) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                action.run();
                dispose();
            }
        });
    }
}
