package ru.shift.task6.models.jSoap.payload;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage implements Payload {
    private UserInfo sender;
    private String text;
    private Instant time;
}