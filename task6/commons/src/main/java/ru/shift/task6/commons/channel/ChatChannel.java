package ru.shift.task6.commons.channel;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import ru.shift.task6.commons.JsonSerializer;
import ru.shift.task6.commons.exceptions.DeserializationException;
import ru.shift.task6.commons.exceptions.SerializationException;
import ru.shift.task6.commons.exceptions.SocketConnectionException;
import ru.shift.task6.commons.models.Envelope;
import ru.shift.task6.commons.models.payload.Payload;

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
    public Envelope<? extends Payload> readEnvelope() throws IOException, DeserializationException {
        return JsonSerializer.deserialize(reader.readLine());
    }

    @Override
    public void sendEnvelope(Envelope<?> envelope) throws SerializationException {
        writer.println(JsonSerializer.serialize(envelope));
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
