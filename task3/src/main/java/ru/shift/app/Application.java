package ru.shift.app;


import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.CellEventController;
import ru.shift.model.cellEvent.CellEventModel;
import ru.shift.view.windows.HighScoresWindow;
import ru.shift.view.windows.MainWindow;
import ru.shift.view.windows.SettingsWindow;

@Slf4j
public class Application {

    private final MainWindow mainWindow = new MainWindow();
    private final SettingsWindow settingsWindow = new SettingsWindow(mainWindow);
    private final HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
    private final CellEventModel cellEventModel = new CellEventModel(mainWindow);
    private final CellEventController cellEventController = new CellEventController(cellEventModel);



    public static void main(String[] args) {
        var app = new Application();

        app.mainWindow.setNewGameMenuAction(e -> { /* TODO */ });
        app.mainWindow.setSettingsMenuAction(e -> app.settingsWindow.setVisible(true));
        app.mainWindow.setHighScoresMenuAction(e -> app.highScoresWindow.setVisible(true));
        app.mainWindow.setExitMenuAction(e -> app.mainWindow.dispose());
        app.mainWindow.setCellListener(app.cellEventController);

        app.mainWindow.createGameField(10, 10);
        app.mainWindow.setVisible(true);

        // TODO: There is a sample code below, remove it after try

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
}
