package ru.shift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.model.api.CellStateManipulator;
import ru.shift.view.ButtonType;

@Slf4j
@RequiredArgsConstructor
public class FieldEventController {
    private final CellStateManipulator cellStateManipulator;

    public void onMouseClick(int x, int y, ButtonType buttonType) {
        switch (buttonType) {
            case LEFT_BUTTON -> cellStateManipulator.openCell(x, y);
            case RIGHT_BUTTON -> cellStateManipulator.toggleFlag(x, y);
            case MIDDLE_BUTTON -> cellStateManipulator.openCellsAround(x, y);
        }
    }
}
