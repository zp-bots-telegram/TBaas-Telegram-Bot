package net.tbaas.telegrambot.commands;

import lombok.Getter;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * @author Zack Pollard
 */
public abstract class Command {

    @Getter
    private final String name;
    @Getter
    private final String[] aliases;

    public Command(String name, String... aliases) {

        this.name = name;
        this.aliases = aliases;
    }

    public abstract boolean execute(CommandMessageReceivedEvent event);
}