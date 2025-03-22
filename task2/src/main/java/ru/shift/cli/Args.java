package ru.shift.cli;

public record Args(
        String inputFilePath,
        String outputFilePath,
        boolean consoleOutput
) {}
