package ru.shift.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RunProperties {
    private ConsumerProperties consumer;
    private ProducerProperties producer;
    private StorageProperties storage;
}
