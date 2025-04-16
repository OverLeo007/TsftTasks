package ru.shift.model.api;

public interface CellStateManipulator {

    void openCell(int x, int y);

    void toggleFlag(int x, int y);

    void openCellsAround(int x, int y);
}
