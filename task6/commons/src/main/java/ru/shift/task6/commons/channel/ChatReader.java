package ru.shift.task6.commons.channel;

import java.io.IOException;
import ru.shift.task6.commons.exceptions.ProtocolException;
import ru.shift.task6.commons.protocol.abstracts.Message;

public interface ChatReader {
    Message readMessage() throws IOException, ProtocolException;
}
