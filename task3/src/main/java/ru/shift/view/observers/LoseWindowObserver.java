package ru.shift.view.observers;

import ru.shift.model.listeners.MV_EndGameListener;
import ru.shift.view.windows.LoseWindow;

public class LoseWindowObserver implements MV_EndGameListener {

    private final LoseWindow loseWindow;

    public LoseWindowObserver(LoseWindow loseWindow) {
        this.loseWindow = loseWindow;
    }

    @Override
    public void onGameEnd() {
        loseWindow.setVisible(true);
    }
}
