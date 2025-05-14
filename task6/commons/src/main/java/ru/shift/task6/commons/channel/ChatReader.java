package ru.shift.task6.commons.channel;

import java.io.IOException;

public interface ChatReader {
    String readline() throws IOException;
}
