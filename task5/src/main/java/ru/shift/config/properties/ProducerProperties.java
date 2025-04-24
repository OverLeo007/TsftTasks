package ru.shift.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProducerProperties {
    private int count;
    private int time;
}

