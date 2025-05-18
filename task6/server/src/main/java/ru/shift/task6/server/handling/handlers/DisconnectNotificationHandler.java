package ru.shift.task6.server.handling.handlers;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.alt.commons.protocol.impl.notifications.DisconnectNotification;
import ru.shift.task6.server.handling.provider.HandlerContext;

@Slf4j
public class DisconnectNotificationHandler implements Handler<DisconnectNotification> {

    @Override
    public void handle(DisconnectNotification request, HandlerContext context) {
        log.info("Client disconnected");
        try {
            context.clientContext().close();
        } catch (IOException e) {
            log.warn("Error while closing connection with client: {}", context.clientContext(), e);
        }
    }
}
