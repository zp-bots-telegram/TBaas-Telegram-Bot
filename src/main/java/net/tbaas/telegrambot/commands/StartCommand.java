package net.tbaas.telegrambot.commands;

import net.tbaas.telegrambot.TBaaSBot;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * @author Zack Pollard
 */
public class StartCommand extends Command {

    public StartCommand() {

        super("start");
    }

    @Override
    public boolean execute(CommandMessageReceivedEvent event) {

        TBaaSBot.getInstance().getTelegramBot().sendMessage(event.getChat(), SendableTextMessage.plain("Hi there friend, welcome to the TBaaS beta! Check the command list for available commands that you can use!").build());
        return true;
    }
}