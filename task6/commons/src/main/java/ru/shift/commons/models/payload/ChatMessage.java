package ru.shift.commons.models.payload;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shift.commons.annotations.PayloadMapping;
import ru.shift.commons.models.PayloadType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PayloadMapping(PayloadType.MESSAGE)
public class ChatMessage implements Payload {
    private UserInfo sender;
    private String text;
    private Instant time;
}