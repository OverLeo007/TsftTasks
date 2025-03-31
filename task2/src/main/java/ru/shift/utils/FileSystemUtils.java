package ru.shift.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class FileSystemUtils {

    private FileSystemUtils() {
        throw new UnsupportedOperationException("Класс утилит не может быть создан");
    }

    public static void createDirForFileIfNotExist(String filePath) throws IOException {
        if (filePath == null) {
            return;
        }
        Path outPath = Path.of(filePath);
        Path dir = outPath.getParent();
        if (dir != null && !Files.exists(dir)) {
            log.debug("Попытка создания директории для файла {}", filePath);
            Files.createDirectories(dir);
            log.debug("Создана директория {} для файла {}", dir, outPath.getFileName());
        }
    }

}
