package ru.shift.task6.commons.channel;

import ru.shift.task6.commons.exceptions.SocketConnectionException;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatChannel implements Closeable, ChatReader, ChatWriter {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public ChatChannel(Socket socket) throws SocketConnectionException {
        this.socket = socket;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        } catch (IOException e) {
            throw new SocketConnectionException("Error while connection creation", e);
        }

    }

    @Override
    public String readline() throws IOException {
        return reader.readLine();
    }

    @Override
    public void printLine(String line) {
        writer.println(line);
    }

    @Override
    public boolean checkReaderError() {
        return writer.checkError();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    @Override
    public String toString() {
        return "ChatChannel{" +
                "socket=" + socket +
                '}';
    }
}
