package net.tbaas.telegrambot;

import lombok.Getter;
import net.tbaas.telegrambot.logger.LogLevel;
import net.tbaas.telegrambot.logger.Logger;
import net.tbaas.telegrambot.managers.CommandManager;
import pro.zackpollard.telegrambot.api.TelegramBot;

/**
 * @author Zack Pollard
 */
public class TBaaSBot {

    public static final String API_URL = "https://tbaas.vil.so/api/v1/";
    public static String API_KEY = "";

    @Getter
    private static TBaaSBot instance;

    @Getter
    private final TelegramBot telegramBot;
    @Getter
    private final CommandManager commandManager;

    public static final void main(String[] args) {

        if(args.length == 2) {
            API_KEY = args[1];
            new TBaaSBot(args[0]);
        } else {
            Logger.log(LogLevel.CRITICAL, "Two arguments must be provided at startup, the TG bot API key followed by the TBaaS API key.");
            System.exit(1);
        }
    }

    public TBaaSBot(String botKey) {

        instance = this;
        this.telegramBot = TelegramBot.login(botKey);
        this.commandManager = new CommandManager();

        this.telegramBot.startUpdates(true);
    }
}
