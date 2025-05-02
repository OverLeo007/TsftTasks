package ru.shift.commons.models.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import ru.shift.commons.annotations.PayloadMapping;
import ru.shift.commons.models.PayloadType;
import ru.shift.commons.models.payload.Payload;
import ru.shift.commons.models.payload.UserInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PayloadMapping(PayloadType.USER_LIST_RS)
public class UserListResponse implements Payload {
    private List<UserInfo> users;
}
