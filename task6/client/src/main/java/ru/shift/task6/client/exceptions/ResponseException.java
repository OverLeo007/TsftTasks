package ru.shift.task6.client.exceptions;

import lombok.Getter;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse;

@Getter
public class ResponseException extends RuntimeException {
    private final ErrorResponse response;

    public ResponseException(ErrorResponse response) {
        super(response.getMessage(), null, false, false);
        this.response = response;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
