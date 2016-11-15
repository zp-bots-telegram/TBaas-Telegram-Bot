package net.tbaas.telegrambot.conversations;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.tbaas.telegrambot.TBaaSBot;
import net.tbaas.telegrambot.logger.LogLevel;
import net.tbaas.telegrambot.logger.Logger;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.content.TextContent;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableMessage;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.conversations.Conversation;
import pro.zackpollard.telegrambot.api.conversations.ConversationContext;
import pro.zackpollard.telegrambot.api.conversations.prompt.RegexPrompt;
import pro.zackpollard.telegrambot.api.conversations.prompt.TextPrompt;
import pro.zackpollard.telegrambot.api.user.User;

/**
 * @author Zack Pollard
 */
public class AddBotConversation extends BotConversation {

    private final TBaaSBot instance;
    private final TelegramBot telegramBot;

    public AddBotConversation(Chat chat, User user) {

        super(chat, user);
        this.instance = TBaaSBot.getInstance();
        this.telegramBot = instance.getTelegramBot();
    }

    @Override
    public boolean run() {

        Conversation.builder(telegramBot)
                .forWhom(getChat())
                .prompts()
                    .first(new APIKeyPrompt())
                    .then(new GitRepoPrompt())
                    .then(new NeedArgumentsPrompt())
                    .last(new GetArgumentsPrompt())
                .disableGlobalEvents(false)
                .build()
                .begin();
        return true;
    }

    public class APIKeyPrompt extends TextPrompt {

        @Override
        public boolean process(ConversationContext context, TextContent input) {

            TelegramBot userBot = TelegramBot.login(input.getContent());

            if(userBot != null) {

                context.setSessionData("botapikey", userBot.getAuthToken());
                context.setSessionData("botusername", userBot.getBotUsername());
                context.setSessionData("botname", userBot.getBotName());
                context.setSessionData("botid", userBot.getBotID());
                return false;
            }

            context.getFrom().sendMessage("The API key you provided was incorrect. Please try again and correct you pasted the exact API key provided to you by @BotFather");
            return true;
        }

        @Override
        public SendableMessage promptMessage(ConversationContext context) {
            return SendableTextMessage.builder()
                    .message("What's the API key for the bot you are adding?")
                    .build();
        }
    }

    public class GitRepoPrompt extends RegexPrompt {

        protected GitRepoPrompt() {
            super("((git|ssh|http(s)?)|(git@[\\w\\.]+))(:(//)?)([\\w\\.@\\:/\\-~]+)(\\.git)(/)?");
        }

        @Override
        protected boolean accept(ConversationContext context, TextContent input) {

            context.setSessionData("giturl", input.getContent());
            return false;
        }

        @Override
        public SendableMessage promptMessage(ConversationContext context) {
            return SendableTextMessage.builder()
                    .message("What's the git repository for the bot you are adding?")
                    .build();
        }

        @Override
        protected SendableMessage invalidationMessage(ConversationContext context, TextContent input) {
            return SendableTextMessage.builder()
                    .message("The provided url did not match the required format for a git repository.")
                    .build();
        }
    }

    private class NeedArgumentsPrompt extends TextPrompt {

        @Override
        public boolean process(ConversationContext context, TextContent input) {

            if(input.getContent().toLowerCase().startsWith("n")) {

                context.setSessionData("cliarguments", "");
                createBot(context);
            }

            return false;
        }

        @Override
        public SendableMessage promptMessage(ConversationContext context) {
            return SendableTextMessage.builder()
                    .message("Would you like to add any command line arguments to your bot? (Yes/No)")
                    .build();
        }
    }

    private class GetArgumentsPrompt extends TextPrompt {

        @Override
        public boolean process(ConversationContext context, TextContent input) {

            context.setSessionData("cliarguments", input.getContent());
            createBot(context);
            return false;
        }

        @Override
        public SendableMessage promptMessage(ConversationContext context) {
            return SendableTextMessage.builder()
                    .message("Please send your string of command line arguments.")
                    .build();
        }
    }

    private void createBot(ConversationContext context) {

        //TODO: Add URL
        //TODO: Add API key
        int statusCode = 0;
        try {
            statusCode = Unirest.post("//TODO: ADD URL HERE")
                        .header("api_key", "WOO_API_KEY")
                        .field("arguments", context.sessionDataBy("cliarguments"), "application/json")
                        .field("buildtool", "maven")
                        .field("language", "java")
                        .field("owner", getUser().getId())
                        .field("repository", context.sessionDataBy("giturl"))
                        .field("username", context.sessionDataBy("botusername"))
                        .asString()
                        .getStatus();
        } catch (UnirestException e) {
            Logger.log(LogLevel.ERROR, "An exception was thrown whilst trying to create the bot through the API", e);
        }

        if(statusCode != 200) {

            context.getFrom().sendMessage("Something went wrong whilst trying to add your bot. Please try again later.");
        }

        context.getConversation().end();
    }
}