package ru.shift.task6.models.jSoap.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.models.jSoap.payload.Payload;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Payload {

    public enum Fault {
        CLIENT,
        SERVER
    }

    private Fault fault;
    private String message;
}
