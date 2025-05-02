package ru.shift.server.config;

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
