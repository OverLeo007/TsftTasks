package ru.shift.server.handling.handlers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.shift.commons.models.PayloadType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BroadcastResponse {
    PayloadType value();
}
