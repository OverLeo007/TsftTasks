package ru.shift;


import lombok.extern.slf4j.Slf4j;
import ru.shift.config.Config;
import ru.shift.config.properties.RunProperties;
import ru.shift.exceptions.ConfigurationLoadException;

@Slf4j
public class Main {

    public static void main(String[] args) {
        loadConfig(args);

        Storage storage = new Storage(Config.getRunConfig().getStorage().getSize());
        runProducers(Config.getRunConfig(), storage);
        runConsumers(Config.getRunConfig(), storage);
    }

    private static void runProducers(RunProperties config, Storage storage) {
        var producerProperties = config.getProducer();
        var produceTime = producerProperties.getTime();
        var producerCount = producerProperties.getCount();

        for (int i = 0; i < producerCount; i++) {
            var producer = new Producer(produceTime, storage);
            new Thread(producer, producer.getId()).start();
        }
    }

    private static void runConsumers(RunProperties config, Storage storage) {
        var consumerProperties = config.getConsumer();
        var consumeTime = consumerProperties.getTime();
        var consumerCount = consumerProperties.getCount();

        for (int i = 0; i < consumerCount; i++) {
            var consumer = new Consumer(consumeTime, storage);
            new Thread(consumer, consumer.getId()).start();
        }
    }

    private static void loadConfig(String[] args) {
        if (args.length > 0) {
            String profile = args[0];
            try {
                Config.loadProfile(profile);
            } catch (ConfigurationLoadException e) {
                log.error("Ошибка загрузки конфигурации: {}", e.getMessage());
                System.exit(1);
            }
        }
    }
}

