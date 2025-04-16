package ru.shift.view.windows;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.GameStartController;
import ru.shift.model.events.NewGameDifficultyEvent;
import ru.shift.app.GameDifficulty;
import ru.shift.model.listeners.NewGameDifficultyListener;

@Slf4j
public class SettingsWindow extends JDialog implements NewGameDifficultyListener {

    private final Map<GameDifficulty, JRadioButton> radioButtonsMap = new HashMap<>(3);
    private final ButtonGroup radioGroup = new ButtonGroup();

    @Setter
    private GameStartController gameStartController;
    private GameDifficulty gameDifficulty;

    public SettingsWindow(JFrame owner) {
        super(owner, "Settings", true);

        GridBagLayout layout = new GridBagLayout();
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        int gridY = 0;
        contentPane.add(
                createRadioButton("Novice (10 mines, 9х9)", GameDifficulty.NOVICE, layout, gridY++));
        contentPane.add(
                createRadioButton("Medium (40 mines, 16х16)", GameDifficulty.MEDIUM, layout, gridY++));
        contentPane.add(
                createRadioButton("Expert (99 mines, 16х30)", GameDifficulty.EXPERT, layout, gridY));

        contentPane.add(createOkButton(layout));
        contentPane.add(createCloseButton(layout));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(300, 180));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        setGameType(GameDifficulty.NOVICE);
    }

    public void setGameType(GameDifficulty gameDifficulty) {
        JRadioButton radioButton = radioButtonsMap.get(gameDifficulty);

        if (radioButton == null) {
            throw new UnsupportedOperationException("Unknown game type: " + gameDifficulty);
        }

        this.gameDifficulty = gameDifficulty;
        radioGroup.setSelected(radioButton.getModel(), true);
    }

    private JRadioButton createRadioButton(String radioButtonText, GameDifficulty gameDifficulty,
            GridBagLayout layout, int gridY) {
        JRadioButton radioButton = new JRadioButton(radioButtonText);
        radioButton.addActionListener(e -> this.gameDifficulty = gameDifficulty);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 20, 0, 0);
        layout.setConstraints(radioButton, gbc);

        radioButtonsMap.put(gameDifficulty, radioButton);
        radioGroup.add(radioButton);

        return radioButton;
    }

    private JButton createOkButton(GridBagLayout layout) {
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(80, 25));
        okButton.addActionListener(e -> {
            dispose();

            if (gameStartController != null) {
                gameStartController.startNewGameWithDifficulty(gameDifficulty);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(15, 0, 0, 0);
        layout.setConstraints(okButton, gbc);

        return okButton;
    }

    private JButton createCloseButton(GridBagLayout layout) {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(80, 25));
        cancelButton.addActionListener(e -> dispose());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(15, 5, 0, 0);
        layout.setConstraints(cancelButton, gbc);

        return cancelButton;
    }


    @Override
    public void onNewGameDifficulty(NewGameDifficultyEvent event) {
        log.debug("onNewGameDifficulty: {}", event.gameDifficulty());
        setGameType(event.gameDifficulty());
    }
}
