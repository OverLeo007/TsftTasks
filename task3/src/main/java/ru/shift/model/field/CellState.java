package ru.shift.model.field;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CellState {
    EMPTY("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    FLAG("f"),
    BOMB("b"),
    CLOSED("c");

    private final static Map<String, CellState> aliasMap =
            Arrays.stream(CellState.values())
                    .collect(Collectors.toMap(CellState::getAlias, cellState -> cellState));

    private final String alias;

    public static CellState fromAlias(String alias) {
        CellState cellState = aliasMap.get(alias);
        if (cellState == null) {
            throw new IllegalArgumentException("Unknown alias for cell state: " + alias);
        }
        return cellState;
    }


}
