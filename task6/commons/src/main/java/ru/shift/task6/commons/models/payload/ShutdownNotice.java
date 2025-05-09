package ru.shift.task6.commons.models.payload;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("SHUTDOWN")
public class ShutdownNotice implements Payload {
    private String reason;

}