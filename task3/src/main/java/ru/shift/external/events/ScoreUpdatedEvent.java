package ru.shift.external.events;

import ru.shift.app.bus.base.Event;
import ru.shift.external.scores.Score;
import ru.shift.app.GameDifficulty;

public record ScoreUpdatedEvent(GameDifficulty difficulty, Score score) implements Event {}
