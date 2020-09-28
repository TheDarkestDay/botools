package com.abrenchev;

import com.abrenchev.domain.TelegramUpdate;
import com.abrenchev.exceptions.BotoolsException;
import com.abrenchev.service.TelegramApiService;
import com.abrenchev.updatehandler.*;
import com.abrenchev.updatenotifier.LongPollingUpdateNotifier;
import com.abrenchev.updatenotifier.UpdateNotifier;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Botools {
    public void runBot(String authToken, String botName, Class<?> clazz) {
        Object botInstance = createInstance(clazz);
        UpdateNotifier updateNotifier = new LongPollingUpdateNotifier(authToken);
        TelegramApiService telegramApi = new TelegramApiService(authToken);

        List<TelegramUpdateHandler> handlers = new ArrayList<>();
        handlers.add(new HelpCommandHandler(botInstance));
        handlers.add(new ChannelPostHandler());
        handlers.add(new CommandHandler());
        handlers.add(new MessageHandler());

        updateNotifier.onUpdate((update) -> {
            Object botResponse = runHandlers(handlers, update, botInstance);

            if (botResponse != null) {
                long chatIdToRespond = update.message != null ? update.message.chat.id : update.channel_post.chat.id;
                telegramApi.sendObject(chatIdToRespond, botResponse);
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
