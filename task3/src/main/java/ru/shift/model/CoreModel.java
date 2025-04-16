package ru.shift.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import ru.shift.app.GameDifficulty;
import ru.shift.app.bus.EventBus;
import ru.shift.app.bus.api.EventEmitter;
import ru.shift.model.api.BombVisibilityToggle;
import ru.shift.model.api.CellStateManipulator;
import ru.shift.model.api.GameDifficultyHolder;
import ru.shift.model.api.NewGameInitializer;
import ru.shift.model.events.BatchOfCellChangeEvent;
import ru.shift.model.events.BombsCountChangeEvent;
import ru.shift.model.events.CellChangeEvent;
import ru.shift.model.events.FieldSetupEvent;
import ru.shift.model.events.NewGameDifficultyEvent;
import ru.shift.model.field.Cell;
import ru.shift.model.field.CellState;
import ru.shift.model.field.CellStateChange;
import ru.shift.model.state.GameState;
import ru.shift.model.state.GameStateMachine;

@Slf4j
public class CoreModel implements CellStateManipulator, NewGameInitializer,
        BombVisibilityToggle, GameDifficultyHolder {

    private final EventEmitter eventEmitter = EventBus.getEventEmitter();

    /** Менеджер состояния игры, имеет сайд эффекты на смену состояния,
     * знает о текущей сложности игры, управляет репозиторием рекордов и таймером.
     * В общем занимается событиями происходящими при смене состояния игры.
     */
    private final GameStateMachine gsm = new GameStateMachine();

    private int rows = GameDifficulty.NOVICE.rows;
    private int cols = GameDifficulty.NOVICE.cols;
    private int bombsN = GameDifficulty.NOVICE.bombsN;

    private final Random random = new Random(42);
    private Cell[][] field;

    private int flagsCount = 0;

    private int closedCellsCount = 0;

    private boolean isBombsVisible = false;


    @Override
    public void openCell(int x, int y) {
        log.debug("Try to open cell at ({}, {}) in {} game state", x, y, gsm.getState());

        switch (gsm.getState()) {
            case STOP -> startGameFrom(x, y);
            case PLAY -> openCellStateless(x, y);
        }

    }

    @Override
    public void toggleFlag(int x, int y) {
        log.debug("Try to toggle flag at ({}, {})", x, y);
        if (gsm.compareState(
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
            if (flagsCount >= bombsN) {
                return;
            }
            curCell.setFlagged(true);
            flagsCount++;
        }
        var newState = curCell.isFlagged() ? CellState.FLAG : CellState.CLOSED;
        eventEmitter.emit(new CellChangeEvent(x, y, newState));

        sendUpdBombsCount();
    }

    @Override
    public void openCellsAround(int x, int y) {
        log.debug("Try to open cells around ({}, {})", x, y);
        if (gsm.compareState(state -> state != GameState.PLAY)) {
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
            if (gsm.compareState(state -> state == GameState.PLAY)) {
                openCellStateless(nextCell);
            }
        }

    }

    private void startGameFrom(int x, int y) {
        log.debug("Game started from ({}, {})", x, y);
        setupBombs(x, y);
        gsm.setState(GameState.PLAY);
        openCellStateless(x, y);
    }

    @Override
    public void initNewGameWithDifficulty(GameDifficulty gameDifficulty) {
        this.gsm.setGameDifficulty(gameDifficulty);

        this.rows = gameDifficulty.rows;
        this.cols = gameDifficulty.cols;
        this.bombsN = gameDifficulty.bombsN;
        log.debug("Game type selected: {}", gameDifficulty);
        eventEmitter.emit(new NewGameDifficultyEvent(gameDifficulty));
        setupNewGame();
    }

    @Override
    public void initNewGame() {
        setupNewGame();
    }

    private void setupNewGame() {
        log.debug("Setup new field with size {}x{}", rows, cols);


        eventEmitter.emit(new FieldSetupEvent(gsm.getGameDifficulty()));
        field = new Cell[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                var newCell = new Cell(x, y);
                field[y][x] = newCell;
            }
        }
        log.debug("Game data setup");

        closedCellsCount = rows * cols - bombsN;
        flagsCount = 0;

        gsm.setState(GameState.STOP);

        log.debug("New game is ready");
    }


    private void setupBombs(int startPosX, int startPosY) {
        log.debug("Bombs setup started");
        int startIdx = startPosY * cols + startPosX;

        List<Integer> bombIndexes = IntStream.range(0, rows * cols)
                .filter(i -> i != startIdx)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(bombIndexes, random);
        bombIndexes = bombIndexes.subList(0, bombsN);

        bombIndexes.forEach(idx -> {
            int y = idx / cols;
            int x = idx % cols;
            field[y][x].setBomb(true);
        });

        if (bombIndexes.contains(startIdx)) {
            log.warn("Fucked up at {}", startIdx);
        }
        if (isBombsVisible) {
            showBombs();
        }
        log.debug("Bombs setup finished");
    }

    private void openCellStateless(int x, int y) {
        Cell cell = field[y][x];
        openCellStateless(cell);
    }

    private void openCellStateless(Cell cell) {
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

            if (curCell.isFlagged()) {
                toggleFlag(curCell.getX(), curCell.getY());
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

            cellsAround.forEach(stack::push);
        }

        eventEmitter.emit(new BatchOfCellChangeEvent(changes));

        if ((closedCellsCount -= changes.size()) == 0) {
            log.debug("All cells opened, you win!");
            endGame(GameState.WIN);
        }
    }

    private void endGame(GameState endState) {
        showBombs();
        gsm.setState(endState);
    }

    private void showBombs() {
        List<CellStateChange> changes = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Cell cell = field[y][x];
                if (cell.isBomb()) {
                    changes.add(new CellStateChange(x, y, CellState.BOMB));
                }
            }
        }
        eventEmitter.emit(new BatchOfCellChangeEvent(changes));

    }

    private void hideBombs() {
        List<CellStateChange> changes = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Cell cell = field[y][x];
                if (cell.isBomb()) {
                    if (cell.isFlagged()) {
                        changes.add(new CellStateChange(x, y, CellState.FLAG));
                    } else {
                        changes.add(new CellStateChange(x, y, CellState.CLOSED));
                    }
                }
            }
        }
        eventEmitter.emit(new BatchOfCellChangeEvent(changes));

    }

    private void sendUpdBombsCount() {
        var bombsCount = bombsN - flagsCount;
        eventEmitter.emit(new BombsCountChangeEvent(bombsCount));
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
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

    @Override
    public void setBombVisibility(boolean isVisible) {
        if (isVisible) {
            isBombsVisible = true;
            showBombs();
        } else {
            isBombsVisible = false;
            hideBombs();
        }
    }

    @Override
    public GameDifficulty getGameDifficulty() {
        return gsm.getGameDifficulty();
    }
}
