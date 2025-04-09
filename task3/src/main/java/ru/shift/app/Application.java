package ru.shift.app;


import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.FieldEventController;
import ru.shift.controller.NewGameController;
import ru.shift.model.field.CoreModel;
import ru.shift.model.timer.Timer;
import ru.shift.view.observers.LoseWindowObserver;
import ru.shift.view.observers.MainWindowObserver;
import ru.shift.view.observers.WinWindowObserver;
import ru.shift.view.windows.HighScoresWindow;
import ru.shift.view.windows.LoseWindow;
import ru.shift.view.windows.MainWindow;
import ru.shift.view.windows.SettingsWindow;
import ru.shift.view.windows.WinWindow;

@Slf4j
public class Application {

    private final MainWindow mainWindow = new MainWindow();
    private final WinWindow winWindow = new WinWindow(mainWindow);
    private final LoseWindow loseWindow = new LoseWindow(mainWindow);

    private final MainWindowObserver mainWindowObserver = new MainWindowObserver(mainWindow);

    private final SettingsWindow settingsWindow = new SettingsWindow(mainWindow);



    private final CoreModel coreModel = CoreModel.builder()
            .fieldEventListener(mainWindowObserver)
            .gameTypeListener(settingsWindow)
            .timer(new Timer(mainWindowObserver))
            .winListener(new WinWindowObserver(winWindow))
            .loseListener(new LoseWindowObserver(loseWindow))
            .build();

    private final FieldEventController fieldEventController = new FieldEventController(coreModel);

    private final NewGameController newGameController = new NewGameController(coreModel);

    private final HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);


    public static void main(String[] args) {
        var app = new Application();

        app.bindListenersToView();
        app.newGameController.onNewGame();
        app.mainWindow.setVisible(true);

    }

    private void bindListenersToView() {
        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));

        mainWindow.setFieldEventListener(fieldEventController);

        mainWindow.setNewGameListener(newGameController);

        settingsWindow.setGameTypeListener(newGameController);

        winWindow.setNewGameListener(e -> newGameController.onNewGame());
        winWindow.setExitListener(e -> mainWindow.dispose());

        loseWindow.setNewGameListener(e -> newGameController.onNewGame());
        loseWindow.setExitListener(e -> mainWindow.dispose());


    }
}
