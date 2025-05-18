package ru.shift.task6.alt.commons.channel;

import java.io.IOException;
import ru.shift.task6.alt.commons.protocol.ProtocolException;
import ru.shift.task6.alt.commons.protocol.abstracts.Message;

public interface ChatReader {
    Message readMessage() throws IOException, ProtocolException;
}
