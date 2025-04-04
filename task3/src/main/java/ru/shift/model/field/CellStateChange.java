package ru.shift.model.field;

public record CellStateChange(
        int x,
        int y,
        CellState cellState
) {}
