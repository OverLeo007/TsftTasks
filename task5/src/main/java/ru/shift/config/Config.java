package ru.shift.config;

import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.yaml.snakeyaml.Yaml;
import ru.shift.config.properties.RunProperties;
import ru.shift.exceptions.ConfigurationLoadException;
import ru.shift.exceptions.PropertiesArgumentException;

@Slf4j
public class Config {

    private static RunProperties runProperties;

    private Config() {
        // Prevent instantiation
    }

    public static void loadProfile(String profile) {
        log.info("Загрузка конфигурации для профиля: {}", profile);
        String fileName = "config/" + profile + ".yaml";
        Yaml yaml = new Yaml();
        try (InputStream inputStream = Config.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new ConfigurationLoadException(
                        "Конфигурационный файл не найден: " + fileName);
            }
            runProperties = yaml.loadAs(inputStream, RunProperties.class);
            if (runProperties == null) {
                throw new ConfigurationLoadException(
                        "Ошибка при загрузке конфигурации из файла: " + fileName);
            }
            validateProperties(runProperties);

        } catch (ConfigurationLoadException e) {
            log.error("Во время получения конфигурации из файла произошла ошибка: {}",
                    e.getMessage());
            System.exit(3);
        } catch (PropertiesArgumentException e) {
            log.error("Некорректное значение в конфигурации: {}", e.getMessage());
            System.exit(2);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при загрузке конфигурации: {}", e.getMessage());
            System.exit(1);
        }
    }

    public static RunProperties getRunConfig() {
        if (runProperties == null) {
            log.warn("Профиль не выбран. Используется профиль по умолчанию.\n"
                    + "Для выбора профиля укажите имя файла конфигурации без расширения"
                    + " в качестве аргумента.");

            loadProfile("default");
        }
        return runProperties;
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
