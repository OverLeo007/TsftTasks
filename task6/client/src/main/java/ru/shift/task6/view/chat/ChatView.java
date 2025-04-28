package ru.shift.task6.view.chat;

import java.time.Instant;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import ru.shift.task6.models.Message;
import ru.shift.task6.models.MessageType;
import ru.shift.task6.models.User;
import ru.shift.task6.presenter.ChatPresenter;

@Slf4j
public class ChatView {
    private ChatWindow chatWindow;

    public void setPresenter(ChatPresenter presenter) {
        this.chatWindow = new ChatWindow(presenter);
    }

    public void addMessage(Message message) {
        chatWindow.appendMessage(message);
    }

    public void joinUser(User user) {
        chatWindow.addUser(user);
    }

    public void leaveUser(User user) {
        chatWindow.removeUser(user);
    }

    public void onDisconnect(Message message) {
        chatWindow.onDisconnect(message.text());
    }

    private void setDemo(ChatWindow chatWindow) {
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            Instant loginTime = Instant.now().minusSeconds(random.nextInt(300));
            chatWindow.addUser(new User("Пользователь " + i, loginTime));
        }
        Instant loginTime = Instant.now().minusSeconds(random.nextInt(300));
        chatWindow.addUser(new User("это 20  символов имя", loginTime));

        chatWindow.appendMessage(new Message(new User("Alex", Instant.now()), MessageType.JOIN, "", Instant.now()));
        chatWindow.appendMessage(new Message(new User("Alex", Instant.now()), MessageType.TEXT, "Привет всем!", Instant.now()));
        chatWindow.appendMessage(new Message(new User("Alex", Instant.now()), MessageType.TEXT, """
                [Verse 1]
                It’s my status as a toilet…
                Brr Skibidi Dop Dop Dop Yes Yes
                Shtibidi Dopidim Dip Dib
                Stibidi Dop Dop Dop Yes Yes
                Shtibidi Dopidim Dip Dib
                Skibidi Dop Dop Dop Yes Yes
                Shtibidi Dopidim Dip Dib
                Stibidi Dop Dop Dop Yes Yes
                Shtibidi Dopidim Dip
                My status is addictive…
                
                [Verse 2]
                Cameras, toilets, fighting in an holy war
                Every battle more insane than before
                Raise the stakes more and more
                Get revenge, we’ll settle the score
                
                [Verse 3]
                It’s my porcelain status…
                Brr Stibidi Dop Dop Dop Yes Yes
                Shtibidi Dopidim Dip Dib
                Skibidi Dop Dop Dop Yes Yes
                Shtibidi Dopidim Dip Dib
                Stibidi Dop Dop Dop Yes Yes
                Shtibidi Dopidim Dip Dib
                Skibidi Dop Dop Dop Yes Yes
                Shtibidi Dopidim Dip Dib""", Instant.now()));
        chatWindow.appendMessage(new Message(new User("Alex", Instant.now()), MessageType.TEXT, "Tralalelo Tralala was allied with the crocos in both Croco-Avian wars. However, he couldn't contribute much because of the aerial nature of most of their enemies. He did take out one important enemy commander in the first war, which greatly helped the crocos win. Rumor states that in the second war, Tralelo will save the crocos from imminent defeat by assassinating Bombombini Gusini by climbing into his engines. However, this was anticipated and Tralalero was caught by a group of Bengali Fishermen (led by Roshogulla Hasimollah) over the pre-planned ocean route Bombombini Gusini would intentionally fly. He was then incinerated by Rantasanta Chinaranta and consumed by the fishermen. There is rumors of the Cybertruck Assassins Involvment in Tralalero tralala's success in the second war. But all information on the matter has long been redacted. There were, however, reported sightings of Piccione Macchina in the skies that day, boosting the credibility of the theory, claiming some sort of cooperation between the Cybertruck Assassins and Tralalero Tralala took place that day.", Instant.now()));
        chatWindow.appendMessage(new Message(new User("Alex", Instant.now()), MessageType.LEAVE, "", Instant.now()));
    }

    public void show(User user) {
        joinUser(user);
        chatWindow.runTimer();
        chatWindow.setVisible(true);
    }
}
