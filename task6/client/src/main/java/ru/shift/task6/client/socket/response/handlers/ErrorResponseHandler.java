package ru.shift.task6.client.socket.response.handlers;

import ru.shift.task6.client.exceptions.ResponseException;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse;

public interface ErrorResponseHandler {

    default Void handle(Throwable ex) {
        handle(((ResponseException) ex).getResponse());
        return null;
    }

    void handle(ErrorResponse handle);

}
