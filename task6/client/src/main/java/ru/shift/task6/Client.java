package ru.shift.task6;

import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.view.ChatView;

@Slf4j
public class Client {

    public static void main(String[] args) {
        var view = new ChatView();
        view.start();
    }
}