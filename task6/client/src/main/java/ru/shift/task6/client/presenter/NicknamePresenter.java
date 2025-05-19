package ru.shift.task6.client.presenter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.client.socket.SocketClient;
import ru.shift.task6.client.view.windowImpl.NicknameWindowImpl;

@RequiredArgsConstructor
@Slf4j
public class NicknamePresenter {

    private final NicknameWindowImpl window;
    private final SocketClient client;
    private final Runnable onFinish;


    public void tryToAuth(String nickname) {
        client.sendAuthRequest(
                nickname,
                success -> {
                    window.onNicknameSuccess();
                    onFinish.run();
                },
                window::showNicknameError
        );
    }
}
