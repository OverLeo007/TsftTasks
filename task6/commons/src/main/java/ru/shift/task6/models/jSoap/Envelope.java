package ru.shift.task6.models.jSoap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.models.jSoap.payload.Payload;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Envelope<T extends Payload> {
    private Header header;
    private T payload;
}

