package ru.shift.view.observers;

import ru.shift.model.listeners.MV_EndGameListener;
import ru.shift.view.windows.WinWindow;

public class WinWindowObserver implements MV_EndGameListener {

    private final WinWindow winWindow;

    public WinWindowObserver(WinWindow winWindow) {
        this.winWindow = winWindow;
    }

    @Override
    public void onGameEnd() {
        winWindow.setVisible(true);
    }
}
