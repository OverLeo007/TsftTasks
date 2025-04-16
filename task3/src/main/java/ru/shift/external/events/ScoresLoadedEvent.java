package ru.shift.external.events;

import java.util.Map;
import ru.shift.app.bus.base.Event;
import ru.shift.external.scores.Score;
import ru.shift.app.GameDifficulty;

public record ScoresLoadedEvent(Map<GameDifficulty, Score> scores) implements Event {}
