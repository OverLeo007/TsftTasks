package ru.shift.task6.client;

import java.io.Closeable;
import java.io.IOException;
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
    // TODO: Разобраться почему не отображаются джойны и ливы
    // TODO: Сделать ввод айпи и никнейма по энтеру

    private final ResourceManager resourceManager = new ResourceManager();

    public void start() {
        initTheme();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Завершение приложения...");
            resourceManager.close();
        }));

        initConnectionWindow();
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
        nicknameWindow.setOnCloseAction(onClose(client));
        nicknameWindow.setVisible(true);
    }

    private void initChatWindow(SocketClient client) {
        val chatWindow = new ChatWindowImpl();
        val chatPresenter = new ChatPresenter(chatWindow, client);

        chatWindow.addOnMessageListener(chatPresenter::sendMessage);
        chatPresenter.joinChat();
        chatWindow.setOnCloseAction(onClose(client));
    }

    private void initTheme() {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
        } catch (Exception e) {
            log.error("Error setting look and feel", e);
        }
    }

    private Runnable onClose(Closeable resource) {
        return () -> {
            try {
                resource.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}