package ru.shift.task6.commons.models.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.annotations.PayloadMapping;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.UserInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PayloadMapping(PayloadType.JOIN_NOTIFICATION)
public class JoinNotification implements Payload {
    private UserInfo user;
}
