package ru.shift.model.events;

import ru.shift.app.bus.base.Event;
import ru.shift.external.scores.Score;

public record NewHighScoreEvent(int newTime, Score oldScore) implements Event {}
