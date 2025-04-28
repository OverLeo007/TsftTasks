package ru.shift.task6.view.connection;


import ru.shift.task6.presenter.ConnectionPresenter;

public class ConnectionView {

    private ConnectWindow connectWindow;

    public void setPresenter(ConnectionPresenter presenter) {
        this.connectWindow = new ConnectWindow(presenter);
    }

    public void showAddressError(String message) {
        connectWindow.showAddressError(message);
    }

    public void showNicknameError(String message) {
        connectWindow.showNicknameError(message);
    }

    public void onAddressSuccess() {
        connectWindow.onAddressSuccess();
    }

    public void onNicknameSuccess() {
        connectWindow.onNicknameSuccess();
    }

    public void show() {
        connectWindow.setVisible(true);
    }

    public void dispose() {
        connectWindow.dispose();
    }
}
