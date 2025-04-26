package ru.shift.task6;

import javax.swing.JFrame;
import javax.swing.UIManager;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.ui.ChatWindow;
import ru.shift.task6.ui.ConnectWindow;
import ru.shift.task6.ui.NicknameWindow;

@Slf4j
public class Client {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
        } catch (Exception e) {
            log.error("Error setting look and feel", e);
        }
//        ConnectWindow connectWindow = new ConnectWindow();
//        connectWindow.setVisible(true);
        ChatWindow chatWindow = new ChatWindow();
        chatWindow.setVisible(true);

    }
}