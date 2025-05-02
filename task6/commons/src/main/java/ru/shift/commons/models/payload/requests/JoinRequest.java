package ru.shift.commons.models.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.shift.commons.annotations.PayloadMapping;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;

@Data
@AllArgsConstructor
@PayloadMapping(PayloadType.JOIN_RQ)
public class JoinRequest implements Payload {
}
