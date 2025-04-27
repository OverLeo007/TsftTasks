package ru.shift.task6.models;

import java.time.Instant;

public record Message(
        User sender,
        MessageType messageType,
        String text,
        Instant time
) {

}
