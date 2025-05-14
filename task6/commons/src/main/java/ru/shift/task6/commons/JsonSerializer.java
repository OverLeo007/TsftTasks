package ru.shift.task6.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.exceptions.DeserializationException;
import ru.shift.task6.commons.exceptions.SerializationException;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.payload.Payload;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class JsonSerializer {

    private static final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String serialize(Envelope<? extends Payload> envelope)
            throws SerializationException {
        try {
            return mapper.writeValueAsString(envelope);
        } catch (JsonProcessingException e) {
            log.error("Error while serialization occurred", e);
            throw new SerializationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Envelope<? extends Payload> deserialize(String json)
            throws DeserializationException {
        try {
            return mapper.readValue(json, Envelope.class);
        } catch (JsonProcessingException e) {
            throw new DeserializationException("Error deserializing the envelope", e);
        }
    }


}
