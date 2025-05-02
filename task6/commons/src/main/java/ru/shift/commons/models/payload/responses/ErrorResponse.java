package ru.shift.commons.models.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.commons.annotations.PayloadMapping;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PayloadMapping(PayloadType.ERROR)
public class ErrorResponse implements Payload {

    public enum Fault {
        CLIENT,
        SERVER
    }

    private Fault fault;
    private String message;
}
