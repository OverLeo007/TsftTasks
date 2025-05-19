package ru.shift.task6.commons.channel;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.commons.JsonSerializer;
import ru.shift.task6.commons.exceptions.ProtocolException;
import ru.shift.task6.commons.protocol.abstracts.Message;

@Slf4j
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
    public Message readMessage() throws IOException, ProtocolException {
        var line = reader.readLine();
        log.trace("Reading message: {}", line);
        return JsonSerializer.deserialize(line);
    }

    @Override
    public void sendMessage(Message message) throws ProtocolException {
        var line = JsonSerializer.serialize(message);
        log.trace("Sending message: {}", line);
        writer.println(line);
    }

    @Override
    public boolean checkWriterError() {
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
                "socket=" + socket.getInetAddress() + ":" + socket.getPort() +
                '}';
    }
}
