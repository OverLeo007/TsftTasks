package ru.shift.server.handling.raw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import ru.shift.commons.models.Envelope;
import ru.shift.commons.models.Header;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;
import ru.shift.commons.models.payload.responses.ErrorResponse;
import ru.shift.commons.models.payload.responses.ErrorResponse.Fault;
import ru.shift.server.handling.handlers.annotations.BroadcastResponse;
import ru.shift.server.handling.handlers.annotations.Handler;
import ru.shift.server.handling.handlers.annotations.RequestType;
import ru.shift.server.handling.handlers.annotations.ResponseType;
import ru.shift.server.client.ClientContext;
import ru.shift.server.services.ClientService;
import ru.shift.server.exceptions.NoHandlerFoundException;
import ru.shift.server.exceptions.client.AbstractClientFaultException;

@Slf4j
public class HandlersRegistry {

    private static final String HANDLERS_CLASSPATH = "ru.shift.server.handling.handlers";

    private final Map<PayloadType, Consumer<Envelope<?>>> handlers = new EnumMap<>(PayloadType.class);

    private final BiConsumer<PayloadType, Payload> responseSender;
    private final BiConsumer<PayloadType, Payload> broadcaster;
    private final BiConsumer<Fault, Throwable> errorResponseSender;


    public HandlersRegistry(
            ClientContext context,
            ClientService service,
            BiConsumer<PayloadType, Payload> responseSender,
            BiConsumer<PayloadType, Payload> broadcaster,
            BiConsumer<Fault, Throwable> errorResponseSender
    ) {
        this.responseSender = responseSender;
        this.errorResponseSender = errorResponseSender;
        this.broadcaster = broadcaster;
        registerHandlers(context, service);
    }

    public Consumer<Envelope<?>> getHandler(PayloadType type) {
        if (!handlers.containsKey(type)) {
            log.error("No handler found for {}", type);
            return createErrorHandler(
                    new NoHandlerFoundException("No handler found for " + type)
            );
        }
        return handlers.get(type);
    }

    private void registerHandlers(ClientContext context, ClientService service) {

        Reflections reflections = new Reflections(HANDLERS_CLASSPATH);
        Set<Class<?>> handlerClasses = reflections.getTypesAnnotatedWith(Handler.class);

        for (Class<?> clazz : handlerClasses) {
            log.trace("Creating handlers of {} instance", clazz.getSimpleName());
            try {
                Object instance;
                // TODO: Weak point
                if (Arrays.stream(clazz.getDeclaredFields()).anyMatch(f -> f.getType().equals(ClientService.class))) {
                    instance = clazz.getDeclaredConstructor(ClientContext.class, ClientService.class)
                            .newInstance(context, service);
                } else {
                    instance = clazz.getDeclaredConstructor(ClientContext.class)
                            .newInstance(context);
                }
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(RequestType.class)) {
                        PayloadType type = method.getAnnotation(RequestType.class).type();
                        log.trace("Creating handler for {}", type);
                        method.setAccessible(true);
                        handlers.put(type, createHandler(instance, method));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate controller: " + clazz, e);
            }
        }
    }

    private Consumer<Envelope<?>> createHandler(Object instance, Method method) {
        return (env) -> {
            try {
                Object arg = getMethodArg(method, env);

                Object result = method.invoke(instance, arg);
                if (result != null) {
                    if (method.isAnnotationPresent(ResponseType.class)) {
                        PayloadType responseType = method.getAnnotation(ResponseType.class).value();
                        responseSender.accept(responseType, (Payload) result);
                    } else if (method.isAnnotationPresent(BroadcastResponse.class)) {
                        PayloadType responseType = method.getAnnotation(BroadcastResponse.class).value();
                        broadcaster.accept(responseType, (Payload) result);
                    }
                }
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();

                log.debug("Error while handling message: {}", cause.getMessage());

                ErrorResponse.Fault fault = (cause instanceof AbstractClientFaultException)
                        ? Fault.CLIENT
                        : Fault.SERVER;

                errorResponseSender.accept(fault, cause);

            } catch (Exception e) {
                errorResponseSender.accept(Fault.SERVER, e);
            }
        };
    }

    private Object getMethodArg(Method method, Envelope<?> env) {
        RequestType requestType = method.getAnnotation(RequestType.class);
        Class<?> accessLevel = requestType != null ? requestType.accessLevel() : Payload.class;

        Object arg;
        if (accessLevel.equals(Header.class)) {
            arg = env.getHeader();
        } else if (accessLevel.equals(Envelope.class)) {
            arg = env;
        } else {
            arg = env.getPayload();
        }
        return arg;
    }

    private Consumer<Envelope<?>> createErrorHandler(Throwable cause) {
        return  env -> errorResponseSender.accept(Fault.SERVER, cause);
    }
}
