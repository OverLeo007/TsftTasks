package ru.shift.task6.client.view.windowImpl;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import ru.shift.task6.client.view.designer.ErrorDialog;

public class ErrorWindowImpl extends ErrorDialog {

    public ErrorWindowImpl(Window owner, String message,  boolean isFatal) {
        super(owner);
        setOnClose(isFatal);
        errorText.setText(message);
        setEnabled(true);
    }

    public ErrorWindowImpl(String message, boolean isFatal) {
        super(null);
        setOnClose(isFatal);
        errorText.setText(message);
        setEnabled(true);
    }

    private void setOnClose(boolean isFatal) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                if (isFatal) {
                    System.exit(1);
                }
            }
        });

        okButton.addActionListener(e -> {
            dispose();
            if (isFatal) {
                System.exit(1);
            }
        });
    }
}
