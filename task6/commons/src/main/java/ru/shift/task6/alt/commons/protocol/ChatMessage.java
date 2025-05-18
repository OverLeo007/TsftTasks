package ru.shift.task6.alt.commons.protocol;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private UserInfo sender;
    private Instant sendTime;
    private String body;
}
