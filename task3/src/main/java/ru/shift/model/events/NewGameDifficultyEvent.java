package ru.shift.model.events;

import ru.shift.app.bus.base.Event;
import ru.shift.app.GameDifficulty;

public record NewGameDifficultyEvent(GameDifficulty gameDifficulty) implements Event {}
