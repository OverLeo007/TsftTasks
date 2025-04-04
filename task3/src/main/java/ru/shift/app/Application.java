package ru.shift.app;


import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.FieldEventController;
import ru.shift.controller.SettingsController;
import ru.shift.model.field.FieldModel;
import ru.shift.view.GameType;
import ru.shift.view.observers.MainWindowObserver;
import ru.shift.view.windows.HighScoresWindow;
import ru.shift.view.windows.SettingsWindow;

@Slf4j
public class Application {

    private final MainWindowObserver mainWindow = new MainWindowObserver();
    private final SettingsWindow settingsWindow = new SettingsWindow(mainWindow);

    private final FieldModel fieldModel = FieldModel.builder()
            .fieldEventListener(mainWindow)
            .gameTypeListener(settingsWindow)
            .build();
    private final FieldEventController fieldEventController = new FieldEventController(fieldModel);

    private final SettingsController settingsController = new SettingsController(fieldModel);

    private final HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);

//    private final WinWindow winWindow = new WinWindow(mainWindow);
//    private final LoseWindow loseWindow = new LoseWindow(mainWindow);


    public static void main(String[] args) {
        var app = new Application();

        app.bindListenersToView();

        app.mainWindow.setSettingsMenuAction(e -> app.settingsWindow.setVisible(true));
        app.mainWindow.setHighScoresMenuAction(e -> app.highScoresWindow.setVisible(true));

        app.mainWindow.createGameField(9, 9);
        app.fieldModel.onGameTypeSelected(GameType.PREVIOUS);
        app.mainWindow.setVisible(true);

//        mainWindow.setTimerValue(145);
//        mainWindow.setBombsCount(45);
//        mainWindow.setCellImage(0, 0, GameImage.EMPTY);
//        mainWindow.setCellImage(0, 1, GameImage.CLOSED);
//        mainWindow.setCellImage(0, 2, GameImage.MARKED);
//        mainWindow.setCellImage(0, 3, GameImage.BOMB);
//        mainWindow.setCellImage(1, 0, GameImage.NUM_1);
//        mainWindow.setCellImage(1, 1, GameImage.NUM_2);
//        mainWindow.setCellImage(1, 2, GameImage.NUM_3);
//        mainWindow.setCellImage(1, 3, GameImage.NUM_4);
//        mainWindow.setCellImage(1, 4, GameImage.NUM_5);
//        mainWindow.setCellImage(1, 5, GameImage.NUM_6);
//        mainWindow.setCellImage(1, 6, GameImage.NUM_7);
//        mainWindow.setCellImage(1, 7, GameImage.NUM_8);
    }

    private void bindListenersToView() {
        mainWindow.setFieldEventListener(fieldEventController);
        mainWindow.setGameTypeListener(settingsController);
        settingsWindow.setGameTypeListener(settingsController);
    }
}
