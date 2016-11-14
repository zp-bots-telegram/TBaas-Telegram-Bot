package net.tbaas.telegrambot;

import lombok.Getter;
import net.tbaas.telegrambot.managers.CommandManager;
import pro.zackpollard.telegrambot.api.TelegramBot;

/**
 * @author Zack Pollard
 */
public class TBaaSBot {

    @Getter
    private static TBaaSBot instance;

    @Getter
    private final TelegramBot telegramBot;
    @Getter
    private final CommandManager commandManager;

    public static final void main(String[] args) {

        new TBaaSBot(args[0]);
    }

    public TBaaSBot(String botKey) {

        instance = this;
        this.telegramBot = TelegramBot.login(botKey);
        this.commandManager = new CommandManager();
    }
}
