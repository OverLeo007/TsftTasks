package ru.shift.task6.commons.models.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.annotations.PayloadMapping;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.UserInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PayloadMapping(PayloadType.AUTH)
public class AuthRequest implements Payload {
    private UserInfo user;
}
