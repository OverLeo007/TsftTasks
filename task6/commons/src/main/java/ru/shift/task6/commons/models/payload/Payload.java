package ru.shift.task6.commons.models.payload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import ru.shift.task6.commons.models.payload.requests.AuthRequest;
import ru.shift.task6.commons.models.payload.requests.JoinRequest;
import ru.shift.task6.commons.models.payload.requests.UserListRequest;
import ru.shift.task6.commons.models.payload.responses.ErrorResponse;
import ru.shift.task6.commons.models.payload.responses.JoinNotification;
import ru.shift.task6.commons.models.payload.responses.JoinResponse;
import ru.shift.task6.commons.models.payload.responses.LeaveNotification;
import ru.shift.task6.commons.models.payload.responses.SuccessAuthResponse;
import ru.shift.task6.commons.models.payload.responses.UserListResponse;

@JsonTypeInfo(
        use = Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @Type(value = ShutdownNotice.class, name = "SHUTDOWN"),
        @Type(value = ChatMessage.class, name = "MESSAGE"),
        @Type(value = ErrorResponse.class, name = "ERROR"),
        @Type(value = JoinNotification.class, name = "JOIN_NOTIFICATION"),
        @Type(value = JoinResponse.class, name = "JOIN_RS"),
        @Type(value = LeaveNotification.class, name = "LEAVE_NOTIFICATION"),
        @Type(value = SuccessAuthResponse.class, name = "SUCCESS"),
        @Type(value = UserListResponse.class, name = "USER_LIST_RS"),
        @Type(value = AuthRequest.class, name = "AUTH"),
        @Type(value = JoinRequest.class, name = "JOIN_RQ"),
        @Type(value = UserListRequest.class, name = "USER_LIST_RQ")
})
public interface Payload {
}

