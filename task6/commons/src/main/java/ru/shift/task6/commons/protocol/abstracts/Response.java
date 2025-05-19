package ru.shift.task6.commons.protocol.abstracts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Response extends AbstractMessageImpl {
    protected String errorMessage;

    public Response(@NonNull String id) {
        super(id);
    }

    public Response(@NonNull String id, String errorMessage) {
        super(id);
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return getErrorMessage() == null;
    }

    public boolean isError() {
        return getErrorMessage() != null;
    }

    @Override
    public boolean isRequest() {
        return false;
    }

    @Override
    public boolean isResponse() {
        return true;
    }
}
