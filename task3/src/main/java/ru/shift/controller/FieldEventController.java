package ru.shift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.listeners.CM_FieldEventListener;
import ru.shift.controller.listeners.CM_onToggleBombsVisibilityListener;
import ru.shift.view.ButtonType;
import ru.shift.view.listeners.VC_FieldEventListener;

@Slf4j
@RequiredArgsConstructor
public class FieldEventController implements VC_FieldEventListener {
    private final CM_FieldEventListener cellEventListener;
    private final CM_onToggleBombsVisibilityListener toggleBombsVisibilityListener;

    @Override
    public void onMouseClick(int x, int y, ButtonType buttonType) {
        switch (buttonType) {
            case LEFT_BUTTON -> cellEventListener.onOpenCell(x, y);
            case RIGHT_BUTTON -> cellEventListener.onToggleFlag(x, y);
            case MIDDLE_BUTTON -> cellEventListener.onOpenCellsAround(x, y);
        }
    }

    @Override
    public void onHackStateChanged(boolean isHack) {
        toggleBombsVisibilityListener.onToggleBombsVisibility(isHack);
    }
}
