package ru.shift.task6.models.jSoap;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header {
    private PayloadType payloadType;
    private Instant sendTime;
}
