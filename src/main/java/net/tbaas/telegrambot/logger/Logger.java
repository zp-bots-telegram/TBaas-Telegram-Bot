package net.tbaas.telegrambot.logger;

import java.io.PrintStream;

/**
 * @author Zack Pollard
 */
public class Logger {

    public static void log(LogLevel level, String message, Exception exception) {

        PrintStream log;

        if(level == LogLevel.INFO) {

            log = System.out;
        } else {

            log = System.err;
        }

        log.println("[" + level.name() + "] - " + message);

        if(exception != null) {

            log.println("Exception stacktrace follows:");
            exception.printStackTrace();
        }
    }

    public static void log(LogLevel level, String message) {

        log(level, message, null);
    }
}