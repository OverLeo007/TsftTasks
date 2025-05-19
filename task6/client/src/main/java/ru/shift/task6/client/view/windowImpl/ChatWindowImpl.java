package ru.shift.task6.client.view.windowImpl;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.DefaultListModel;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.alt.commons.protocol.ChatMessage;
import ru.shift.task6.alt.commons.protocol.UserInfo;
import ru.shift.task6.client.view.designer.ChatWindow;

@Slf4j
public class ChatWindowImpl extends ChatWindow {

    private final DefaultListModel<UserInfo> userModel = new DefaultListModel<>();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault());

    {
        this.usersList.setModel(userModel);
    }

    public void addOnMessageListener(Consumer<String> onMessageSent) {
        ActionListener sendAction = e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                onMessageSent.accept(message);
                messageField.setText("");
            }
        };

        sendButton.addActionListener(sendAction);
        messageField.addActionListener(sendAction);
    }

    public void run() {
        runTimer();
        setVisible(true);
    }

    public void appendMessage(ChatMessage message) {
        StyledDocument doc = chatArea.getStyledDocument();
        try {
            String formattedTime = formatTime(message.getSendTime());
            String header = "<" + message.getSender().getNickname() + ":" + formattedTime + "> ";
            doc.insertString(doc.getLength(), header, doc.getStyle("time"));
            doc.insertString(doc.getLength(), "- " + message.getBody() + "\n",
                    doc.getStyle("regular"));
        } catch (BadLocationException e) {
            log.error("Error inserting message into chat area", e);
        }
    }

    public void onJoin(UserInfo user) {
        appendCenter(user.getNickname() + " зашел в чат");
        addUser(user);
    }

    public void onLeave(UserInfo user) {
        appendCenter(user.getNickname() + " вышел из чата");
        removeUser(user);
    }

    private void appendCenter(String text) {
        StyledDocument doc = chatArea.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text + "\n", doc.getStyle("regular"));
            doc.setParagraphAttributes(doc.getLength() - text.length(), text.length(),
                    doc.getStyle("center"), true);
        } catch (BadLocationException e) {
            log.error("Error inserting message into chat area", e);
        }
    }

    private String formatTime(Instant instant) {
        return formatter.format(instant);
    }

    private void addUser(UserInfo user) {
        userModel.addElement(user);
    }

    private void removeUser(UserInfo user) {
        userModel.removeElement(user);
    }

    public void updateSidebar(List<UserInfo> users) {
        userModel.removeAllElements();
        userModel.addAll(users);
    }

    public void runTimer() {
        Timer timer = new Timer(2000, e -> usersList.repaint());
        timer.start();
    }

    public void onDisconnect(String reason) {

            log.debug("setting up shutdown notice {}", reason);
            chatErrorLabel.setText(reason);
            chatErrorLabel.setVisible(true);
            messageField.setEnabled(false);
            sendButton.setEnabled(false);
    }

    public void setOnCloseAction(Runnable action) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                action.run();
                dispose();
            }
        });
    }

}
