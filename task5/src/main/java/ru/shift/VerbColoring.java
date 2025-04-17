package ru.shift;

import java.util.Map;

public final class VerbColoring {

    private VerbColoring() {
        // Prevent instantiation
        throw new UnsupportedOperationException("Utility class");
    }

    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[34m";

    private static final String GREEN_BG = "\u001B[30;42m";
    private static final String YELLOW_BG = "\u001B[30;43m";
    private static final String BLUE_BG = "\u001B[30;44m";
    private static final String ORANGE_BG = "\u001B[30;48;5;214m";
    private static final String PURPLE_BG = "\u001B[30;45m";
    private static final String RED_BG = "\u001B[30;41m";

    private static final Map<String, String> PV = Map.of(
            "начал", GREEN_BG,
            "производит", YELLOW_BG,
            "произвел", BLUE_BG,
            "хочет положить", ORANGE_BG,
            "ожидает", PURPLE_BG,
            "положил", RED_BG
    );

    private static final Map<String, String> CV = Map.of(
            "начал", GREEN_BG,
            "потребляет", BLUE_BG,
            "потребил", RED_BG,
            "хочет забрать", PURPLE_BG,
            "ожидает", ORANGE_BG,
            "взял", YELLOW_BG
    );

    private static final boolean IS_COLORED = System.getProperty("colored") != null;

    public static String getPV(String verb) {
        if (IS_COLORED) {
            var coloring = PV.get(verb);
            if (coloring != null) {
                return coloring + verb + RESET + CYAN;
            }
        }
        return verb;
    }

    public static String getCV(String verb) {
        if (IS_COLORED) {
            var coloring = CV.get(verb);
            if (coloring != null) {
                return coloring + verb + RESET + CYAN;
            }
        }
        return verb;
    }
}
