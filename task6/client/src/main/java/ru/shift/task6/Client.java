package ru.shift.task6;

import javax.swing.JFrame;
import javax.swing.UIManager;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.ui.ConnectWindow;

@Slf4j
public class Client {

    public static void main(String[] args) {

        try {
            // Здесь ставишь нужную тему
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
            // Или любую другую, которую выбрал в JFormDesigner
        } catch (Exception e) {
            log.error("Error setting look and feel", e);
        }
        ConnectWindow connectWindow = new ConnectWindow();
        connectWindow.setVisible(true);
    }
}