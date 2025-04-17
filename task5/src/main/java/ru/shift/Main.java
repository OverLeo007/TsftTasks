package ru.shift;


import lombok.extern.slf4j.Slf4j;
import ru.shift.config.Config;
import ru.shift.config.dto.RunConfig;

@Slf4j
public class Main {

    public static void main(String[] args) {
        loadConfig(args);

        Storage storage = new Storage(Config.getRunConfig().getStorage().getSize());
        runProducers(Config.getRunConfig(), storage);
        runConsumers(Config.getRunConfig(), storage);
    }

    private static void runProducers(RunConfig config, Storage storage) {
        var producerCfg = config.getProducer();
        var produceTime = producerCfg.getTime();
        var producerCount = producerCfg.getCount();

        for (int i = 0; i < producerCount; i++) {
            var producer = new Producer(produceTime, storage);
            new Thread(producer).start();
        }
    }

    private static void runConsumers(RunConfig config, Storage storage) {
        var consumerCfg = config.getConsumer();
        var consumeTime = consumerCfg.getTime();
        var consumerCount = consumerCfg.getCount();

        for (int i = 0; i < consumerCount; i++) {
            var consumer = new Consumer(consumeTime, storage);
            new Thread(consumer).start();
        }
    }

    private static void loadConfig(String[] args) {
        if (args.length > 0) {
            String profile = args[0];
            Config.loadProfile(profile);
        }
    }
}

