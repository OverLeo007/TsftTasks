package ru.shift.task6.models;

import java.time.Instant;

public record User(
        String name,
        Instant logTime
) {

}
