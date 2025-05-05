package ru.shift.task6.commons.models.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import ru.shift.task6.commons.annotations.PayloadMapping;
import ru.shift.task6.commons.models.PayloadType;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.UserInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PayloadMapping(PayloadType.USER_LIST_RS)
public class UserListResponse implements Payload {
    private List<UserInfo> users;
}
