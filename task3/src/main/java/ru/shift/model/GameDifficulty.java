package ru.shift.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GameDifficulty {
    NOVICE(9, 9, 10),
    MEDIUM(16, 16, 40),
    EXPERT(16, 30, 99);

    public final int rows;
    public final int cols;
    public final int bombsN;
}
