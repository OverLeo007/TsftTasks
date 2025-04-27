package ru.shift.model.events;

import ru.shift.app.bus.base.Event;
import ru.shift.app.GameDifficulty;

public record FieldSetupEvent(GameDifficulty gameDifficulty) implements Event {}
