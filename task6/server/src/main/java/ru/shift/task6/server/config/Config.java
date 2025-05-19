package ru.shift.task6.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import ru.shift.task6.server.exceptions.ConfigurationLoadException;
import ru.shift.task6.server.exceptions.PropertiesArgumentException;

@Slf4j
public class Config {

    @Getter
    private enum FileType {
        INTERNAL("Встроенный"),
        EXTERNAL("Внешний");

        private final String fileTypeName;

        FileType(String fileTypeName) {
            this.fileTypeName = fileTypeName;
        }
    }

    public static final String FILENAME = "properties.yaml";
    private static final Path EXTERNAL_CONFIG_PATH = Path.of(FILENAME);

    public static RunProperties loadProperties() throws ConfigurationLoadException {

        try {
            ensureExternalConfigExists();
            try (InputStream inputStream = Files.newInputStream(EXTERNAL_CONFIG_PATH)) {
                return loadConfigFromStream(inputStream, FileType.EXTERNAL);
            }
        } catch (Exception e) {
            log.warn("""
                    Произошла проблема при попытке считать конфигурацию из внешнего файла:
                    {}.
                    Будет использована встроенная конфигурация.""", e.getMessage());

            try (InputStream internalStream = Config.class.getClassLoader()
                    .getResourceAsStream(FILENAME)) {
                return loadConfigFromStream(internalStream, FileType.INTERNAL);
            } catch (IOException ex) {
                throw new ConfigurationLoadException("Ошибка при загрузке встроенной конфигурации",
                        ex);
            }
        }
    }

    private static RunProperties loadConfigFromStream(InputStream inputStream, FileType fileType) {
        if (inputStream == null) {
            throw new ConfigurationLoadException(
                    fileType.getFileTypeName() + "файл конфигурации не найден.");
        }
        Yaml yaml = new Yaml();
        RunProperties loadedProperties = yaml.loadAs(inputStream, RunProperties.class);
        if (loadedProperties == null) {
            throw new ConfigurationLoadException(
                    fileType.getFileTypeName() + " файл конфигурации пустой.");
        }
        validateProperties(loadedProperties);
        return loadedProperties;
    }

    private static void ensureExternalConfigExists() throws IOException {
        if (!Files.exists(EXTERNAL_CONFIG_PATH)) {
            try (InputStream internalStream = Config.class.getClassLoader()
                    .getResourceAsStream(FILENAME)) {
                if (internalStream == null) {
                    throw new ConfigurationLoadException("Встроенный файл конфигурации не найден.");
                }
                Files.copy(internalStream, EXTERNAL_CONFIG_PATH);
                log.warn(
                        "Создан внешний файл конфигурации по умолчанию. Вы можете отредактировать его: {}",
                        EXTERNAL_CONFIG_PATH.toAbsolutePath());
            }
        }
    }

    private static void validateProperties(RunProperties properties) {
        final var server = properties.getServer();
        if (server.getPort() <= 1024 || server.getPort() > 65535) {
            throw new PropertiesArgumentException("Некорректный номер порта: " + server.getPort() +
                    ". Порт должен быть в диапазоне от 1025 до 65535");
        }
    }


}

