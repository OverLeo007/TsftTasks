package ru.shift.task6.view.components;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.nio.file.Paths;
import lombok.Getter;

public enum Icons {
    ERROR("error.png"),
    PENDING("pending.png"),
    SEND("send.png"),
    SUCCESS("success.png");

    private final FlatSVGIcon icon;
    private static final String ICONS_PATH = "icons/";

    Icons(String iconPath) {
        this.icon = new FlatSVGIcon(ICONS_PATH + iconPath);
    }

    public FlatSVGIcon icon() {
        return icon;
    }
}
