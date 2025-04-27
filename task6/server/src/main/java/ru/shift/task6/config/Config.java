package ru.shift.task6.config;

import java.io.IOException;
import java.io.InputStream;
import lombok.val;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.ConstructorException;
import org.yaml.snakeyaml.parser.ParserException;
import ru.shift.task6.exceptions.ConfigurationLoadException;
import ru.shift.task6.exceptions.PropertiesArgumentException;

public class Config {

    public static final String FILENAME = "properties.yaml";

    public static RunProperties loadProperties() throws ConfigurationLoadException {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = Config.class.getClassLoader()
                .getResourceAsStream(FILENAME)) {
            if (inputStream == null) {
                throw new ConfigurationLoadException(
                        "Конфигурационный файл не найден: " + FILENAME);
            }
            RunProperties loadedProperties = yaml.loadAs(inputStream, RunProperties.class);
            if (loadedProperties == null) {
                throw new ConfigurationLoadException(
                        "Ошибка при загрузке конфигурации из файла: " + FILENAME);
            }
            validateProperties(loadedProperties);
            return loadedProperties;

        } catch (PropertiesArgumentException e) {
            throw new ConfigurationLoadException(
                    "Некорректное значение в файле конфигурации: " + e.getMessage(), e);
        } catch (ParserException | ConstructorException e) {
            throw new ConfigurationLoadException(
                    "Ошибка парсинга конфигурационного файла: " + FILENAME, e);
        } catch (IOException e) {
            throw new ConfigurationLoadException(
                    "Ошибка чтения конфигурационного файла: " + FILENAME, e);
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
