package ru.shift.task6.commons.models.payload.responses;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("ERROR")
public class ErrorResponse implements Payload {

    public enum Fault {
        CLIENT,
        SERVER
    }

    private PayloadType correctResponseType;
    private Fault fault;
    private String message;

}



