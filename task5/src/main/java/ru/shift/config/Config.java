package ru.shift.config;

import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.ConstructorException;
import org.yaml.snakeyaml.parser.ParserException;
import ru.shift.config.properties.RunProperties;
import ru.shift.exceptions.ConfigurationLoadException;
import ru.shift.exceptions.PropertiesArgumentException;

@Slf4j
public class Config {

    public static final String DEFAULT_PROFILE = "default";

    private Config() {
        // Prevent instantiation
    }

    public static RunProperties loadProfile(String profile) throws ConfigurationLoadException {
        log.info("Загрузка конфигурации для профиля: {}", profile);
        String fileName = "config/" + profile + ".yaml";
        Yaml yaml = new Yaml();
        try (InputStream inputStream = Config.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new ConfigurationLoadException(
                        "Конфигурационный файл не найден: " + fileName);
            }
            RunProperties loadedProperties = yaml.loadAs(inputStream, RunProperties.class);
            if (loadedProperties == null) {
                throw new ConfigurationLoadException(
                        "Ошибка при загрузке конфигурации из файла: " + fileName);
            }
            validateProperties(loadedProperties);
            return loadedProperties;
        } catch (PropertiesArgumentException e) {
            throw new ConfigurationLoadException("Некорректное значение в файле конфигурации: " + e.getMessage(), e);
        } catch (ParserException | ConstructorException e) {
            throw new ConfigurationLoadException("Ошибка парсинга конфигурационного файла: " + fileName, e);
        } catch (IOException e) {
            throw new ConfigurationLoadException("Ошибка чтения конфигурационного файла: " + fileName, e);
        }
    }


    private static void validateProperties(RunProperties properties) {
        val producer = properties.getProducer();
        val consumer = properties.getConsumer();
        val storage = properties.getStorage();

        if (producer.getCount() <= 0) {
            throw new PropertiesArgumentException("Количество производителей должно быть больше 0");
        }
        if (producer.getTime() <= 0) {
            throw new PropertiesArgumentException("Время производства должно быть больше 0");
        }
        if (consumer.getCount() <= 0) {
            throw new PropertiesArgumentException("Количество потребителей должно быть больше 0");
        }
        if (consumer.getTime() <= 0) {
            throw new PropertiesArgumentException("Время потребления должно быть больше 0");
        }
        if (storage.getSize() <= 0) {
            throw new PropertiesArgumentException("Размер склада должен быть больше 0");
        }
    }
}
