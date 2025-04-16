package ru.shift.config.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RunConfig {
    private ConsumerCfg consumer;
    private ProducerCfg producer;
    private StorageCfg storage;
}
