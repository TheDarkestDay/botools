package com.abrenchev;

import com.abrenchev.command.SendMessageCommand;
import com.abrenchev.domain.TelegramUpdate;
import com.abrenchev.exceptions.BotoolsException;
import com.abrenchev.updatehandler.CommandHandler;
import com.abrenchev.updatehandler.DirectMessageHandler;
import com.abrenchev.updatehandler.HelpCommandHandler;
import com.abrenchev.updatehandler.TelegramUpdateHandler;
import com.abrenchev.updatenotifier.LongPollingUpdateNotifier;
import com.abrenchev.updatenotifier.UpdateNotifier;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Botools {
    public void runBot(String authToken, String botName, Class<?> clazz) {
        Object botInstance = createInstance(clazz);
        UpdateNotifier updateNotifier = new LongPollingUpdateNotifier(authToken);

        List<TelegramUpdateHandler> handlers = new ArrayList<>();
        handlers.add(new HelpCommandHandler(botInstance));
        handlers.add(new CommandHandler());
        handlers.add(new DirectMessageHandler());

        updateNotifier.onUpdate((update) -> {
            Object botResponse = runHandlers(handlers, update, botInstance);

            if (botResponse != null) {
                var command = new SendMessageCommand();
                command.execute(authToken, botResponse);
            }
        });
    }

    private Object runHandlers(List<TelegramUpdateHandler> handlers, TelegramUpdate update, Object botInstance) {
        for (TelegramUpdateHandler handler : handlers) {
            var result = handler.handleUpdate(update, botInstance);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new BotoolsException("Failed to create bot instance", exception);
        }
    }
}
