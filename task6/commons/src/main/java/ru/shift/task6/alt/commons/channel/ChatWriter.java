package ru.shift.task6.alt.commons.channel;

import ru.shift.task6.alt.commons.protocol.ProtocolException;
import ru.shift.task6.alt.commons.protocol.abstracts.Message;

public interface ChatWriter {
    void sendMessage(Message message) throws ProtocolException;

    boolean checkWriterError();
}
