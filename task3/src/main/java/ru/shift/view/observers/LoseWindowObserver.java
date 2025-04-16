package ru.shift.view.observers;

import ru.shift.model.events.GameStateChangeEvent;
import ru.shift.model.listeners.ChangeGameStateListener;
import ru.shift.model.state.GameState;
import ru.shift.view.windows.LoseWindow;

public class LoseWindowObserver implements ChangeGameStateListener {

    private final LoseWindow loseWindow;

    public LoseWindowObserver(LoseWindow loseWindow) {
        this.loseWindow = loseWindow;
    }

    @Override
    public void onChangeState(GameStateChangeEvent event) {
        if (event.gameState() == GameState.LOSE) {
            loseWindow.setVisible(true);
        }
    }
}
