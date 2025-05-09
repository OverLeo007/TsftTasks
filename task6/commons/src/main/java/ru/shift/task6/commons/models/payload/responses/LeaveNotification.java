package ru.shift.task6.commons.models.payload.responses;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.UserInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("LEAVE_NOTIFICATION")
public class LeaveNotification implements Payload {
    private UserInfo user;
}

