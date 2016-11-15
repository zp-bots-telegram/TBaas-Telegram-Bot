package net.tbaas.telegrambot.managers;

import net.tbaas.telegrambot.TBaaSBot;
import net.tbaas.telegrambot.commands.Command;
import net.tbaas.telegrambot.logger.LogLevel;
import net.tbaas.telegrambot.logger.Logger;
import org.reflections.Reflections;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Zack Pollard
 */
public class CommandManager implements Listener {

    private final TBaaSBot instance;
    private final TelegramBot telegramBot;
    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandManager() {

        this.instance = TBaaSBot.getInstance();
        this.telegramBot = instance.getTelegramBot();

        this.loadAllCommands();

        telegramBot.getEventsManager().register(this);
    }

    public void loadAllCommands() {

        Reflections reflections = new Reflections("net.tbaas.telegrambot.commands");
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);

        for(Class<? extends Command> myClass : classes) {

            try {
                Command command = myClass.newInstance();

                if(commands.put(command.getName(), command) != null) {

                    Logger.log(LogLevel.CRITICAL, "Error whilst registering command " + command.getName() + ". Command name was already registered by another command.");
                    System.exit(1);
                }

                for(String commandAlias : command.getAliases()) {

                    if(commands.put(commandAlias, command) != null) {

                        Logger.log(LogLevel.CRITICAL, "Error whilst registering command " + command.getName() + ". The alias '" + commandAlias + "' was already registered by another command.");
                        System.exit(1);
                    }
                }
            } catch (InstantiationException | IllegalAccessException e) {
                Logger.log(LogLevel.CRITICAL, "Error whilst loading command " + myClass.getSimpleName() + ".", e);
                System.exit(0);
            }
        }
    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {

        Command command = commands.get(event.getCommand());

        if(command != null) {
            command.execute(event);
        }
    }
}
