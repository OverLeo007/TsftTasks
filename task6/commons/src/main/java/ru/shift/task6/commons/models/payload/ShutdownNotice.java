package ru.shift.task6.commons.models.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.annotations.PayloadMapping;
import ru.shift.task6.commons.models.PayloadType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PayloadMapping(PayloadType.SHUTDOWN)
public class ShutdownNotice implements Payload {
    private String reason;
}