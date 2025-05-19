package ru.shift.task6.client.view.misc;

public enum Icons {
    ERROR("error.svg"),
    PENDING("pending.svg"),
    SEND("send.svg"),
    SUCCESS("success.svg");

    private final String iconPath;
    private static final String ICONS_PATH = "icons/";

    Icons(String iconName) {
        this.iconPath = ICONS_PATH + iconName;
    }

    public String icon() {
        return iconPath;
    }
}
