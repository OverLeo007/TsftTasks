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
import ru.shift.controller.listeners.CM_onToggleBombsVisibilityListener;
import ru.shift.model.GameDifficulty;
import ru.shift.model.listeners.MV_EndGameListener;
import ru.shift.model.listeners.MV_FiledEventListener;
import ru.shift.model.listeners.MV_NewGameListener;
import ru.shift.model.listeners.MV_OnNewRecordListener;
import ru.shift.model.scores.ScoreRepository;
import ru.shift.model.state.GameState;
import ru.shift.model.state.GameStateManager;
import ru.shift.model.timer.Timer;

@Slf4j
public class CoreModel implements CM_FieldEventListener, CM_NewGameListener,
        CM_onToggleBombsVisibilityListener {

    private final MV_FiledEventListener fieldEventListener;
    private final MV_NewGameListener gameTypeListener;

    /** Менеджер состояния игры, имеет сайд эффекты на смену состояния,
     * знает о текущей сложности игры, управляет репозиторием рекордов и таймером.
     * В общем занимается событиями происходящими при смене состояния игры.
     */
    private final GameStateManager gsm;

    private int rows = GameDifficulty.NOVICE.rows;
    private int cols = GameDifficulty.NOVICE.cols;
    private int bombsN = GameDifficulty.NOVICE.bombsN;

    private final Random random = new Random(42);
    private Cell[][] field;

    private int flagsCount = 0;

    private int closedCellsCount = 0;

    private boolean isBombsVisible = false;

    @Builder
    private CoreModel(
            MV_FiledEventListener fieldEventListener,
            MV_NewGameListener gameTypeListener,
            Timer timer,
            ScoreRepository scoreRepository,
            MV_EndGameListener loseListener,
            MV_EndGameListener winListener,
            MV_OnNewRecordListener newRecordListener
    ) {
        this.fieldEventListener = fieldEventListener;
        this.gameTypeListener = gameTypeListener;
        this.gsm = GameStateManager.builder()
                .timer(timer)
                .winListener(winListener)
                .loseListener(loseListener)
                .onNewRecordListener(newRecordListener)
                .scoreRepository(scoreRepository)
                .build();
    }

    @Override
    public void onOpenCell(int x, int y) {
        log.debug("Try to open cell at ({}, {}) in {} game state", x, y, gsm.getState());

        switch (gsm.getState()) {
            case STOP -> startGameFrom(x, y);
            case PLAY -> openCell(x, y);
        }

    }

    @Override
    public void onToggleFlag(int x, int y) {
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
        fieldEventListener.onChangeCellState(x, y, newState);
        sendUpdBombsCount();
    }

    @Override
    public void onOpenCellsAround(int x, int y) {
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
                openCell(nextCell);
            }
        }

    }

    private void startGameFrom(int x, int y) {
        log.debug("Game started from ({}, {})", x, y);
        setupBombs(x, y);
        gsm.setState(GameState.PLAY);
        openCell(x, y);
    }

    @Override
    public void onGameTypeSelected(GameDifficulty gameDifficulty) {
        this.gsm.setGameDifficulty(gameDifficulty);

        this.rows = gameDifficulty.rows;
        this.cols = gameDifficulty.cols;
        this.bombsN = gameDifficulty.bombsN;
        log.debug("Game type selected: {}", gameDifficulty);
        gameTypeListener.onGameTypeSelected(gameDifficulty);
        setupNewGame();
    }

    @Override
    public void onNewGame() {
        setupNewGame();
    }

    private void setupNewGame() {
        log.debug("Setup new field with size {}x{}", rows, cols);
        fieldEventListener.onFieldSetup(this.gsm.getGameDifficulty());

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
        gsm.setGameDifficulty(this.gsm.getGameDifficulty());

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

            if (curCell.isFlagged()) {
                onToggleFlag(curCell.getX(), curCell.getY());
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

        fieldEventListener.onBatchChangeCellState(changes);
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
        fieldEventListener.onBatchChangeCellState(changes);
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
        fieldEventListener.onBatchChangeCellState(changes);
    }

    private void sendUpdBombsCount() {
        var bombsCount = bombsN - flagsCount;
        fieldEventListener.onChangeBombsCount(bombsCount);
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
    public void onToggleBombsVisibility(boolean isVisible) {
        if (isVisible) {
            isBombsVisible = true;
            showBombs();
        } else {
            isBombsVisible = false;
            hideBombs();
        }
    }
}
