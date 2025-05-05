package ru.shift.task6.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.yaml.snakeyaml.Yaml;
import ru.shift.task6.server.exceptions.ConfigurationLoadException;
import ru.shift.task6.server.exceptions.PropertiesArgumentException;

@Slf4j
public class Config {

    public static final String FILENAME = "properties.yaml";
    private static final Path EXTERNAL_CONFIG_PATH = Path.of(FILENAME);

    public static RunProperties loadProperties() throws ConfigurationLoadException {
        ensureExternalConfigExists();

        try (InputStream externalStream = Files.newInputStream(EXTERNAL_CONFIG_PATH)) {
            log.info("Загружаем конфигурацию из внешнего файла: {}",
                    EXTERNAL_CONFIG_PATH.toAbsolutePath());
            Yaml yaml = new Yaml();
            RunProperties loadedProperties = yaml.loadAs(externalStream, RunProperties.class);
            if (loadedProperties == null) {
                throw new ConfigurationLoadException(
                        "Файл конфигурации пустой: " + EXTERNAL_CONFIG_PATH);
            }
            validateProperties(loadedProperties);
            return loadedProperties;
        } catch (Exception e) {
            log.warn("""
                    Внешний конфигурационный файл повреждён или некорректен:
                    {}.
                    Будет использована встроенная конфигурация.""", e.getMessage());

            try (InputStream internalStream = Config.class.getClassLoader()
                    .getResourceAsStream(FILENAME)) {
                if (internalStream == null) {
                    throw new ConfigurationLoadException("Встроенный файл конфигурации не найден.");
                }
                Yaml yaml = new Yaml();
                RunProperties fallbackProperties = yaml.loadAs(internalStream, RunProperties.class);
                if (fallbackProperties == null) {
                    throw new ConfigurationLoadException("Встроенный файл конфигурации пустой.");
                }
                validateProperties(fallbackProperties);
                return fallbackProperties;
            } catch (IOException ex) {
                throw new ConfigurationLoadException("Ошибка при загрузке встроенной конфигурации",
                        ex);
            }
        }
    }

    private static void ensureExternalConfigExists() throws ConfigurationLoadException {
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
            } catch (IOException e) {
                throw new ConfigurationLoadException("Не удалось создать внешний файл конфигурации",
                        e);
            }
        }
    }

    private static void validateProperties(RunProperties properties) {
        val server = properties.getServer();
        if (server.getPort() <= 1024 || server.getPort() > 65535) {
            throw new PropertiesArgumentException("Некорректный номер порта: " + server.getPort() +
                    ". Порт должен быть в диапазоне от 1025 до 65535");
        }
    }
}

