package ru.shift.task6.commons.protocol.abstracts;

import ru.shift.task6.commons.protocol.MessageType;

public interface Message {

    MessageType getType();

    boolean isRequest();

    boolean isResponse();
}
