package ru.shift.task6.commons.models.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.shift.task6.commons.annotations.PayloadMapping;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;

@Data
@AllArgsConstructor
@PayloadMapping(PayloadType.USER_LIST_RQ)
public class UserListRequest implements Payload {

}
