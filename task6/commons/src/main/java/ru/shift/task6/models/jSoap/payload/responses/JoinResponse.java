package ru.shift.task6.models.jSoap.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.models.jSoap.payload.Payload;
import ru.shift.task6.models.jSoap.payload.UserInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinResponse implements Payload {
    private UserInfo user;
}
