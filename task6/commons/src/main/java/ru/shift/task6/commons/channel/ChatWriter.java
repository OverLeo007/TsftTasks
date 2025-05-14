package ru.shift.task6.commons.channel;

public interface ChatWriter {
    void printLine(String line);

    boolean checkReaderError();
}
