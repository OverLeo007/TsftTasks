package ru.shift.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.shift.commons.annotations.PayloadTypeHolder;
import ru.shift.commons.exceptions.DeserializationException;
import ru.shift.commons.models.Envelope;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;

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
            throws DeserializationException {
        JavaType type = mapper.getTypeFactory()
                .constructParametricType(Envelope.class, payloadClass);
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new DeserializationException(e);
        }
    }

    public static Envelope<? extends Payload> deserialize(String json)
            throws DeserializationException {
        JsonNode root;
        try {
             root = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new DeserializationException(e);
        }

        JsonNode headerNode = root.get("header");

        if (headerNode == null || headerNode.get("payloadType") == null) {
            throw new DeserializationException("Некорректный JSON: нет поля 'payloadType'");
        }

        PayloadType payloadType = PayloadType.valueOf(headerNode.get("payloadType").asText());
        Class<? extends Payload> payloadClass = PayloadTypeHolder.getFromType(payloadType);

        if (payloadClass == null) {
            throw new DeserializationException("Неизвестный тип payload: " + payloadType);
        }
        return deserialize(json, payloadClass);
    }

}
