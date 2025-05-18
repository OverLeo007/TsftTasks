package ru.shift.task6.alt.commons.channel;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import ru.shift.task6.alt.commons.JsonSerializer;
import ru.shift.task6.alt.commons.protocol.ProtocolException;
import ru.shift.task6.alt.commons.protocol.abstracts.Message;


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
    public <T extends Message> T readMessage() throws IOException, ProtocolException {
        return JsonSerializer.deserialize(reader.readLine());
    }

    @Override
    public void sendMessage(Message message) throws ProtocolException {
        writer.println(JsonSerializer.serialize(message));
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
