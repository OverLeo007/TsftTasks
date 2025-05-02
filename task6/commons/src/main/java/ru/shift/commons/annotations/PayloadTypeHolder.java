package ru.shift.commons.annotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;
import ru.shift.commons.exceptions.TypeMappingException;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PayloadTypeHolder {

    private static final String PAYLOAD_CLASSPATH = "ru.shift.commons.models.payload";

    private static final Map<PayloadType, Class<? extends Payload>> PAYLOAD_TYPES = new HashMap<>();

    static {
        Reflections reflections = new Reflections(PAYLOAD_CLASSPATH);
        Set<Class<? extends Payload>> classes = reflections.getSubTypesOf(Payload.class);

        for (Class<? extends Payload> cls : classes) {
            PayloadMapping mapping = cls.getAnnotation(PayloadMapping.class);
            if (mapping != null) {
                PAYLOAD_TYPES.put(mapping.value(), cls);
            }
        }
    }

    public static Class<? extends Payload> getFromType(PayloadType type) {
        if (!PAYLOAD_TYPES.containsKey(type)) {
            throw new TypeMappingException("No message class found for type %s".formatted(type));
        }
        return PAYLOAD_TYPES.get(type);
    }
}
