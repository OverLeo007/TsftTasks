package ru.shift.task6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientMain {
    public static final int PORT = 8899;

    public static void main(String[] args) {
        log.info("Client started");
        try (Socket socket = new Socket("localhost", PORT)) {
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                log.info("Received: {}", line);
            }
        } catch (SocketException e) {
            log.info("Server disconnected");
        } catch (IOException e) {
            log.error("Error in client", e);
        } finally {
            log.info("Client stopped");
        }
    }
}
