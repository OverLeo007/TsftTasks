package ru.shift.task6.commons.channel;

import java.io.IOException;
import ru.shift.task6.commons.exceptions.DeserializationException;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.payload.Payload;

public interface ChatReader {
    Envelope<? extends Payload> readEnvelope() throws IOException, DeserializationException;
}
