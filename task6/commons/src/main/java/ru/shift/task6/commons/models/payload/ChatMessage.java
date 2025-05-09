package ru.shift.task6.commons.models.payload;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("MESSAGE")
public class ChatMessage implements Payload {
    private UserInfo sender;
    private String text;
    private Instant time;
}