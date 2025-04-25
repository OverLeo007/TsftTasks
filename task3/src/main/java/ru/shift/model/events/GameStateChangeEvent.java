package ru.shift.model.events;

import ru.shift.app.bus.base.Event;
import ru.shift.model.state.GameState;

public record GameStateChangeEvent(GameState gameState) implements Event {}
