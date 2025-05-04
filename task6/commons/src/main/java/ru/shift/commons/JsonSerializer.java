package ru.shift.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.commons.annotations.PayloadTypeHolder;
import ru.shift.commons.exceptions.DeserializationException;
import ru.shift.commons.exceptions.SerializationException;
import ru.shift.commons.exceptions.TypeMappingException;
import ru.shift.commons.models.Envelope;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;

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

    public static <T extends Payload> Envelope<T> deserialize(String json, Class<T> payloadClass)
            throws DeserializationException {
        JavaType type = mapper.getTypeFactory()
                .constructParametricType(Envelope.class, payloadClass);
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("Error while deserialization occurred", e);
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
            throw new DeserializationException("Incorrect JSON: no 'payloadType' field ");
        }

        try {
            PayloadType payloadType = PayloadType.valueOf(headerNode.get("payloadType").asText());
            Class<? extends Payload> payloadClass = PayloadTypeHolder.getFromType(payloadType);

            if (payloadClass == null) {
                throw new DeserializationException("Unknown payload type: " + payloadType);
            }
            return deserialize(json, payloadClass);
        } catch (TypeMappingException e) {
            throw new DeserializationException(e);
        } catch (IllegalArgumentException e) {
            throw new DeserializationException("PayloadType constant with this name not found", e);
        }
    }

}
