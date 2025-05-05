package ru.shift.commons.models.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.commons.annotations.PayloadMapping;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;
import ru.shift.commons.models.payload.UserInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PayloadMapping(PayloadType.JOIN_NOTIFICATION)
public class JoinNotification implements Payload {
    private UserInfo user;
}
