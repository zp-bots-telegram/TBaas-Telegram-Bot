package net.tbaas.telegrambot.commands;

import net.tbaas.telegrambot.conversations.AddBotConversation;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * @author Zack Pollard
 */
public class AddBotCommand extends Command {

    public AddBotCommand() {

        super("addbot", "add");
    }

    @Override
    public boolean execute(CommandMessageReceivedEvent event) {

        new AddBotConversation(event.getChat(), event.getMessage().getSender()).run();
        return true;
    }
}
