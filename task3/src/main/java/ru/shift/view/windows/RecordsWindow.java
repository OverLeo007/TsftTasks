package ru.shift.view.windows;


import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ru.shift.view.listeners.VC_ScoreRecordListener;

public class RecordsWindow extends JDialog {
    private String playerName;
    private JTextField nameField;

    private final VC_ScoreRecordListener scoreRecordListener;

    public RecordsWindow(
            JFrame owner,
            VC_ScoreRecordListener scoreRecordListener,
            String previousName,
            long previousTime,
            long currentTime
    ) {
        super(owner, "Game Records", true);
        this.scoreRecordListener = scoreRecordListener;
        initializeUI();

        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel previousRecordLabel = new JLabel("Previous: " + previousName + " - " + previousTime + "s");
        panel.add(previousRecordLabel);

        JLabel currentTimeLabel = new JLabel("Your time: " + currentTime + "s");
        panel.add(currentTimeLabel);

        addCommonComponents(panel);

        setVisible(true);
    }

    public RecordsWindow(
            JFrame owner,
            VC_ScoreRecordListener scoreRecordListener,
            long currentTime
    ) {
        super(owner, "Game Records", true);
        this.scoreRecordListener = scoreRecordListener;
        initializeUI();

        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel currentTimeLabel = new JLabel("Your time: " + currentTime + "s (New Record!)");
        panel.add(currentTimeLabel);

        addCommonComponents(panel);
        setVisible(true);
    }

    private void initializeUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void addCommonComponents(JPanel panel) {
        // Подпись для поля ввода
        JLabel enterNameLabel = new JLabel("Enter your name:");
        panel.add(enterNameLabel);

        // Поле для ввода имени
        nameField = new JTextField();
        panel.add(nameField);

        // Кнопка OK
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            playerName = nameField.getText().trim();
            if (scoreRecordListener != null) {
                scoreRecordListener.onRecordNameEntered(playerName);
            }
            dispose();
        });
        panel.add(okButton);

        add(panel);
    }
}
