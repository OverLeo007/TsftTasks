package ru.shift;


import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.config.Config;
import ru.shift.config.properties.RunProperties;
import ru.shift.exceptions.ConfigurationLoadException;

@Slf4j
public class Main {

    public static void main(String[] args) {
        val properties = loadConfig(args);

        Storage storage = new Storage(properties.getStorage().getSize());
        runProducers(properties, storage);
        runConsumers(properties, storage);
    }

    private static void runProducers(RunProperties config, Storage storage) {
        val producerProperties = config.getProducer();
        val produceTime = producerProperties.getTime();
        val producerCount = producerProperties.getCount();

        for (int i = 0; i < producerCount; i++) {
            val producer = new Producer(produceTime, storage);
            new Thread(producer, producer.getId()).start();
        }
    }

    private static void runConsumers(RunProperties config, Storage storage) {
        val consumerProperties = config.getConsumer();
        val consumeTime = consumerProperties.getTime();
        val consumerCount = consumerProperties.getCount();

        for (int i = 0; i < consumerCount; i++) {
            val consumer = new Consumer(consumeTime, storage);
            new Thread(consumer, consumer.getId()).start();
        }
    }

    private static RunProperties loadConfig(String[] args) {
        String profile = args.length > 0 ? args[0] : Config.DEFAULT_PROFILE;
        try {
            return Config.loadProfile(profile);
        } catch (ConfigurationLoadException e) {
            log.error("Ошибка загрузки конфигурации: {}", e.getMessage());
            System.exit(1);
            throw new IllegalStateException("Unreachable");
        } catch (Exception e) {
            log.error("Неизвестная ошибка: ", e);
            System.exit(1);
            throw new IllegalStateException("Unreachable");
        }
    }
}

