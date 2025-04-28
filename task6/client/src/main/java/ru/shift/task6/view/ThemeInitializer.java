package ru.shift.task6.view;

import javax.swing.UIManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThemeInitializer {
    public static void init() {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
        } catch (Exception e) {
            log.error("Error setting look and feel", e);
        }
    }
}
