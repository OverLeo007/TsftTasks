package ru.shift.model.field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import ru.shift.controller.listeners.CM_FieldEventListener;
import ru.shift.controller.listeners.CM_GameTypeListener;
import ru.shift.model.listeners.MV_FiledEventListener;
import ru.shift.model.listeners.MV_GameTypeListener;
import ru.shift.view.GameType;

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
            case STOP -> {
                startGameFrom(x, y);
            }
            case PLAY -> {
                openCell(x, y);
            }

        }

    }

    private void startGameFrom(int x, int y) {
        log.debug("Game started from ({}, {})", x, y);
        setupBombs(x, y);
        gameState = GameState.PLAY;
        openCell(x, y);


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
            fieldEventListener.onChangeCellState(x, y, CellState.BOMB); // FIXME: Delete to disable hack
        });

        if (bombIndexes.contains(startIdx)) {
            log.warn("Fucked up at {}", startIdx);
        }

        log.debug("Bombs setup finished");
    }


    private void openCell(int x, int y) {
        Cell cell = field[y][x];
        if (cell.isOpen() || cell.isFlagged()) {
            log.debug("Cell ({}, {}) is already open or flagged", x, y);
            return;
        }
        if (cell.isBomb()) {
            log.debug("Cell ({}, {}) is a bomb", x, y);
            // TODO: Lose game
            return;
        }

        Stack<Cell> stack = new Stack<>();
        stack.push(cell);

        List<CellStateChange> changes = new ArrayList<>();

        while (!stack.isEmpty()) {
            Cell curCell = stack.pop();
            if (curCell.isOpen()) {
                continue;
            }

            curCell.setOpen(true);
            var cellsAround = getCellsAround(curCell);
            int bombsCount = getBombsCount(cellsAround);
            var newStateForCell = CellState.fromAlias(
                    String.valueOf(
                            bombsCount
                    )
            );
            changes.add(new CellStateChange(curCell.getX(), curCell.getY(), newStateForCell));
            if (bombsCount > 0) {
                continue;
            }

            cellsAround.forEach(c -> {
                if (!c.isFlagged()) {
                    stack.push(c);
                }
            });
        }

        fieldEventListener.onBatchChangeCellState(changes);
        if ((closedCellsCount -= changes.size()) == 0) {
            // TODO: Win game
        }

    }

    private List<Cell> getCellsAround(Cell cell) {
        List<Cell> cells = new ArrayList<>(8);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int newX = cell.getX() + i;
                int newY = cell.getY() + j;
                if (!isOutOfBounds(newX, newY)) {
                    cells.add(field[newY][newX]);
                }
            }
        }
        return cells;
    }

    private int getBombsCount(List<Cell> cells) {
        return (int) cells.stream().filter(Cell::isBomb).count();
    }


    private boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= gameType.cols || y < 0 || y >= gameType.rows;
    }

    @Override
    public void onToggleFlag(int x, int y) {
        log.debug("Flag toggled at ({}, {})", x, y);
        fieldEventListener.onChangeCellState(x, y, CellState.FLAG);
    }

    @Override
    public void onOpenCellsAround(int x, int y) {
        log.debug("Cells opened around ({}, {})", x, y);
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

    private void updBombsCount() {
        fieldEventListener.onChangeBombsCount(gameType.bombsN - flagsCount);
    }

    private void setupField() {
        log.debug("Setup new field with size {}x{}", gameType.rows, gameType.cols);
        int rows = gameType.rows;
        int cols = gameType.cols;
        field = new Cell[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                field[y][x] = new Cell(x, y);
            }
        }
    }


}
