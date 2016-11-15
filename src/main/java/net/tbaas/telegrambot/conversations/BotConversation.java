package net.tbaas.telegrambot.conversations;

import lombok.Getter;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.user.User;

/**
 * @author Zack Pollard
 */
public abstract class BotConversation {

    @Getter
    private final Chat chat;
    @Getter
    private final User user;

    public BotConversation(Chat chat, User user) {

        this.chat = chat;
        this.user = user;
    }

    public abstract boolean run();
}
