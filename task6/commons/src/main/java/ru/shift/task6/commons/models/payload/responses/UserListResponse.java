package ru.shift.task6.commons.models.payload.responses;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.UserInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("USER_LIST_RS")
public class UserListResponse implements Payload {
    private List<UserInfo> users;
}

