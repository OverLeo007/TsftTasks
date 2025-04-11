package ru.shift.view.windows;


import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.shift.view.ButtonType;
import ru.shift.view.GameImage;
import ru.shift.view.listeners.VC_FieldEventListener;
import ru.shift.view.listeners.VC_NewGameListener;

@Slf4j
public class MainWindow extends JFrame  {

    @Setter
    private VC_FieldEventListener fieldEventListener;
    @Setter
    private VC_NewGameListener newGameListener;

    private final Container contentPane;
    private final GridBagLayout mainLayout;

    private JMenuItem highScoresMenu;
    private JMenuItem settingsMenu;

    private final JMenu gameMenu = new JMenu("Game");


    protected JButton[][] cellButtons;
    private JLabel timerLabel;
    private JLabel bombsCounterLabel;

    public MainWindow() {
        super("Minesweeper");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        createMenu();

        contentPane = getContentPane();
        mainLayout = new GridBagLayout();
        contentPane.setLayout(mainLayout);

        contentPane.setBackground(new Color(144, 158, 184));
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenuItem newGameMenu;
        gameMenu.add(newGameMenu = new JMenuItem("New Game"));

        gameMenu.addSeparator();
        gameMenu.add(highScoresMenu = new JMenuItem("High Scores"));
        gameMenu.add(settingsMenu = new JMenuItem("Settings"));
        gameMenu.addSeparator();

        JMenuItem exitMenu;
        gameMenu.add(exitMenu = new JMenuItem("Exit"));

        menuBar.add(gameMenu);

        setJMenuBar(menuBar);

        newGameMenu.addActionListener(e -> newGameListener.onNewGame());
        exitMenu.addActionListener(e -> dispose());

    }


    public void setHighScoresMenuAction(ActionListener listener) {
        highScoresMenu.addActionListener(listener);
    }

    public void setSettingsMenuAction(ActionListener listener) {
        settingsMenu.addActionListener(listener);
    }

    public void setBombsCount(int bombsCount) {
        bombsCounterLabel.setText(String.valueOf(bombsCount));
    }

    public void setCellImage(int x, int y, GameImage image) {
        cellButtons[y][x].setIcon(image.getImageIcon());
    }

    public void setTimerValue(int value) {
        timerLabel.setText(String.valueOf(value));
    }

    public void createGameField(int rowsCount, int colsCount) {
        contentPane.removeAll();
        setPreferredSize(new Dimension(20 * colsCount + 70, 20 * rowsCount + 110));

        addButtonsPanel(createButtonsPanel(rowsCount, colsCount));
        addTimerImage();
        addTimerLabel(timerLabel = new JLabel("0"));
        addBombCounter(bombsCounterLabel = new JLabel("0"));
        addBombCounterImage();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createButtonsPanel(int numberOfRows, int numberOfCols) {
        cellButtons = new JButton[numberOfRows][numberOfCols];
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(20 * numberOfCols, 20 * numberOfRows));
        buttonsPanel.setLayout(new GridLayout(numberOfRows, numberOfCols, 0, 0));

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                final int x = col;
                final int y = row;

                cellButtons[y][x] = new JButton(GameImage.CLOSED.getImageIcon());
                cellButtons[y][x].addMouseListener(createMouseAdapter(x, y));
                buttonsPanel.add(cellButtons[y][x]);
            }
        }

        return buttonsPanel;
    }

    private void addButtonsPanel(JPanel buttonsPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 20, 5, 20);
        mainLayout.setConstraints(buttonsPanel, gbc);
        contentPane.add(buttonsPanel);
    }

    private void addTimerImage() {
        JLabel label = new JLabel(GameImage.TIMER.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 20, 0, 0);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

    private void addTimerLabel(JLabel timerLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 5, 0, 0);
        mainLayout.setConstraints(timerLabel, gbc);
        contentPane.add(timerLabel);
    }

    private void addBombCounter(JLabel bombsCounterLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 0.7;
        mainLayout.setConstraints(bombsCounterLabel, gbc);
        contentPane.add(bombsCounterLabel);
    }

    private void addBombCounterImage() {
        JLabel label = new JLabel(GameImage.BOMB_ICON.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 3;
        gbc.insets = new Insets(0, 5, 0, 20);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

    private MouseAdapter createMouseAdapter(int x, int y) {
        return new MouseAdapter() {

            private boolean isButton1Pressed = false;
            private boolean isButton3Pressed = false;

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isButton1Pressed = true;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    isButton3Pressed = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (fieldEventListener == null) {
                    return;
                }
                if (isButton1Pressed && isButton3Pressed) {
                    fieldEventListener.onMouseClick(x, y, ButtonType.MIDDLE_BUTTON);
                }  else {
                    switch (e.getButton()) {
                        case MouseEvent.BUTTON1 ->
                            fieldEventListener.onMouseClick(x, y, ButtonType.LEFT_BUTTON);
                        case MouseEvent.BUTTON2 ->
                            fieldEventListener.onMouseClick(x, y, ButtonType.MIDDLE_BUTTON);
                        case MouseEvent.BUTTON3 ->
                            fieldEventListener.onMouseClick(x, y, ButtonType.RIGHT_BUTTON);
                        default -> {
                            // Other mouse buttons are ignored
                        }
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isButton1Pressed = false;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    isButton3Pressed = false;
                }
            }
        };
    }

    public void enableHackMode(boolean isHack) {
        if (isHack) {
            JCheckBoxMenuItem showMinesOption = new JCheckBoxMenuItem("Показывать мины");
            showMinesOption.addActionListener(e -> {
                boolean selected = showMinesOption.isSelected();
                if (fieldEventListener != null) {
                    fieldEventListener.onHackStateChanged(selected);
                }
            });
            gameMenu.add(showMinesOption, 1);
        }
    }
}
