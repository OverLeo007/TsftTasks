package ru.shift.model.field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.listeners.CM_FieldEventListener;
import ru.shift.controller.listeners.CM_GameTypeListener;
import ru.shift.model.GameType;
import ru.shift.model.listeners.MV_FiledEventListener;
import ru.shift.model.listeners.MV_GameTypeListener;

@Slf4j
public class FieldModel implements CM_FieldEventListener, CM_GameTypeListener {

    private final MV_FiledEventListener fieldEventListener;
    private final MV_GameTypeListener gameTypeListener;

    @Builder
    public FieldModel(MV_FiledEventListener fieldEventListener,
            MV_GameTypeListener gameTypeListener) {
        this.fieldEventListener = fieldEventListener;
        this.gameTypeListener = gameTypeListener;
    }

    private GameType gameType = GameType.NOVICE;
    private GameState gameState = GameState.STOP;
    private final Random random = new Random(42);


    private Cell[][] field;

    private int flagsCount = 0;

    private int closedCellsCount = 0;

    @Override
    public void onOpenCell(int x, int y) {
        log.debug("Try to open cell at ({}, {})", x, y);

        switch (gameState) {
            case STOP -> startGameFrom(x, y);
            case PLAY -> openCell(x, y);

        }

    }

    @Override
    public void onToggleFlag(int x, int y) {
        log.debug("Try to toggle flag at ({}, {})", x, y);
        if (gameState == GameState.LOSE || gameState == GameState.WIN) {
            return;
        }

        Cell curCell = field[y][x];

        if (curCell.isOpen()) {
            return;
        }

        if (curCell.isFlagged()) {
            curCell.setFlagged(false);
            flagsCount--;
        } else {
            curCell.setFlagged(true);
            flagsCount++;
        }
        var newState = curCell.isFlagged() ? CellState.FLAG : CellState.CLOSED;
        curCell.setState(newState);
        fieldEventListener.onChangeCellState(x, y, newState);
        sendUpdBombsCount();
    }

    @Override
    public void onOpenCellsAround(int x, int y) {
        log.debug("Try to open cells around ({}, {})", x, y);
        if (gameState != GameState.PLAY) {
            return;
        }
        var cell = field[y][x];
        var cellsAround = findAllCellsAround(cell);
        var bombsAroundCount = countFromCells(cellsAround, Cell::isBomb);

        if (cell.isClosed() || bombsAroundCount == 0) {
            return;
        }

        var flagsAroundCount = countFromCells(cellsAround, Cell::isFlagged);
        var closedCellsAroundCount = countFromCells(cellsAround, Cell::isClosed);

        if (flagsAroundCount != bombsAroundCount || closedCellsAroundCount == flagsAroundCount) {
            return;
        }

        for (var nextCell : findFromCells(cellsAround, c -> c.isClosed() && !c.isFlagged())) {
            openCell(nextCell);
        }

    }

    private void startGameFrom(int x, int y) {
        log.debug("Game started from ({}, {})", x, y);
        setupBombs(x, y);
        gameState = GameState.PLAY;
        openCell(x, y);
    }

    @Override
    public void onGameTypeSelected(GameType gameType) {
        this.gameType = (gameType == GameType.PREVIOUS) ? this.gameType : gameType;
        log.debug("Game type selected: {}", this.gameType);
        gameTypeListener.onGameTypeSelected(this.gameType);
        fieldEventListener.onFieldSetup(this.gameType);
        setupGameData();
    }

    private void setupGameData() {
        log.debug("Game data setup");
        setupField();
        closedCellsCount = gameType.rows * gameType.cols - gameType.bombsN;
        flagsCount = 0;
        gameState = GameState.STOP;

    }

    private void setupField() {
        log.debug("Setup new field with size {}x{}", gameType.rows, gameType.cols);
        int rows = gameType.rows;
        int cols = gameType.cols;
        field = new Cell[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                var newCell = new Cell(x, y);
                newCell.setState(CellState.CLOSED);
                field[y][x] = newCell;
            }
        }
    }

    private void setupBombs(int startPosX, int startPosY) {
        log.debug("Bombs setup started");
        int rows = gameType.rows;
        int cols = gameType.cols;
        int startIdx = startPosY * cols + startPosX;

        List<Integer> bombIndexes = IntStream.range(0, rows * cols)
                .filter(i -> i != startIdx)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(bombIndexes, random);
        bombIndexes = bombIndexes.subList(0, gameType.bombsN);

        // Устанавливаем бомбы
        bombIndexes.forEach(idx -> {
            int y = idx / cols;
            int x = idx % cols;
            field[y][x].setBomb(true);
            field[y][x].setState(CellState.BOMB);
            fieldEventListener.onChangeCellState(x, y,
                    CellState.BOMB); // FIXME: Delete to disable hack
        });

        if (bombIndexes.contains(startIdx)) {
            log.warn("Fucked up at {}", startIdx);
        }

        log.debug("Bombs setup finished");
    }

    private void openCell(int x, int y) {
        Cell cell = field[y][x];
        openCell(cell);
    }

    private void openCell(Cell cell) {
        if (cell.isOpen() || cell.isFlagged()) {
            log.debug("Cell ({}, {}) is already open or flagged", cell.getX(), cell.getY());
            return;
        }
        if (cell.isBomb()) {
            log.debug("Cell ({}, {}) is a bomb, you lose", cell.getX(), cell.getY());
            // TODO: Lose game
            return;
        }

        openCellUnsafe(cell);
    }

    private void openCellUnsafe(Cell cell) {
        Stack<Cell> stack = new Stack<>();
        stack.push(cell);

        List<CellStateChange> changes = new ArrayList<>();

        while (!stack.isEmpty()) {
            Cell curCell = stack.pop();
            if (curCell.isOpen()) {
                continue;
            }

            curCell.setOpen(true);
            var cellsAround = findAllCellsAround(curCell);
            int bombsCount = countFromCells(cellsAround, Cell::isBomb);
            var newStateForCell = CellState.fromAlias(
                    String.valueOf(
                            bombsCount
                    )
            );
            curCell.setState(newStateForCell);
            changes.add(new CellStateChange(curCell.getX(), curCell.getY(), newStateForCell));
            if (bombsCount > 0) {
                continue;
            }

            cellsAround.forEach(c -> {
                stack.push(c);
                // Need if we want to ignore flagged cells
//                if (!c.isFlagged()) {
//                }
            });
        }

        fieldEventListener.onBatchChangeCellState(changes);
        if ((closedCellsCount -= changes.size()) == 0) {
            log.debug("All cells opened, you win!");
            // TODO: Win game
        }
    }

    private void sendUpdBombsCount() {
        var bombsCount = gameType.bombsN - flagsCount;
        fieldEventListener.onChangeBombsCount(Math.max(bombsCount, 0));
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < gameType.cols && y >= 0 && y < gameType.rows;
    }

    private List<Cell> findAllCellsAround(Cell cell) {
        return findCellsAround(cell, c -> true);
    }

    private List<Cell> findCellsAround(Cell cell, Predicate<Cell> cellPredicate) {
        List<Cell> cells = new ArrayList<>(8);

        forEachCellAround(cell, (x, y) -> {
            var curCell = field[y][x];
            if (cellPredicate.test(curCell)) {
                cells.add(curCell);
            }
        });

        return cells;
    }

    private void forEachCellAround(Cell cell, BiConsumer<Integer, Integer> action) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int newX = cell.getX() + i;
                int newY = cell.getY() + j;
                if (isInBounds(newX, newY)) {
                    action.accept(newX, newY);
                }
            }
        }
    }

    private List<Cell> findFromCells(List<Cell> cells, Predicate<Cell> cellPredicate) {
        return cells.stream()
                .filter(cellPredicate)
                .collect(Collectors.toList());
    }

    private int countFromCells(List<Cell> cells, Predicate<Cell> cellPredicate) {
        return (int) cells.stream()
                .filter(cellPredicate)
                .count();
    }

}
