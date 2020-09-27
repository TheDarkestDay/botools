package com.abrenchev.updatehandler;

import com.abrenchev.annotations.HandleCommand;
import com.abrenchev.domain.TelegramUpdate;

import java.lang.reflect.Method;

public class HelpCommandHandler extends CommandHandler {
    private final String helpText;

    private final String HELP_COMMAND = "/help";

    public HelpCommandHandler(Object botInstance) {
        Class<?> clazz = botInstance.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        StringBuilder helpTextBuilder = new StringBuilder("List of available commands: \n\n");

        for (Method botMethod : methods) {
            if (botMethod.isAnnotationPresent(HandleCommand.class)) {
                String commandName = botMethod.getAnnotation(HandleCommand.class).command();
                String commandDescription = botMethod.getAnnotation(HandleCommand.class).description();
                helpTextBuilder.append(commandName).append(" - ").append(commandDescription).append("\n");
            }
        }

        helpText = helpTextBuilder.toString();
    }

    @Override
    public Object handleUpdate(TelegramUpdate update, Object botInstance) {
        String enteredCommand = extractCommand(update.message);

        if (HELP_COMMAND.equals(enteredCommand)) {
            return helpText;
        }

        return null;
    }
}
