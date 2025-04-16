package ru.shift;


import lombok.extern.slf4j.Slf4j;
import ru.shift.config.Config;

@Slf4j
public class Main {

    public static void main(String[] args) {
        loadConfig(args);

        log.info(String.valueOf(Config.getRunConfig()));
    }

    private static void loadConfig(String[] args) {
        if (args.length > 0) {
            String profile = args[0];
            Config.loadProfile(profile);
        }
    }
}