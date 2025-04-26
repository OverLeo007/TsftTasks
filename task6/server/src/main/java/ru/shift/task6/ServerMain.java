package ru.shift.task6;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMain {

    public static final int PORT = 8899;

    private static final Collection<Socket> clients = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws IOException {
        log.info("Server started");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            new Thread(() -> {
                Random rnd = new Random();

                try {
                    while (true) {
                        List<Socket> deadClients = new ArrayList<>();

                        for (Socket client : clients) {
                            try {
                                if (client.isClosed()) {
                                    log.info("Client disconnected (closed)");
                                    deadClients.add(client);
                                    continue;
                                }

                                OutputStreamWriter writer = new OutputStreamWriter(
                                        client.getOutputStream(), StandardCharsets.UTF_8);
                                PrintWriter printWriter = new PrintWriter(writer, true);

                                printWriter.println("OK - " + rnd.nextInt());

                                // check for socket error
                                if (printWriter.checkError()) {
                                    log.info("Client disconnected (write error)");
                                    deadClients.add(client);
                                }
                            } catch (IOException e) {
                                log.info("Client disconnected with IOException");
                                deadClients.add(client);
                            }
                        }


                        clients.removeAll(deadClients);

                        Thread.sleep(1000);
                    }
                } catch (Exception ignored) {
                }
            }).start();

            while (true) {
                Socket socket = serverSocket.accept();
                log.info("Client connected");
                clients.add(socket);
            }
        }
    }
}
