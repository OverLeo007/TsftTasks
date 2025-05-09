package ru.shift.task6.commons.models.payload.requests;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.shift.task6.commons.models.payload.Payload;

@Data
@AllArgsConstructor
@JsonTypeName("USER_LIST_RQ")
public class UserListRequest implements Payload {
}

