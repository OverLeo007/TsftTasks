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
import ru.shift.controller.listeners.CM_NewGameListener;
import ru.shift.model.GameDifficulty;
import ru.shift.model.listeners.MV_EndGameListener;
import ru.shift.model.listeners.MV_FiledEventListener;
import ru.shift.model.listeners.MV_NewGameListener;
import ru.shift.model.state.GameState;
import ru.shift.model.state.GameStateManager;
import ru.shift.model.timer.Timer;

@Slf4j
public class CoreModel implements CM_FieldEventListener, CM_NewGameListener {

    private final MV_FiledEventListener fieldEventListener;
    private final MV_NewGameListener gameTypeListener;


    private final GameStateManager gameStateManager;

    @Builder
    private CoreModel(
            MV_FiledEventListener fieldEventListener,
            MV_NewGameListener gameTypeListener,
            Timer timer,
            MV_EndGameListener winListener,
            MV_EndGameListener loseListener
    ) {
        this.fieldEventListener = fieldEventListener;
        this.gameTypeListener = gameTypeListener;
        this.gameStateManager = GameStateManager.builder()
                .timer(timer)
                .winListener(winListener)
                .loseListener(loseListener)
                .build();
    }

    private GameDifficulty gameDifficulty = GameDifficulty.NOVICE;
//    private GameState gameState = GameState.STOP;
    private final Random random = new Random(42); // TODO: Remove seed

    private Cell[][] field;

    private int flagsCount = 0;

    private int closedCellsCount = 0;

    @Override
    public void onOpenCell(int x, int y) {
        log.debug("Try to open cell at ({}, {}) {}", x, y, gameStateManager.getState());

        switch (gameStateManager.getState()) {
            case STOP -> startGameFrom(x, y);
            case PLAY -> openCell(x, y);
        }

    }

    @Override
    public void onToggleFlag(int x, int y) {
        log.debug("Try to toggle flag at ({}, {})", x, y);
        if (gameStateManager.compareState(
                state -> state == GameState.LOSE || state == GameState.WIN
        )) {
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
        fieldEventListener.onChangeCellState(x, y, newState);
        sendUpdBombsCount();
    }

    @Override
    public void onOpenCellsAround(int x, int y) {
        log.debug("Try to open cells around ({}, {})", x, y);
        if (gameStateManager.compareState(state -> state != GameState.PLAY)) {
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
            if (gameStateManager.compareState(state -> state == GameState.PLAY)) {
                openCell(nextCell);
            }
        }

    }

    private void startGameFrom(int x, int y) {
        log.debug("Game started from ({}, {})", x, y);
        setupBombs(x, y);
        gameStateManager.setState(GameState.PLAY);
        openCell(x, y);
    }

    @Override
    public void onGameTypeSelected(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
        log.debug("Game type selected: {}", this.gameDifficulty);
        gameTypeListener.onGameTypeSelected(this.gameDifficulty);
        setupNewGame();
    }

    @Override
    public void onNewGame() {
        setupNewGame();
    }

    private void setupNewGame() {
        log.debug("Setup new field with size {}x{}", this.gameDifficulty.rows, this.gameDifficulty.cols);
        fieldEventListener.onFieldSetup(this.gameDifficulty);

        int rows = this.gameDifficulty.rows;
        int cols = this.gameDifficulty.cols;
        field = new Cell[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                var newCell = new Cell(x, y);
                field[y][x] = newCell;
            }
        }
        log.debug("Game data setup");

        closedCellsCount = this.gameDifficulty.rows * this.gameDifficulty.cols - this.gameDifficulty.bombsN;
        flagsCount = 0;
        gameStateManager.setState(GameState.STOP);

        log.debug("New game is ready");
    }


    private void setupBombs(int startPosX, int startPosY) {
        log.debug("Bombs setup started");
        int rows = gameDifficulty.rows;
        int cols = gameDifficulty.cols;
        int startIdx = startPosY * cols + startPosX;

        List<Integer> bombIndexes = IntStream.range(0, rows * cols)
                .filter(i -> i != startIdx)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(bombIndexes, random);
        bombIndexes = bombIndexes.subList(0, gameDifficulty.bombsN);

        bombIndexes.forEach(idx -> {
            int y = idx / cols;
            int x = idx % cols;
            field[y][x].setBomb(true);
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
            endGame(GameState.LOSE);
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
            endGame(GameState.WIN);
        }
    }

    private void endGame(GameState endState) {
        gameStateManager.setState(endState);
        showBombs();
    }

    private void showBombs() {
        List<CellStateChange> changes = new ArrayList<>();
        for (int y = 0; y < gameDifficulty.rows; y++) {
            for (int x = 0; x < gameDifficulty.cols; x++) {
                Cell cell = field[y][x];
                if (cell.isBomb()) {
                    changes.add(new CellStateChange(x, y, CellState.BOMB));
                }
            }
        }
        fieldEventListener.onBatchChangeCellState(changes);
    }

    private void sendUpdBombsCount() {
        var bombsCount = gameDifficulty.bombsN - flagsCount;
        fieldEventListener.onChangeBombsCount(Math.max(bombsCount, 0));
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < gameDifficulty.cols && y >= 0 && y < gameDifficulty.rows;
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
