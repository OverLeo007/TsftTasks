package ru.shift.task6.commons.models.payload.requests;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.commons.models.payload.Payload;
import ru.shift.task6.commons.models.payload.UserInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("AUTH")
public class AuthRequest implements Payload {
    private UserInfo user;
}

