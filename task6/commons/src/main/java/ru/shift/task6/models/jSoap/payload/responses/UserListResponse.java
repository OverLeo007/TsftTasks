package ru.shift.task6.models.jSoap.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import ru.shift.task6.models.jSoap.payload.Payload;
import ru.shift.task6.models.jSoap.payload.UserInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse implements Payload {
    private List<UserInfo> users;
}
