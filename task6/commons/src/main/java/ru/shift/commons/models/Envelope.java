package ru.shift.commons.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.commons.models.payload.Payload;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Envelope<T extends Payload> {
    private Header header;
    private T payload;
}

