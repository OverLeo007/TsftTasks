package ru.shift.model.field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Cell {
    private final int x;
    private final int y;

    private boolean isOpen = false;
    private boolean isFlagged = false;
    private boolean isBomb = false;

    private CellState state;

    public boolean isClosed() {
        return !isOpen;
    }

}
