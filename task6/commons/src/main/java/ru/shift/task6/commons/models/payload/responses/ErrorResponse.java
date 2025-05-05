package ru.shift.task6.commons.models.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.annotations.PayloadMapping;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PayloadMapping(PayloadType.ERROR)
public class ErrorResponse implements Payload {

    public enum Fault {
        CLIENT,
        SERVER
    }

    private PayloadType correctResponseType;
    private Fault fault;
    private String message;
}
