package ru.shift.model.events;

import java.util.List;
import ru.shift.app.bus.base.Event;
import ru.shift.utils.Pair;

public record BombsGeneratedEvent(List<Pair<Integer, Integer>> bombCoords) implements Event {

}
