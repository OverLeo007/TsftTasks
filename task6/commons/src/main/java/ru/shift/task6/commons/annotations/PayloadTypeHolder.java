package ru.shift.task6.commons.annotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;
import ru.shift.task6.commons.exceptions.TypeMappingException;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PayloadTypeHolder {

    private static final String PAYLOAD_CLASSPATH = "ru.shift.task6.commons.models.payload";

    private static final Map<PayloadType, Class<? extends Payload>> PAYLOAD_TYPES = new HashMap<>();
    private static final Map<Class<? extends Payload>, PayloadType> TYPE_PAYLOADS = new HashMap<>();


    static {
        Reflections reflections = new Reflections(PAYLOAD_CLASSPATH);
        Set<Class<? extends Payload>> classes = reflections.getSubTypesOf(Payload.class);

        for (Class<? extends Payload> cls : classes) {
            PayloadMapping mapping = cls.getAnnotation(PayloadMapping.class);
            if (mapping != null) {
                PAYLOAD_TYPES.put(mapping.value(), cls);
                TYPE_PAYLOADS.put(cls, mapping.value());
            }
        }
    }

    public static Class<? extends Payload> getFromType(PayloadType type) throws TypeMappingException {
        if (!PAYLOAD_TYPES.containsKey(type)) {
            throw new TypeMappingException("No message class found for type %s".formatted(type));
        }
        return PAYLOAD_TYPES.get(type);
    }
}
