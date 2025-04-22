package ru.shift.controller;

import lombok.Getter;
import ru.shift.external.External;
import ru.shift.model.CoreModel;
import ru.shift.view.GameView;

@Getter
public class GameController {

    private final GameStartController gameStartController;
    private final GameView gameView;

    public GameController(CoreModel coreModel, GameView gameView, External external) {

        FieldEventController fieldEventController = new FieldEventController(coreModel);
        this.gameStartController = new GameStartController(coreModel);
        this.gameView = gameView;
        ScoreRecordController scoreRecordController = new ScoreRecordController(
                external.getScoreRepository());

        gameView.getMainWindow().setSettingsMenuAction(e -> gameView.getSettingsWindow().setVisible(true));
        gameView.getMainWindow().setHighScoresMenuAction(e -> gameView.getHighScoresWindow().setVisible(true));

        gameView.getMainWindow().setFieldEventController(fieldEventController);

        gameView.getMainWindow().setGameStartController(gameStartController);

        gameView.getSettingsWindow().setGameStartController(gameStartController);

        gameView.getWinWindow().setNewGameListener(e -> gameStartController.startNewGame());

        gameView.getLoseWindow().setNewGameListener(e -> gameStartController.startNewGame());
        gameView.getRecordWindowObserver().setScoreRecordListener(scoreRecordController);
    }

    public void startNewGame(boolean isHack) {
        gameStartController.startNewGame();
        gameView.getMainWindow().setVisible(true);
        gameView.getMainWindow().enableHackMode(isHack);
    }
}
