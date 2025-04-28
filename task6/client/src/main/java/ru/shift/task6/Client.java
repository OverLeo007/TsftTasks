package ru.shift.task6;

import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.socket.SocketConnection;
import ru.shift.task6.presenter.Presenter;
import ru.shift.task6.view.ThemeInitializer;
import ru.shift.task6.view.View;

@Slf4j
public class Client {

    public static void main(String[] args) {
        ThemeInitializer.init();
        var connection = new SocketConnection();
        var view = new View();
        new Presenter(view, connection);
        view.start();
    }
}