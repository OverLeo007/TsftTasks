package ru.shift.task6.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RunProperties {

    private Server server;

    @Data
    @NoArgsConstructor
    public static class Server {
        private int port;
    }
}
