package ru.shift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.listeners.CM_CellEventListener;
import ru.shift.view.ButtonType;
import ru.shift.view.listeners.VC_CellEventListener;

@Slf4j
@RequiredArgsConstructor
public class CellEventController implements VC_CellEventListener {
    private final CM_CellEventListener cellEventListener;

    @Override
    public void onMouseClick(int x, int y, ButtonType buttonType) {
        switch (buttonType) {
            case LEFT_BUTTON -> cellEventListener.openCell(x, y);
            case RIGHT_BUTTON -> cellEventListener.toggleFlag(x, y);
            case MIDDLE_BUTTON -> cellEventListener.openCellsAround(x, y);
        }
    }
}
