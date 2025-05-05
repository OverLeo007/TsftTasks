package ru.shift.task6.client;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.shift.task6.client.presenter.ChatPresenter;
import ru.shift.task6.client.presenter.ConnectionPresenter;
import ru.shift.task6.client.presenter.NicknamePresenter;
import ru.shift.task6.client.socket.SocketClient;
import ru.shift.task6.client.view.windowImpl.ChatWindowImpl;
import ru.shift.task6.client.view.windowImpl.ConnectionWindowImpl;
import ru.shift.task6.client.view.windowImpl.NicknameWindowImpl;

@Slf4j
public class Client {
    private final ResourceManager resourceManager = new ResourceManager();

    public void start() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Завершение приложения...");
            resourceManager.close();
        }));

        SwingUtilities.invokeLater(() -> {
                    initTheme();
                    initConnectionWindow();
                }
        );

    }

    private void initConnectionWindow() {
        val connectionWindow = new ConnectionWindowImpl();
        val connectionPresenter = new ConnectionPresenter(connectionWindow, client -> {

            resourceManager.register(client);
            connectionWindow.dispose();

            initNicknameWindow(client);
        });

        connectionWindow.setOkButtonListener(connectionPresenter::tryToConnect);
        connectionWindow.setVisible(true);
    }

    private void initNicknameWindow(SocketClient client) {
        val nicknameWindow = new NicknameWindowImpl();
        val nicknamePresenter = new NicknamePresenter(nicknameWindow, client, () -> {

            nicknameWindow.dispose();

            initChatWindow(client);
        });

        nicknameWindow.setOkButtonListener(nicknamePresenter::tryToAuth);
        nicknameWindow.setOnCloseAction(onClose());
        nicknameWindow.setVisible(true);
    }

    private void initChatWindow(SocketClient client) {
        val chatWindow = new ChatWindowImpl();
        val chatPresenter = new ChatPresenter(chatWindow, client);

        chatWindow.addOnMessageListener(chatPresenter::sendMessage);
        chatPresenter.joinChat();
        chatWindow.setOnCloseAction(onClose());
    }

    private void initTheme() {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
        } catch (Exception e) {
            log.error("Error setting look and feel", e);
        }
    }

    private Runnable onClose() {
        return resourceManager::close;
    }
}