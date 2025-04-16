package ru.shift.view.observers;

import ru.shift.model.events.GameStateChangeEvent;
import ru.shift.model.listeners.ChangeGameStateListener;
import ru.shift.model.state.GameState;
import ru.shift.view.windows.WinWindow;

public class WinWindowObserver implements ChangeGameStateListener {

    private final WinWindow winWindow;

    public WinWindowObserver(WinWindow winWindow) {
        this.winWindow = winWindow;
    }

    @Override
    public void onChangeState(GameStateChangeEvent event) {
        if (event.gameState() == GameState.WIN) {
            winWindow.setVisible(true);
        }
    }
}
