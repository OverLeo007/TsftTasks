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
@PayloadMapping(PayloadType.LEAVE_RS)
public class LeaveResponse implements Payload {
    private UserInfo user;
}
