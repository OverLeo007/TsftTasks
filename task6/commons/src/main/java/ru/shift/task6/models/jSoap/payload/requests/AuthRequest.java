package ru.shift.task6.models.jSoap.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.task6.models.jSoap.payload.Payload;
import ru.shift.task6.models.jSoap.payload.UserInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest implements Payload {
    private UserInfo user;
}
