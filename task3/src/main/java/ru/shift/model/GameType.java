package ru.shift.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GameType {
    NOVICE(9, 9, 10),
    MEDIUM(12, 12, 32),
    EXPERT(16, 30, 99),
    PREVIOUS(-1, -1, -1);

    public final int rows;
    public final int cols;
    public final int bombsN;
}
