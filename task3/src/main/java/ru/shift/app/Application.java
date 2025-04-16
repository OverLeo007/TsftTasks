package ru.shift.app;

import ru.shift.controller.GameController;
import ru.shift.external.External;
import ru.shift.model.CoreModel;
import ru.shift.view.GameView;

public class Application {
    public static void main(String[] args) {
        var model = new CoreModel();
        var view = new GameView();
        var external = new External(model);
        var controller = new GameController(model, view, external);

        external.getScoreRepository().onListenersSubscribed();

        boolean isHack = System.getProperty("hack") != null;
        controller.startNewGame(isHack);
    }
}
