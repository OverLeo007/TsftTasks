package ru.shift.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.shift.model.cellEvent.CellState;

@Getter
@AllArgsConstructor
public class Cell {
    private int x;
    private int y;
    @Setter
    private CellState state;
}
