package ru.shift.task6.commons.channel;

import ru.shift.task6.commons.exceptions.SerializationException;
import ru.shift.task6.commons.models.Envelope;

public interface ChatWriter {
    void sendEnvelope(Envelope<?> envelope) throws SerializationException;

    boolean checkWriterError();
}
