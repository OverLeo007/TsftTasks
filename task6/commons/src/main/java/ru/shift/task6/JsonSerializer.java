package ru.shift.task6;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.shift.task6.models.jSoap.Envelope;
import ru.shift.task6.models.jSoap.payload.Payload;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonSerializer {

    private static final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String serialize(Envelope<? extends Payload> envelope)
            throws JsonProcessingException {
        return mapper.writeValueAsString(envelope);
    }

    public static <T extends Payload> Envelope<T> deserialize(String json, Class<T> payloadClass)
            throws JsonProcessingException {
        JavaType type = mapper.getTypeFactory()
                .constructParametricType(Envelope.class, payloadClass);

        return mapper.readValue(json, type);
    }
}
