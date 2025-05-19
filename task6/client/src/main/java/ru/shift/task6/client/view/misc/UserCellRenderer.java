package ru.shift.task6.client.view.misc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.alt.commons.protocol.UserInfo;


@Slf4j
public class UserCellRenderer extends JLabel implements ListCellRenderer<UserInfo> {
    @Override
    public Component getListCellRendererComponent(JList<? extends UserInfo> list, UserInfo user, int index,
            boolean isSelected, boolean cellHasFocus) {
        setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        setOpaque(true);
        setBackground(new Color(0x3c3f41));
        setForeground(list.getForeground());

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime loginLocal = user.getLogTime().atZone(ZoneId.systemDefault());
        Duration onlineDuration = Duration.between(loginLocal, now);

        String onlineText = formatDuration(onlineDuration);

        setText("%-24s %s".formatted(user.getNickname(), onlineText));
        return this;
    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        if (seconds < 10) {
            return "только что";
        } else if (seconds < 60) {
            return seconds + " s";
        }

        long minutes = duration.toMinutes();
        long hours = duration.toHours();

        if (hours > 5) {
            return hours + " ч";
        } else if (hours > 0) {
            long minutesPart = duration.toMinutesPart();
            return String.format("%d h %d m", hours, minutesPart);
        } else {
            long secondsPart = duration.toSecondsPart();
            return String.format("%d m %d s", minutes, secondsPart);
        }
    }

}

