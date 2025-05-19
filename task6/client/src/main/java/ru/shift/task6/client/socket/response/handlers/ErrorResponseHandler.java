package ru.shift.task6.client.socket.response.handlers;

public interface ErrorResponseHandler {

    default Void handle(Throwable ex) {
        handle(ex.getMessage());
        return null;
    }

    void handle(String errorMessage);

}
