package ru.shift.task6.models.jSoap.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShutdownNotice implements Payload {
    private String reason;
}