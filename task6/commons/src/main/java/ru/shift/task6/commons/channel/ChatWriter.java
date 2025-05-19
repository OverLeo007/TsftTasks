package ru.shift.task6.commons.channel;

import ru.shift.task6.commons.exceptions.ProtocolException;
import ru.shift.task6.commons.protocol.abstracts.Message;

public interface ChatWriter {
    void sendMessage(Message message) throws ProtocolException;

    boolean checkWriterError();
}
