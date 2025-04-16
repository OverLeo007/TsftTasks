package ru.shift.config;

import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import ru.shift.config.dto.RunConfig;

@Slf4j
public class Config {

    private static RunConfig runConfig;

    public static void loadProfile(String profile) {
        log.info("Загрузка конфигурации для профиля: {}", profile);
        String fileName = "config/" + profile + ".yaml";
        Yaml yaml = new Yaml();
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RuntimeException("Конфигурационный файл не найден: " + fileName);
            }
            runConfig = yaml.loadAs(inputStream, RunConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке конфигурации", e);
        }
    }

    public static RunConfig getRunConfig() {
        if (runConfig == null) {
            log.warn("Профиль не выбран. Используется профиль по умолчанию.\n"
                    + "Для выбора профиля укажите имя файла конфигурации без расширения"
                    + " в качестве аргумента.");

            loadProfile("default");
        }
        return runConfig;
    }
}
