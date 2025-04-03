package ru.shift.model.cellEvent;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.listeners.CM_CellEventListener;
import ru.shift.model.Cell;
import ru.shift.model.cellEvent.listeners.MV_CellEventListener;

@Slf4j
@RequiredArgsConstructor
public class CellEventModel implements CM_CellEventListener {

    private final MV_CellEventListener cellEventListener;

    private Cell[][] field;
    private final Random random = new Random(42);


    @Override
    public void openCell(int x, int y) {
      log.debug("Cell opened at ({}, {})", x, y);
      cellEventListener.setCellState(x, y, CellState.EMPTY);
    }

    @Override
    public void toggleFlag(int x, int y) {
        log.debug("Flag toggled at ({}, {})", x, y);



        cellEventListener.setCellState(x, y, CellState.FLAG);
    }

    @Override
    public void openCellsAround(int x, int y) {
        log.debug("Cells opened around ({}, {})", x, y);
    }
}
